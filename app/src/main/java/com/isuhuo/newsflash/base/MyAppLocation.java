package com.isuhuo.newsflash.base;

import android.app.Application;


import com.isuhuo.newsflash.util.UserBeen;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2017/8/30.
 */

public class MyAppLocation extends Application{
    public UserBeen user;
    // 拿到application上下文
    public static MyAppLocation app;
    @Override
    public void onCreate() {
        app = this;
        super.onCreate();

        Config.DEBUG=true;
        UMShareAPI.get(this);

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }

    {
        PlatformConfig.setQQZone("1106385986","mC49ca71ZY23gpTd");
        PlatformConfig.setSinaWeibo("3428507896","c376c036bf2991f1d9aa8a2705d02a1c","http://www.isuhuo.com/");

    }
    public UserBeen getUser() {
        return user;
    }

    public void setUser(UserBeen user) {
        this.user = user;
    }
    public MyAppLocation() {
        super();
    }

    public static MyAppLocation getApp() {
        return app;
    }

    public static void setApp(MyAppLocation app) {
        MyAppLocation.app = app;
    }
}
