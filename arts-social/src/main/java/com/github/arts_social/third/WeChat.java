package com.github.arts_social.third;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.arts_social.Auth;
import com.github.arts_social.IShare;
import com.github.arts_social.LoginResult;
import com.github.arts_social.PackageUtils;
import com.github.arts_social.R;
import com.github.arts_social.ShareActionListener;
import com.github.arts_social.ShareContent;
import com.github.arts_social.ShareUtils;
import com.github.arts_social.SocialType;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class WeChat extends Auth implements IShare {
    private Activity mActivity;

    public static WeChat getInstance(Activity activity) {
        return new WeChat(activity);
    }

    private WeChat(Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    protected String getPlatformName() {
        return Wechat.NAME;
    }

    public void login(final TokenCallback callback) {
        if (checkExist(false)) return;
        mPlatform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                PlatformDb platformDb = platform.getDb();
                LoginResult loginResult = new LoginResult();
                loginResult.token = platformDb.getToken();
                loginResult.openId = platformDb.get("openid");
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

    @Override
    public boolean checkExist(boolean isLogout) {
        if (!isLogout && !PackageUtils.isApplicationExist(mActivity, PackageUtils.PACKAGE_NAME_WECHAT)) {
            Toast.makeText(mActivity, mActivity.getString(R.string.toast_wechat_not_exist), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    public void share(SocialType type, ShareContent content) {
        if (checkExist(false)) return;
        if (SocialType.WECHAT.equals(type)) {
            Platform platform = ShareSDK.getPlatform(Wechat.NAME);
            shareTo(content, platform);
        } else if (SocialType.MOMENT.equals(type)) {
            Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
            shareTo(content, platform);
        }
    }

    private void shareTo(ShareContent content, Platform platform) {
        Wechat.ShareParams params = new Wechat.ShareParams();
        params.setShareType(Platform.SHARE_WEBPAGE);
        params.setTitle(content.title);
        params.setImageUrl(ShareUtils.checkUrl(content.cover));
        params.setUrl(content.url);
        if (!TextUtils.isEmpty(content.description)){
            params.setText(content.description);
        }
        platform.share(params);
        platform.setPlatformActionListener(new ShareActionListener(mActivity));
    }
}
