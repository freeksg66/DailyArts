package com.github.dailyarts.router;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class RouterUriBuilder {
    String SCHEMA_HOSTS = "dailyarts://dailyarts.github.com";
    String AND_STR = "&";
    String QUESTION_STR = "?";
    String EQUAL_STR = "=";

    public RouterUriBuilder() {
    }

    private StringBuilder baseUri;

    public RouterUriBuilder baseUri(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("baseUri is null");
        }
        baseUri = new StringBuilder(SCHEMA_HOSTS + path);
        return this;
    }

    /**
     * 该处encode 是为了router内部decode 逆向解决%号编码问题
     * router 默认需要标准Uri 即？kry=value & kye= value 中的key value已经encode过
     * 因此 本地构造uri的时候禁止传入encode 过的key value
     * <p>
     * dailyarts://dailyarts.github.com/web/simple?url=http://haochebang.auto.sohu.com&title=好车榜
     *
     * @param key
     * @param value
     * @return
     */
    public RouterUriBuilder addParams(String key, String value) {
        if (baseUri.toString().contains(QUESTION_STR)) {
            baseUri.append(AND_STR);
        } else {
            baseUri.append(QUESTION_STR);
        }
        baseUri.append(Uri.encode(key)).append(EQUAL_STR).append(Uri.encode(value));
        return this;
    }

    public Uri buildUri() {
        if (TextUtils.isEmpty(baseUri)) {
            throw new IllegalArgumentException("baseUri is null");
        }
        return Uri.parse(baseUri.toString());
    }
}
