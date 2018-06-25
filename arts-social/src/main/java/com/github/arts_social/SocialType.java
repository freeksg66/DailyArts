package com.github.arts_social;

/**
 * Created by legao005426 on 2018/6/25.
 */

public enum SocialType {

    QQ(10),
    QZONE(11),
    WECHAT(20),
    MOMENT(21),
    SINA(30),
    CLIPBOARD(40);

    private int type;

    SocialType(int type){
        this.type = type;
    }
}
