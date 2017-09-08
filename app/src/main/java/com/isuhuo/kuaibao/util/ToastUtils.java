package com.isuhuo.kuaibao.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 鱼 on 2017/8/26.
 */

public class ToastUtils {
    private static Toast toast;
    public static void showToast(Context context,String msg){
        if (toast == null){
            toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }
}
