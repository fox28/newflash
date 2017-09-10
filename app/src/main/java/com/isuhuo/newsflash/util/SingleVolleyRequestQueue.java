package com.isuhuo.newsflash.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/5/6.
 */
public class SingleVolleyRequestQueue {
    private static SingleVolleyRequestQueue slingleQueue;
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
        if (slingleQueue == null){
            slingleQueue = new SingleVolleyRequestQueue(context);
        }
        return slingleQueue;
    }
    public  void  addToRequestQueue(Request req){
        getRequestQueue().add(req);
    }


}
