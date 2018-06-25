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
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;


/**
 * Created by legao005426 on 2018/6/25.
 */

public class Tencent extends Auth implements IShare {

    private Activity mActivity;

    private Tencent(Activity activity) {
        super();
        mActivity = activity;
    }

    public static Tencent getInstance(Activity activity) {
        return new Tencent(activity);
    }

    @Override
    public void share(SocialType type, ShareContent content) {
        Platform.ShareParams params;
        if (SocialType.QQ.equals(type)) {
            mPlatform = ShareSDK.getPlatform(getPlatformName());
            params = new QQ.ShareParams();
        } else {
            mPlatform = ShareSDK.getPlatform(QZone.NAME);
            params = new QZone.ShareParams();
        }
        params.setTitle(content.title);
        params.setTitleUrl(content.url);
        params.setImageUrl(ShareUtils.checkUrl(content.cover));
        if (!TextUtils.isEmpty(content.description)){
            params.setText(content.description);
        }
        mPlatform.share(params);
        mPlatform.setPlatformActionListener(new ShareActionListener(mActivity));
    }

    @Override
    protected String getPlatformName() {
        return QQ.NAME;
    }

    @Override
    protected boolean checkExist(boolean isLogout) {
        return false;
    }

    @Override
    public void login(final TokenCallback callback) {
        mPlatform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform mPlatform, int i, HashMap<String, Object> hashMap) {
                mPlatform.getDb().getToken();
                LoginResult loginResult = new LoginResult();
                loginResult.token = mPlatform.getDb().getToken();
                callback.onSuccess(loginResult);
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
}
