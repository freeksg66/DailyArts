package com.github.arts_social;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class ShareUtils {
    interface ImageProtocol{
        String HTTPS = "https";
        String HTTP = "http";
    }

    public static String checkUrl(String url){
        if (null == url)return url;
        if (!url.startsWith(ImageProtocol.HTTP) && !url.startsWith(ImageProtocol.HTTPS)){
            return "http:"+url;
        }
        return url;
    }
}
