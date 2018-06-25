package com.github.arts_social;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class SocialApplication {
    private SocialApplication() {

    }

    /**
     * 用于违章APP的初始化，主要是重置各种三方key，默认是从assets.ShareSDK.xml读取，数据是搜狐汽车APP的key
     */
    public static void initByDriveHelper() {
        initWeibo();
        initQZone();
        initWechat();
        initWechatMoments();
        initWechatFavorite();
        initQQ();
    }

    private static void initWeibo() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", "1");
        hashMap.put("SortId", "1");
        hashMap.put("AppKey", "2773246890");
        hashMap.put("AppSecret", "3bb872ae0f0437b1e2e538b9cf9579bd");
        hashMap.put("RedirectUrl", "http://sns.whalecloud.com/sina2/callback");
        hashMap.put("ShareByAppClient", "true");
        hashMap.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(SinaWeibo.NAME, hashMap);
    }


    private static void initQZone() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", "3");
        hashMap.put("SortId", "3");
        hashMap.put("AppId", "100733772");
        hashMap.put("AppKey", "d1928dd946dabb8e74d85d87e6642f4f");
        hashMap.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(QZone.NAME, hashMap);
    }


    private static void initWechat() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", "4");
        hashMap.put("SortId", "4");
        hashMap.put("AppId", "wx77cccf7e5d6dfdb3");
        hashMap.put("AppSecret", "d0e6385ed6fb1916f8d4bfcb971329b3");
        hashMap.put("BypassApproval", "false");
        hashMap.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(Wechat.NAME, hashMap);
    }

    private static void initWechatMoments() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", "5");
        hashMap.put("SortId", "5");
        hashMap.put("AppId", "wx77cccf7e5d6dfdb3");
        hashMap.put("AppSecret", "d0e6385ed6fb1916f8d4bfcb971329b3");
        hashMap.put("BypassApproval", "false");
        hashMap.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(WechatMoments.NAME, hashMap);
    }

    private static void initWechatFavorite() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", "6");
        hashMap.put("SortId", "6");
        hashMap.put("AppId", "wx77cccf7e5d6dfdb3");
        hashMap.put("AppSecret", "d0e6385ed6fb1916f8d4bfcb971329b3");
        hashMap.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(WechatFavorite.NAME, hashMap);
    }

    private static void initQQ() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Id", "2");
        hashMap.put("SortId", "2");
        hashMap.put("AppId", "100733772");
        hashMap.put("AppKey", "d1928dd946dabb8e74d85d87e6642f4f");
        hashMap.put("ShareByAppClient", "true");
        hashMap.put("Enable", "true");
        ShareSDK.setPlatformDevInfo(QQ.NAME, hashMap);
    }
}
