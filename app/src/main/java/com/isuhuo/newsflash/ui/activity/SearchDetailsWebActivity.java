package com.isuhuo.newsflash.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.base.BaseActivity;

import com.isuhuo.newsflash.network.NormalPostRequest;
import com.isuhuo.newsflash.network.URLMannager;

public class SearchDetailsWebActivity extends BaseActivity {
    private WebView webView;
    private LinearLayout back;
    private TextView titleTv;
    private String id;
    private String url;
    private String title, collect_status;
    private Button shoucBtn;
    private Button yscBtn;
    private Map<String,String> params;
    private int resultCode = 0;
    private int shoucang = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_details_web);
        Intent intent=getIntent();
        id = intent.getStringExtra("id");
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("name");
        back=(LinearLayout)findViewById(R.id.my_set_fanhui);
        shoucBtn = (Button)findViewById(R.id.shoucBtn);
        yscBtn = (Button)findViewById(R.id.yscBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent date = new Intent();
                date.putExtra("shoucang", shoucang);
                setResult(resultCode, date);
                finish();
            }
        });
        titleTv = (TextView) findViewById(R.id.head_title);
        titleTv.setText(title);
        webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl(url);
       /* if(BaseApplication.app.getUser()!=null){
            collect_status = intent.getStringExtra("collect_status");
            if(collect_status.equals("1")){
                shoucBtn.setVisibility(View.GONE);
                yscBtn.setVisibility(View.VISIBLE);
            }else{
                shoucBtn.setVisibility(View.VISIBLE);
                yscBtn.setVisibility(View.GONE);
            }
        }*/
        shoucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoucang();
            }
        });
        yscBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qxshoucang();
            }
        });
        addHistory();
    }

    private void addHistory() {
        params = new HashMap<String, String>();

        params.put("uid", "MDAwMDAwMDAwMICdi3M");
        params.put("news_id", id);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL +  URLMannager.Add_History,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("success")){
                                shoucBtn.setVisibility(View.VISIBLE);
                                yscBtn.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(SearchDetailsWebActivity.this,"sss",Toast.LENGTH_SHORT).show();
            }
        }, params);

        requestQueue.add(request);
    }

    private void qxshoucang() {
        params = new HashMap<String, String>();

        params.put("uid", "MDAwMDAwMDAwMICdi3M");
        params.put("news_ids", id);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        final Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL +  URLMannager.Del_Collect,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("success")){
                                    shoucBtn.setVisibility(View.VISIBLE);
                                    yscBtn.setVisibility(View.GONE);
                            }
                            shoucang = 2;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(SearchDetailsWebActivity.this,"sss",Toast.LENGTH_SHORT).show();
            }
        }, params);

        requestQueue.add(request);
    }

    private void shoucang() {
        params = new HashMap<String, String>();
        params.put("uid", "MDAwMDAwMDAwMICdi3M");
        params.put("news_id", id+"");
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        final Request<JSONObject> request = new NormalPostRequest(URLMannager.Base_URL + URLMannager.Add_Collect,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("success")){
                                if(shoucBtn.isShown()){
                                    shoucBtn.setVisibility(View.GONE);
                                    yscBtn.setVisibility(View.VISIBLE);
                                }else{
                                    shoucBtn.setVisibility(View.VISIBLE);
                                    yscBtn.setVisibility(View.GONE);
                                }
                                shoucang = 1;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }, params);

        requestQueue.add(request);
    }

}
