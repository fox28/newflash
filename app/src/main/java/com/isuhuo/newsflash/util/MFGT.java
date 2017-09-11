package com.isuhuo.newsflash.util;

import android.app.Activity;
import android.content.Intent;


import com.isuhuo.newsflash.R;
import com.isuhuo.newsflash.ui.activity.login.LoginActivity;
import com.isuhuo.newsflash.ui.activity.MainActivity;

/**
 * Created by apple on 2017/3/30.
 */

public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }



    public static void startActivity(Activity activity, Class cls) {
        activity.startActivity(new Intent(activity, cls));
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);

    }


    public static void gotoMainActivity(Activity activity) {
        startActivity(activity, MainActivity.class);

    }



//    public static void gotoRegisterActivity(Activity activity) {
////        startActivity(activity, RegisterActivity.class);
//        startActivityForResult(activity, new Intent(activity,RegisterActivity.class),
//                I.REQUEST_CODE_REGISTER);
//    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoLoginActivity(Activity activity) {
        startActivity(activity, LoginActivity.class);
    }
    public static void gotoLoginActivity(Activity activity, int requestCode) {
        startActivityForResult(activity, new Intent(activity, LoginActivity.class), requestCode);
    }

}
