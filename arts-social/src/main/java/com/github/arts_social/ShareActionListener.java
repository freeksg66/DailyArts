package com.github.arts_social;

import android.app.Activity;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class ShareActionListener implements PlatformActionListener {

    private Activity mActivity;

    public ShareActionListener(Activity activity){
        mActivity = activity;
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Toast.makeText(mActivity,mActivity.getText(R.string.toast_share_success).toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(mActivity,mActivity.getText(R.string.toast_share_fail).toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {

    }
}
