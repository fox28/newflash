package com.isuhuo.newflash.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.isuhuo.newflash.R;
import com.isuhuo.newflash.network.NormalPostRequest;
import com.isuhuo.newflash.network.SingleVolleyRequestQueue;
import com.isuhuo.newflash.network.URLMannager;
import com.isuhuo.newflash.util.SMSUtil;
import com.isuhuo.newflash.util.SpUtils;
import com.isuhuo.newflash.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 鱼 on 2017/8/26.
 */
public class ForgetPasswordActivity extends AppCompatActivity {
    private static final int TIME_CUT = 1;
    private static final int TIME_END = 2;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.xiugai_phone)
    EditText xiugaiPhone;
    @BindView(R.id.xiugai_send)
    Button xiugaiSend;
    @BindView(R.id.xiugai_code)
    EditText xiugaiCode;
    @BindView(R.id.xiugai_pwd)
    EditText xiugaiPwd;
    @BindView(R.id.xiugai_eye)
    ImageView xiugaiEye;
    @BindView(R.id.xiugai_bt)
    Button xiugaiBt;
    private String putPhone;
    private String msg;
    private String putNewPwd;
    private boolean isSee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugai);
        ButterKnife.bind(this);
        isSee = true;
    }

    @OnClick({R.id.ll_back, R.id.xiugai_send, R.id.xiugai_eye, R.id.xiugai_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.xiugai_send:
                putPhone = xiugaiPhone.getText().toString();
                if (SMSUtil.judgePhoneNums(this,putPhone)){
                    //请求服务器获取验证码
                    sendRequest();

                }
                break;
            case R.id.xiugai_eye:
                //密码可见可不见
                if (isSee){
                    xiugaiEye.setImageResource(R.mipmap.eyeon);
                    isSee = false;
                    xiugaiPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    xiugaiEye.setImageResource(R.mipmap.eyeoff);
                    xiugaiPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isSee = true;
                }
                break;
            case R.id.xiugai_bt:
                if (!xiugaiPhone.getText().toString().equals("")&&(!xiugaiCode.getText().toString().equals(""))
                        &&(!xiugaiPwd.getText().toString().equals(""))&&(6<=xiugaiPwd.getText().toString().length()&&xiugaiPwd.getText().toString().length()<=18)){
                    //TODO 比较短信 从请求服务器获取来的 记得修改
                    if (xiugaiCode.getText().toString().equals(msg)){
                        putNewPwd = xiugaiPwd.getText().toString();
                        admitNewPwd();  //提交
//                            ToastUtils.showToast(this,"成功");
                        Intent intent =  new Intent(this,LoginActivity.class);
//                            intent.putExtra("phone",putPhone);
                        startActivity(intent);
                    }else{
                        ToastUtils.showToast(this,"验证码不对");
                    }
                }else{
                    ToastUtils.showToast(this,"请将信息填写完整or密码长度为6-18位");
                }
                break;
        }
    }

    private void admitNewPwd() {
        Map<String,String> mRegisterParams = new HashMap<>();
        mRegisterParams.put("sign","2");
        mRegisterParams.put("password",putNewPwd);
        mRegisterParams.put("phone",putPhone);

        final NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_EditPassword, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")){
                        ToastUtils.showToast(ForgetPasswordActivity.this,jsonObject.getString("msg"));
                    //    MyAppLocation.user.setPassword(putNewPwd);
                        //保存新密码
                        SpUtils.putUser(ForgetPasswordActivity.this,"password",putNewPwd);
                        finish();
                    }else{
                        ToastUtils.showToast(ForgetPasswordActivity.this,jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mRegisterParams);

        SingleVolleyRequestQueue.getInstance(ForgetPasswordActivity.this).addToRequestQueue(postRequest);
    }


    private void sendRequest() {
        Map<String,String> mPutParams = new HashMap<>();;
        mPutParams.put("phone",putPhone);
        mPutParams.put("sign","2");  //发送短信 sign=2代表找回密码
        NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_SendMess, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")){
                        //进行倒计时
                        xiugaiSend.setEnabled(false); //发送之后不能点击
                        new Thread(new cutDownTask()){}.start();  //倒计时方法
                        JSONObject data = jsonObject.getJSONObject("data");
                        msg = data.getString("code");
                        ToastUtils.showToast(ForgetPasswordActivity.this,jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }, mPutParams);

        SingleVolleyRequestQueue.getInstance(ForgetPasswordActivity.this).addToRequestQueue(postRequest);
    }

    int time = 120;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_CUT:
                    xiugaiSend.setText(time+"秒");
                    break;
                case TIME_END:
                    xiugaiSend.setText("重新发送");
                    xiugaiSend.setEnabled(true);
                    break;
            }
        }
    };
    //倒计时
    private class cutDownTask implements Runnable{
        @Override
        public void run() {
            for (;time>0;time--) {
                SystemClock.sleep(999);
                mHandler.sendEmptyMessage(TIME_CUT);
            }
            //循环结束,发送一次消息
            mHandler.sendEmptyMessage(TIME_END);
            time = 120;
        }
    }
}
