package com.isuhuo.kuaibao.news;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.isuhuo.kuaibao.R;
import com.isuhuo.kuaibao.base.MyAppLocation;
import com.isuhuo.kuaibao.network.NormalPostRequest;
import com.isuhuo.kuaibao.network.SingleVolleyRequestQueue;
import com.isuhuo.kuaibao.network.URLMannager;

import org.json.JSONObject;

import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/9/1.
 */

public class DetailActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_su_bao_web);
        bindPush();
    }

    private void bindPush() {
        String registrationID = JPushInterface.getRegistrationID(DetailActivity.this);
        if (registrationID!=null){
            sendPhoneId(registrationID);
        }
    }

    private void sendPhoneId(String registrationID) {
        HashMap<String, String> mParams = new HashMap<>();
        if (MyAppLocation.app.user.getId()!=null){
            mParams.put("uid",MyAppLocation.app.user.getId());
        }
        mParams.put("phoneId",registrationID);
        NormalPostRequest postRequest = new NormalPostRequest(URLMannager.Base_URL + URLMannager.URL_JPush, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mParams);
        SingleVolleyRequestQueue.getInstance(this).addToRequestQueue(postRequest);
    }
}
