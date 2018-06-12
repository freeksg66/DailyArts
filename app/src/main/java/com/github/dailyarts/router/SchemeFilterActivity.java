package com.github.dailyarts.router;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.github.dailyarts.utils.CommonUtils;

/**
 * 外部跳转入口
 * Created by legao005426 on 2018/6/11.
 */

public class SchemeFilterActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //外部跳转，若应用未启动，则启动首页再跳转
        if (!CommonUtils.isApplicationShowing(getPackageName(), getApplicationContext())) {
            RouterManager.getInstance().startActivity(RouterConstant.MAIN);
        }

        Uri uri = getIntent().getData();
        RouterManager.getInstance().build(uri)
                .addFlag(getIntent().getFlags())
                .navigation(this, new NavCallback() {
                    @Override
                    public void onArrival(Postcard postcard) {
                    }
                });
        finish();
    }
}
