package com.github.arts_social;

import android.app.Activity;

import com.github.arts_social.third.ClipBoard;
import com.github.arts_social.third.Sina;
import com.github.arts_social.third.Tencent;
import com.github.arts_social.third.WeChat;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class ThirdParty {
    private SocialType mType;
    private Activity mActivity;

    private ThirdParty(Activity activity, SocialType type) {
        mActivity = activity;
        mType = type;
    }

    public static ThirdParty with(Activity activity, SocialType type) {
        return new ThirdParty(activity, type);
    }

    public ThirdParty login(Auth.TokenCallback callback) {
        Auth auth = null;
        if (SocialType.WECHAT.equals(mType)) {
            auth = WeChat.getInstance(mActivity);
        } else if (SocialType.SINA.equals(mType)) {
            auth = Sina.getInstance(mActivity);
        } else if (SocialType.QQ.equals(mType)){
            auth = Tencent.getInstance(mActivity);
        }
        if (checkNull(auth))return this;
        if (checkNull(callback))return this;
        auth.login(callback);
        return this;
    }

    public void logout(){
        WeChat.getInstance(mActivity).logout();
        Sina.getInstance(mActivity).logout();
        Tencent.getInstance(mActivity).logout();
    }

    public ThirdParty share(ShareContent content) {
        IShare iShare = null;
        if (checkNull(content)) return this;
        if (SocialType.CLIPBOARD.equals(mType)) {
            iShare = ClipBoard.getInstance(mActivity);
        } else if (SocialType.QQ.equals(mType) || SocialType.QZONE.equals(mType)) {
            iShare = Tencent.getInstance(mActivity);
        } else if (SocialType.WECHAT.equals(mType) || SocialType.MOMENT.equals(mType)) {
            iShare = WeChat.getInstance(mActivity);
        } else if (SocialType.SINA.equals(mType)) {
            iShare = Sina.getInstance(mActivity);
        }
        if (checkNull(iShare)) return this;
        iShare.share(mType, content);
        return this;
    }

    private static boolean checkNull(Object o) {
        if (null == o)
            return true;
        return false;
    }
}
