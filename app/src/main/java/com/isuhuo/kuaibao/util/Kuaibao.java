package com.isuhuo.kuaibao.util;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-08-28.
 */
public class Kuaibao implements Serializable{
    public String id;
    public String name;
    public String content;
    public String img_url_1;
    public String release_time;
    public String faburen;
    public String collect_status;
    public boolean isDelet;

    public boolean isDelet() {
        return isDelet;
    }

    public void setDelet(boolean delet) {
        isDelet = delet;
    }

    public String getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(String collect_status) {
        this.collect_status = collect_status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String url;

    public String getFaburen() {
        return faburen;
    }

    public void setFaburen(String faburen) {
        this.faburen = faburen;
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url_1() {
        return img_url_1;
    }

    public void setImg_url_1(String img_url_1) {
        this.img_url_1 = img_url_1;
    }

    public String getRelease_time() {
        return release_time;
    }

    public void setRelease_time(String release_time) {
        this.release_time = release_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Kuaibao() {
        super();
    }

    public Kuaibao(String id, String name, String img_url_1, String content, String release_time, String faburen, String url, String collect_status) {
        this.id = id;
        this.name = name;
        this.img_url_1 = img_url_1;
        this.content = content;
        this.release_time = release_time;
        this.faburen = faburen;
        this.url = url;
        this.collect_status = collect_status;
    }
}
