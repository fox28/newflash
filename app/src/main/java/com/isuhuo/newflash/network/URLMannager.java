package com.isuhuo.newflash.network;

/**
 * Created by Administrator on 2017-08-29.
 */
public class URLMannager {
    public static String Base_URL ="http://news.isuhuo.com/api.php/";
    public static String Get_News = "Index/getNewsList";
    public static String Add_Collect = "User/addCollect";
    public static String Del_Collect = "User/delCollect";
    public static String User_List = "User/userList";
    public static String Add_History = "Index/addNewsHistory";
    public static String Del_History = "Index/delNewsHistory";

    //发送短信
    public static final String URL_SendMess ="Public/sendMessage";
    //提交注册
    public static final String URL_Register = "Public/register";
    //普通登录
    public static final String URL_Login = "Public/login";
    //修改/找回 密码
    public static final String URL_EditPassword = "User/editPassword";
    public static final String URL_JPush = "Index/getPhoneids";
    public static final String URL_ApiLogin = "Public/login";

    // 修改用户头像、昵称
    public static final String URL_UPDATE_AVATAR = "User/editUserHead";
    public static final String URL_UPDATE_NICK = "User/editName";


}
