package com.isuhuo.newsflash.ui.activity.login;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.network.NormalPostRequest;
import com.isuhuo.newsflash.network.SingleVolleyRequestQueue;
import com.isuhuo.newsflash.network.URLMannager;
import com.isuhuo.newsflash.util.SpUtils;
import com.isuhuo.newsflash.util.ToastUtils;

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

public class RegisterActivity extends AppCompatActivity {


    private static final int TIME_CUT = 1;
    private static final int TIME_END = 2;
    @BindView(R.id.iv_back)
    LinearLayout ivBack;
    @BindView(R.id.zhuce_phone)
    EditText zhucePhone;
    @BindView(R.id.zhuce_send)
    Button zhuceSend;
    @BindView(R.id.zhuce_code)
    EditText zhuceCode;
    @BindView(R.id.zhuce_pwd)
    EditText zhucePwd;
    @BindView(R.id.zhuce_eye)
    ImageView zhuceEye;
    @BindView(R.id.zhuce_bt)
    Button zhuceBt;
    @BindView(R.id.zhuce_check)
    ImageView zhuceCheck;
    @BindView(R.id.zhuce_xieyi)
    TextView zhuceXieyi;
    @BindView(R.id.tv_usualLogin)
    TextView tvUsualLogin;
    private boolean isSee;
    private boolean isCheck; //是否被勾选
    private String msg;
    private String putPhone;
    private String putPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuce);
        ButterKnife.bind(this);

        isCheck = true;
        isSee = true;
        init();
    }

    private void init() {
        //设置下划线
        tvUsualLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvUsualLogin.getPaint().setAntiAlias(true);
    }

    @OnClick({R.id.iv_back, R.id.zhuce_send, R.id.zhuce_eye, R.id.zhuce_bt, R.id.zhuce_xieyi, R.id.tv_usualLogin})
    public void onViewClicked(View view) {
        putPhone = zhucePhone.getText().toString();  //拿到用户输入的手机号
        putPwd = zhucePwd.getText().toString();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.zhuce_send:
                //校验手机号
                if (!putPhone.equals("")){
                    //请求服务器获取验证码
                    sendRequest();
                }
                break;
            case R.id.zhuce_eye:
                //密码可见可不见
                if (isSee){
                    zhuceEye.setImageResource(R.mipmap.eyeon);
                    isSee = false;
                    zhucePwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    zhuceEye.setImageResource(R.mipmap.eyeoff);
                    zhucePwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isSee = true;
                }
                break;
            case R.id.zhuce_bt:
                //注册

                if (!zhucePhone.getText().toString().equals("")&&(!zhuceCode.getText().toString().equals(""))
                        &&(!zhucePwd.getText().toString().equals(""))&&(6<=zhucePwd.getText().toString().length()&&zhucePwd.getText().toString().length()<=18)){
                    //TODO 比较短信 从请求服务器获取来的 记得修改
                    if (zhuceCode.getText().toString().equals(msg)){
                        admits();
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
            case R.id.zhuce_xieyi:
                Intent intent=new Intent(this,AgreementActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_usualLogin:
                Intent intent1=new Intent(this,LoginActivity.class);
                startActivity(intent1);
                break;
        }
    }

    //sp保存数据
    private void admits() {
        Map<String,String> mRegisterParams = new HashMap<>();
        mRegisterParams.put("phone",putPhone);
        mRegisterParams.put("password",putPwd);
        mRegisterParams.put("source","3");

        NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_Register, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")){
                        ToastUtils.showToast(RegisterActivity.this,jsonObject.getString("msg"));
                        JSONObject data = jsonObject.getJSONObject("data");
                        saveUser(data);
                        finish();
                    }else{
                        ToastUtils.showToast(RegisterActivity.this,jsonObject.getString("msg"));
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

        SingleVolleyRequestQueue.getInstance(RegisterActivity.this).addToRequestQueue(postRequest);
    }

    private void saveUser(JSONObject data) {
        try {
            SpUtils.putUser(this,"name",data.getString("name"));
            SpUtils.putUser(this,"id",data.getString("id"));
            SpUtils.putUser(this,"phone",data.getString("phone"));
            SpUtils.putUser(this,"password",data.getString("password"));
            SpUtils.putUser(this,"user_head_img",data.getString("user_head_img"));
            SpUtils.putUser(this,"sex",data.getString("sex"));
            SpUtils.putUser(this,"province",data.getString("province"));
            SpUtils.putUser(this,"city",data.getString("city"));
            SpUtils.putUser(this,"login",data.getString("login"));
            SpUtils.putUser(this,"register_type",data.getString("register_type"));
            SpUtils.putUser(this,"source",data.getString("source"));
            SpUtils.putUser(this,"reg_time",data.getString("reg_time"));
            SpUtils.putUser(this,"reg_ip",data.getString("reg_ip"));
            SpUtils.putUser(this,"status",data.getString("status"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest() {
        //TODO 请求服务器
        Map<String,String> mPutParams = new HashMap<>();;
        mPutParams.put("phone",putPhone);
        mPutParams.put("sign","1");
        NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_SendMess, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")){
                        zhuceSend.setEnabled(false); //发送之后不能点击
                        new myclock().execute();  //倒计时方法
                        JSONObject data = jsonObject.getJSONObject("data");
                        msg = data.getString("code");
                    }else {
                        ToastUtils.showToast(RegisterActivity.this,jsonObject.getString("msg"));
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

        SingleVolleyRequestQueue.getInstance(RegisterActivity.this).addToRequestQueue(postRequest);
    }

//    int time = 120;
//    private Handler mHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case TIME_CUT:
//                    zhuceSend.setText(time+"秒");
//                    break;
//                case TIME_END:
//                    zhuceSend.setText("重新发送");
//                    zhuceSend.setEnabled(true);
//                    break;
//            }
//        }
//    };
//    //倒计时
//    private class cutDownTask implements Runnable{
//        @Override
//        public void run() {
//            for (;time>0;time--) {
//                SystemClock.sleep(999);
//                mHandler.sendEmptyMessage(TIME_CUT);
//            }
//            //循环结束,发送一次消息
//            mHandler.sendEmptyMessage(TIME_END);
//            time = 120;
//        }
//    }

    //异步任务进行120秒计时
    public class myclock extends AsyncTask<Void,Integer,String> {

        @Override
        protected String doInBackground(Void... voids) {

            //一个120秒的进程
            for(int i=1;i<=120;i++){
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "请重新发送";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //计时过程实时动态改变textview的显示
            int current=120-values[0];
            zhuceSend.setText(current+"秒后重发");
            //    send.setBackgroundResource(R.drawable.button_bg2);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //计时结束恢复，重新发送
            zhuceSend.setText(s);
            zhuceSend.setEnabled(true);
            //    send.setBackgroundResource(R.drawable.button_bg);
        }
    }
}
