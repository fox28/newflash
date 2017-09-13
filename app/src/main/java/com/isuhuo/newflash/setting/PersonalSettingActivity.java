package com.isuhuo.newflash.setting;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.isuhuo.newflash.R;
import com.isuhuo.newflash.base.MyAppLocation;
import com.isuhuo.newflash.network.MultiPartRequest;
import com.isuhuo.newflash.network.URLMannager;
import com.isuhuo.newflash.ui.utils.ClipImageActivity;
import com.isuhuo.newflash.ui.utils.view.CircleImageView;
import com.isuhuo.newflash.util.MFGT;
import com.isuhuo.newflash.util.SingleVolleyRequestQueue;
import com.isuhuo.newflash.util.SpUtils;
import com.isuhuo.newflash.util.ToastUtils;
import com.isuhuo.newflash.util.UserBeen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PersonalSettingActivity extends AppCompatActivity {
    private static final String TAG = "PersonalSettingActivity";
    private UserBeen user = new UserBeen();
    private Bundle mBundle;

    @BindView(R.id.iv_user_profile_avatar)
    ImageView mIvUserProfileAvatar;
    @BindView(R.id.tv_user_profile_nick)
    TextView mTvUserProfileNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_setting);
        ButterKnife.bind(this);

        user = MyAppLocation.app.getUser();
        updatePersonalProfile();
        mBundle = savedInstanceState;
    }

    @OnClick({R.id.backArea, R.id.layout_user_profile_avatar, R.id.layout_user_profile_nickname})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backArea:
                MFGT.finish(PersonalSettingActivity.this);
                break;
            case R.id.layout_user_profile_avatar:
                //创建拍照存储的临时文件
                createCameraTempFile(mBundle);
                uploadHeadImage();

                break;
            case R.id.layout_user_profile_nickname:
                MFGT.gotoUpdateNickActivity(PersonalSettingActivity.this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Log.e(TAG, "cropImagePath = " + cropImagePath);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    Log.e(TAG, "bitMap = " + bitMap);
                    mIvUserProfileAvatar.setImageBitmap(bitMap);
                    Log.e(TAG, "bitmap 加载中……");
//                    if (type == 1) {
//                        headImage1.setImageBitmap(bitMap);
//                    } else {
//                        headImage2.setImageBitmap(bitMap);
//                    }
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    File file = saveBitmapFile(bitMap);
                    // 上传服务器
                    updateAvatar(file);

                }
                break;
        }
    }

    private void updateAvatar(File file) {

        //构造参数列表
        List<Part> partList = new ArrayList<Part>();
        partList.add(new StringPart("uid", user.getId()));
        try {
            partList.add(new FilePart("file", file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //获取队列
        RequestQueue requestQueue = SingleVolleyRequestQueue.getInstance(this).getRequestQueue();
        String url = "http://test/profileUpdate.do";
        //生成请求
        MultiPartRequest profileUpdateRequest = new MultiPartRequest(URLMannager.Base_URL+URLMannager.URL_UPDATE_AVATAR,
                partList.toArray(new Part[partList.size()]), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //处理成功返回信息
                Toast.makeText(getApplication(), "修改成功", Toast.LENGTH_SHORT).show();
                // {"status":"success","msg":"\u64cd\u4f5c\u6210\u529f\uff01","data":{"url":"http:\/\/news.isuhuo.com\/Uploads\/User\/59b77cf3305ed.jpg"}}
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("success")){
                        ToastUtils.showToast(PersonalSettingActivity.this,jsonObject.getString("msg"));
                        JSONObject data = jsonObject.getJSONObject("data");
                        SpUtils.putUser(PersonalSettingActivity.this,"user_head_img",data.getString("url"));
                        user.setUser_head_img(data.getString("url"));
                        MFGT.finish(PersonalSettingActivity.this);
                    }else{
                        ToastUtils.showToast(PersonalSettingActivity.this,jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //处理失败错误信息
                Log.e("MultipartRequest", error.getMessage(), error);
                Toast.makeText(getApplication(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //将请求加入队列
        requestQueue.add(profileUpdateRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePersonalProfile();
    }

    private void updatePersonalProfile() {
        if (user != null) {
            Glide.with(PersonalSettingActivity.this)
                    .load(user.getUser_head_img())
                    .into(mIvUserProfileAvatar);
            // user.getName() == SpUtils.getUser(this, "name", null)
            mTvUserProfileNick.setText(user.getName());
        }
    }

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像1
    private CircleImageView headImage1;
    //头像2
    private ImageView headImage2;
    //调用照相机返回图片临时文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(PersonalSettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersonalSettingActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCarema();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(PersonalSettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(PersonalSettingActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统图库
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 将bitmap转换或file文件
     *
     * @param bitmap
     * @return
     */
    private File saveBitmapFile(Bitmap bitmap) {
        if (bitmap != null) {
            String imagePath = getAvatarPath(PersonalSettingActivity.this, user.getName()) + ".jpg";
            File file = new File(imagePath); // 将要保存图片的途径
            Log.e(TAG, "file = " + file.getAbsolutePath());
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        return null;
    }

    /**
     * 返回头像保存在sd卡的位置:
     * Android/data/cn.ucai.superwechat/files/pictures/user_avatar
     *
     * @param context
     * @param path
     * @return
     */
    public static String getAvatarPath(Context context, String path) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File folder = new File(dir, path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder.getAbsolutePath();
    }


}
