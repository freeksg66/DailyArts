package com.github.arts_social;

/**
 * Created by legao005426 on 2018/6/25.
 */

public interface IShare {

    interface ImageProtocol{
        String HTTPS = "https";
        String HTTP = "http";
    }

    void share(SocialType type ,ShareContent content);
}
