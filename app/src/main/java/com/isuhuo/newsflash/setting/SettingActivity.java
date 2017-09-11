package com.isuhuo.newsflash.setting;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.base.MyAppLocation;
import com.isuhuo.newsflash.util.DataCleanManager;
import com.isuhuo.newsflash.util.MFGT;
import com.isuhuo.newsflash.util.ToastUtils;
import com.isuhuo.newsflash.util.UserBeen;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/29.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SettingActivity";
    private UserBeen user = new UserBeen();

    @BindView(R.id.setting_fanhui)
    LinearLayout settingFanhui;
    @BindView(R.id.iv_edit_next)
    ImageView ivEditNext;
    @BindView(R.id.set_edit)
    FrameLayout setEdit;
    @BindView(R.id.tv_set_cache)
    TextView tvSetCache;
    @BindView(R.id.set_clean)
    RelativeLayout setClean;
    @BindView(R.id.tv_set_ziti)
    TextView tvSetZiti;
    @BindView(R.id.set_ziti)
    RelativeLayout setZiti;
    @BindView(R.id.wifi)
    TextView wifi;
    @BindView(R.id.tv_set_net)
    TextView tvSetNet;
    @BindView(R.id.set_wifi)
    RelativeLayout setWifi;
    @BindView(R.id.tv_set_version)
    TextView tvSetVersion;
    @BindView(R.id.set_version)
    RelativeLayout setVersion;
    @BindView(R.id.set_tuichu)
    Button setTuichu;
    @BindView(R.id.tv_suhuo)
    TextView tvSuhuo;
    private PopupWindow popupWindow;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x01:
                    tvSetCache.setText("0.0KB");
                    break;
            }
        };
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        user = MyAppLocation.app.getUser();
        // 判断是否登录，然后显示/隐藏setEdit
        if (user == null) {
            setEdit.setVisibility(View.GONE);
        } else {
            setEdit.setVisibility(View.VISIBLE);
        }
        //获取版本号
        initVersion();
        //获取缓存文件大小
        getCacheSize();
    }

    private void getCacheSize() {
        File file =new File(this.getCacheDir().getPath());
        try {
            tvSetCache.setText(DataCleanManager.getCacheSize(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initVersion() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            //0代表获取当前的版本信息
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            tvSetVersion.setText(packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.setting_fanhui, R.id.set_edit, R.id.set_clean, R.id.set_ziti, R.id.set_wifi, R.id.set_tuichu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_fanhui:
                finish();
                break;
            case R.id.set_edit: //编辑资料的图标

                // TODO 判断是否登录
                if (user != null) {
                    MFGT.gotoPersonalSettingActivity(SettingActivity.this);
                    setEdit.setVisibility(View.VISIBLE);
                }
//                else {
//                    setEdit.setVisibility(View.GONE);
//                }
//                setEdit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });

                break;
            case R.id.set_clean:    //点击清除缓存
                parseCacheClean();
                break;
            case R.id.set_ziti:     //设置字体大小
                parseTextSize();
                break;
            case R.id.set_wifi:     //网络情况下载
                parseLoadImage();
                break;
            case R.id.set_tuichu:  //退出登录

                break;
        }
    }

    //
    private void parseLoadImage() {

    }

    //修改字体大小
    private void parseTextSize() {

    }

    private void parseCacheClean() {
        //popupWindow里面填充的布局
        View cleanView = getLayoutInflater().inflate(R.layout.view_clean_cache, null, false);
        popupWindow = new PopupWindow(cleanView);
        //因为给添加了灰色背景,点击出现, 因此代码设置popupWindow的宽高
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //popupWindow放在哪个布局上
        View rootview = LayoutInflater.from(SettingActivity.this).inflate(R.layout.activity_setting, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM,0,0);
        TextView mSure = (TextView) cleanView.findViewById(R.id.tv_queding);
        TextView mCancel = (TextView) cleanView.findViewById(R.id.tv_quxiao);
        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);

    }

    //清除缓存布局里的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_queding:   //点击确定 开始清除缓存
                //handler子主交互
                Message msg = new Message();
                DataCleanManager.cleanInternalCache(getApplicationContext());
                msg.what = 0x01;
                handler.sendMessageDelayed(msg, 800);

                ToastUtils.showToast(this,"清除成功");
                popupWindow.dismiss();
                break;
            case R.id.tv_quxiao:
               // ToastUtils.showToast(SettingActivity.this,"取消了");
                popupWindow.dismiss();
                break;
        }
    }
}
