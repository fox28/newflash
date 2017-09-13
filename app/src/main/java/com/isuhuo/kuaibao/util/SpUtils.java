package com.isuhuo.kuaibao.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/8/30.
 */

public class SpUtils {
    private static final String FILE_NAME = "share_date";

    public static void putUser(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static String getUser(Context context,String key,String dValue){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key,dValue);
    }
}
