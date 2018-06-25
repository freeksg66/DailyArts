package com.github.arts_social;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class ShareContent {

    public String title;//标题
    public String cover;//封面Url
    public String description;//内容简介
    public String url;//链接


    public ShareContent(String title, String cover, String url) {
        this.title = title;
        this.cover = cover;
        this.url = url;
    }

    public ShareContent(String title, String cover, String description, String url) {
        this.title = title;
        this.cover = cover;
        this.description = description;
        this.url = url;
    }
}
