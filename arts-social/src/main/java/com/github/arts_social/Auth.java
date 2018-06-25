package com.github.arts_social;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by legao005426 on 2018/6/25.
 */

public abstract class Auth {
    protected Platform mPlatform;

    public interface TokenCallback{
        void onSuccess(LoginResult result);

        void onFailure();
    }

    public Auth(){
        mPlatform = ShareSDK.getPlatform(getPlatformName());
    }

    protected abstract String getPlatformName();
    protected abstract boolean checkExist(boolean isLogout);

    public abstract void login(TokenCallback callback);

    public void logout(){
        if (checkExist(true)) return;
        if (mPlatform.isAuthValid()){
            mPlatform.removeAccount(true);
        }
    }
}
