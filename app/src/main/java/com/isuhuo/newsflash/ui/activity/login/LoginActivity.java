package com.isuhuo.newsflash.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.base.MyAppLocation;
import com.isuhuo.newsflash.network.NormalPostRequest;
import com.isuhuo.newsflash.network.SingleVolleyRequestQueue;
import com.isuhuo.newsflash.network.URLMannager;
import com.isuhuo.newsflash.util.SpUtils;
import com.isuhuo.newsflash.util.ToastUtils;
import com.isuhuo.newsflash.util.UserBeen;
import com.umeng.socialize.UMShareAPI;

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

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_fanhui)
    LinearLayout loginFanhui;
    @BindView(R.id.log_uname)
    EditText logUname;
    @BindView(R.id.log_upwd)
    EditText logUpwd;
    @BindView(R.id.log_bt)
    Button logBt;
    @BindView(R.id.login_xiugai)
    TextView loginXiugai;
    @BindView(R.id.login_zhuce)
    TextView loginZhuce;
    @BindView(R.id.login_qq)
    ImageView loginQq;
    @BindView(R.id.login_wb)
    ImageView loginWb;
    String login;
    private String uId;
    private String uName;
    private String uHeadIcon;
    private int resultCode = 2;
    private String shoucang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.login_fanhui, R.id.log_bt, R.id.login_xiugai, R.id.login_zhuce, R.id.login_qq, R.id.login_wb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_fanhui:
                finish();
                break;
            case R.id.log_bt:
                String putName = logUname.getText().toString();
                String putPwd = logUpwd.getText().toString();
                if(putName.length()==11){    //判断是用户名还是手机号登陆
                    boolean isphone=true;
                    for(int i=0;i<putName.length();i++ ){
                        if (!Character.isDigit(putName.charAt(i))){
                            isphone=false;
                        }
                    }
                    if(isphone) {
                        login = "3";
                        httpRequest(putName, putPwd);
                    }
                    else {
                        ToastUtils.showToast(LoginActivity.this,"请输入正确的手机号");
                    }
                }
                else
                if(putName.length()<=10){
                    login="1";
                    httpRequest(putName,putPwd);
                }
                else{
                    ToastUtils.showToast(LoginActivity.this,"请输入正确的用户名/手机号！");
                }

                break;
            case R.id.login_xiugai:
                Intent intent1 = new Intent(this,ForgetPasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.login_zhuce:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_qq:
                break;
            case R.id.login_wb:
                break;
        }
    }
    private void otherWBLogin() {
        Map<String, String> mQQParams= new HashMap<>();
        mQQParams.put("name",uName);
        mQQParams.put("open_id",uId);
        mQQParams.put("img_url",uHeadIcon);
        mQQParams.put("source","3");
        mQQParams.put("title","微博");
        NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_ApiLogin, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        UserBeen userBeen = new UserBeen();
                        userBeen.setId(data.getString("id"));
                        userBeen.setName(data.getString("name"));
                        userBeen.setPassword(data.getString("password"));
                        userBeen.setUser_head_img(data.getString("user_head_img"));
                        userBeen.setPhone(data.getString("phone"));
                        userBeen.setSex(data.getString("sex"));
                        userBeen.setProvince(data.getString("province"));//省份
                        userBeen.setCity(data.getString("city"));
                        userBeen.setLogin(data.getString("login"));
                        userBeen.setRegister_type(data.getString("register_type"));
                        userBeen.setSource(data.getString("source"));
                        userBeen.setReg_time(data.getString("reg_time"));
                        userBeen.setReg_ip(data.getString("reg_ip"));
                        userBeen.setStatus(data.getString("status"));
                        MyAppLocation.app.setUser(userBeen);
                        saveUser(userBeen);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mQQParams);
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(postRequest);
    }


    private void otherQQLogin() {
        Map<String, String> mQQParams= new HashMap<>();
        mQQParams.put("name",uName);
        mQQParams.put("open_id",uId);
        mQQParams.put("img_url",uHeadIcon);
        mQQParams.put("source","3");
        mQQParams.put("title","QQ");
        NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_ApiLogin, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        UserBeen userBeen = new UserBeen();
                        userBeen.setId(data.getString("id"));
                        userBeen.setName(data.getString("name"));
                        userBeen.setPassword(data.getString("password"));
                        userBeen.setUser_head_img(data.getString("user_head_img"));
                        userBeen.setPhone(data.getString("phone"));
                        userBeen.setSex(data.getString("sex"));
                        userBeen.setProvince(data.getString("province"));//省份
                        userBeen.setCity(data.getString("city"));
                        userBeen.setLogin(data.getString("login"));
                        userBeen.setRegister_type(data.getString("register_type"));
                        userBeen.setSource(data.getString("source"));
                        userBeen.setReg_time(data.getString("reg_time"));
                        userBeen.setReg_ip(data.getString("reg_ip"));
                        userBeen.setStatus(data.getString("status"));
                        MyAppLocation.app.setUser(userBeen);
                        saveUser(userBeen);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mQQParams);
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(postRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
    private void httpRequest(String putName, String putPwd) {
        Map<String,String> mLoginParams = new HashMap<>();
        mLoginParams.put("name",putName);
        mLoginParams.put("password",putPwd);
        NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_Login, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("status").equals("success")){
                        ToastUtils.showToast(LoginActivity.this,jsonObject.getString("msg"));
                        //获取返回的数据
                        JSONObject data = jsonObject.getJSONObject("data");
                        UserBeen userBeen = new UserBeen();
                        userBeen.setId(data.getString("id"));
                        userBeen.setName(data.getString("name"));
                        userBeen.setPassword(data.getString("password"));
                        userBeen.setUser_head_img(data.getString("user_head_img"));
                        userBeen.setPhone(data.getString("phone"));
                        userBeen.setSex(data.getString("sex"));
                        userBeen.setProvince(data.getString("province")+"你的省份");//省份
                        userBeen.setCity(data.getString("city")+"你的城市");
                        userBeen.setLogin(data.getString("login"));
                        userBeen.setRegister_type(data.getString("register_type"));
                        userBeen.setSource(data.getString("source"));
                        userBeen.setReg_time(data.getString("reg_time"));
                        userBeen.setReg_ip(data.getString("reg_ip"));
                        userBeen.setStatus(data.getString("status"));
                        MyAppLocation.app.setUser(userBeen);
                        saveUser(userBeen);
                        Intent date = new Intent();
                        date.putExtra("denglu",data.getString("user_head_img"));
                        date.putExtra("dengluname",data.getString("name"));
                        setResult(resultCode, date);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mLoginParams);
        SingleVolleyRequestQueue.getInstance(LoginActivity.this).addToRequestQueue(postRequest);
    }
    private void saveUser(UserBeen userBeen) {
        SpUtils.putUser(this,"id",userBeen.getId());
        SpUtils.putUser(this,"name",userBeen.getName());
        SpUtils.putUser(this,"password",userBeen.getPassword());
        SpUtils.putUser(this,"user_head_img",userBeen.getUser_head_img());
        SpUtils.putUser(this,"phone",userBeen.getPhone());
        SpUtils.putUser(this,"sex",userBeen.getSex());
        SpUtils.putUser(this,"province",userBeen.getProvince());//省份
        SpUtils.putUser(this,"city",userBeen.getCity());
        SpUtils.putUser(this,"login",userBeen.getLogin());
        SpUtils.putUser(this,"register_type",userBeen.getRegister_type());
        SpUtils.putUser(this,"source",userBeen.getSource());
        SpUtils.putUser(this,"reg_time",userBeen.getReg_time());
        SpUtils.putUser(this,"reg_ip",userBeen.getReg_ip());
        SpUtils.putUser(this,"status",userBeen.getStatus());
    }

    //http://www.jianshu.com/p/db4136d70226
    //http://www.jianshu.com/p/db4136d70226
}
