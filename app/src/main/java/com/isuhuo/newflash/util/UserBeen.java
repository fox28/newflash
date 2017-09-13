package com.isuhuo.newflash.util;

/**
 * Created by Administrator on 2017/8/30.
 */

public class UserBeen {
/*
*  "status": "success",
    "msg": "登陆成功",
    "data": {
        "id": "MDAwMDAwMDAwMICdj3M",
        "name": "DDCr201708",
        "password": "72280e429cd08e9462739d16e4d1eede",
        "user_head_img": "http://news.isuhuo.com/Uploads/Default/default.jpg",
        "phone": "15726653195",
        "sex": "0",
        "province": null,
        "city": null,
        "login": "1",
        "register_type": "1",
        "source": "3",
        "reg_time": "1504075672",
        "reg_ip": "2077206435",
        "status": "1"*/
    private String name;
    private String id;
    private String password;
    private String phone;
    private String user_head_img;
    private String sex;
    private String province;
    private String city;
    private String register_type;
    private String source;
    private String reg_time;
    private String reg_ip;
    private String status;
    private String login;   // "1"是登录成功

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_head_img() {
        return user_head_img;
    }

    public void setUser_head_img(String user_head_img) {
        this.user_head_img = user_head_img;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegister_type() {
        return register_type;
    }

    public void setRegister_type(String register_type) {
        this.register_type = register_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getReg_ip() {
        return reg_ip;
    }

    public void setReg_ip(String reg_ip) {
        this.reg_ip = reg_ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public UserBeen(){}

    public void initUser(){
        name=null;
        id=null;
        password=null;
        phone=null;
        user_head_img=null;
        sex=null;
        province=null;
        city=null;
        register_type=null;
        source=null;
        reg_time=null;
        reg_time=null;
        reg_ip=null;
        login=null;
    }
}
