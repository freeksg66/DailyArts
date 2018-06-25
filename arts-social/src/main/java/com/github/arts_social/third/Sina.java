package com.github.arts_social.third;

import android.app.Activity;
import android.text.TextUtils;

import com.github.arts_social.Auth;
import com.github.arts_social.IShare;
import com.github.arts_social.LoginResult;
import com.github.arts_social.ShareActionListener;
import com.github.arts_social.ShareContent;
import com.github.arts_social.ShareUtils;
import com.github.arts_social.SocialType;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class Sina extends Auth implements IShare {
    private Activity mActivity;

    public static Sina getInstance(Activity activity) {
        return new Sina(activity);
    }

    private Sina(Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    protected String getPlatformName() {
        return SinaWeibo.NAME;
    }

    @Override
    protected boolean checkExist(boolean isLogout) {
        return false;
    }

    public void login(final TokenCallback callback) {
        mPlatform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                PlatformDb platformDb = platform.getDb();
                LoginResult result = new LoginResult();
                result.token = platformDb.getToken();
                callback.onSuccess(result);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                callback.onFailure();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                callback.onFailure();
            }
        });
        mPlatform.authorize();
    }

    @Override
    public void share(SocialType type, ShareContent content) {
        SinaWeibo.ShareParams params = new SinaWeibo.ShareParams();
        params.setTitle(content.title);
        params.setImageUrl(ShareUtils.checkUrl(content.cover));
        if (TextUtils.isEmpty(content.description)) {
            params.setText(content.title + " " + content.url);
        }else {
            params.setText(content.description);
        }
        mPlatform.share(params);
        mPlatform.setPlatformActionListener(new ShareActionListener(mActivity));
    }
}
