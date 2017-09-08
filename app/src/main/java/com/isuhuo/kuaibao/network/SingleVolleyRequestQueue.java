package com.isuhuo.kuaibao.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2017/8/30.
 */

public class SingleVolleyRequestQueue {
    private static SingleVolleyRequestQueue singleQueue;
    private RequestQueue requestQueue;
    private static Context context;

    //私有化构造
    private SingleVolleyRequestQueue(Context context){
        this.context = context;
        requestQueue = getRequestQueue();
    }
    //提供获取请求队列的方法
    public RequestQueue getRequestQueue() {
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }
    //提供获取类对象的方法
    public static synchronized SingleVolleyRequestQueue getInstance(Context context){
        if (singleQueue == null){
            singleQueue = new SingleVolleyRequestQueue(context);
        }
        return singleQueue;
    }
    public  void  addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }
}
