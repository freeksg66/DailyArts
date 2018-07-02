package com.github.arts_social.third;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.arts_social.R;
import com.github.arts_social.ShareContent;

/**
 * Created by legao005426 on 2018/6/25.
 */

public class ClipBoard { //implements IShare {

    private Activity mActivity;
    private ClipboardManager mManager;

    private ClipBoard(Activity activity) {
        mActivity = activity;
        mManager = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public static ClipBoard getInstance(Activity activity) {
        return new ClipBoard(activity);
    }

//    @Override
//    public void share(SocialType type, ShareContent content) {
//        if (SocialType.CLIPBOARD.equals(type) && null != content.url) {
//            StringBuffer sb = new StringBuffer();
//            sb.append(content.title).append(" ");
//            if (!TextUtils.isEmpty(content.description)){
//                sb.append(content.description).append(" ");
//            }
//            sb.append(content.url);
//            ClipData data = ClipData.newPlainText("text",sb);
//            mManager.setPrimaryClip(data);
//            Toast.makeText(mActivity, mActivity.getText(R.string.toast_share_copy_to_clipboard), Toast.LENGTH_SHORT).show();
//        }
//    }
}
