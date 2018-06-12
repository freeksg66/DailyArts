package com.github.dailyarts.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.widget.AppActionBar;

/**
 * Created by legao005426 on 2018/5/7.
 */

@Route(path = RouterConstant.AboutActivityConst.PATH)
public class AboutActivity extends BaseActivity {
    private static String TAG = "AboutActivity";

    private AppActionBar appActionBar;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_about;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onInitView() {
        RouterManager.getInstance().inject(this);
        appActionBar = findViewById(R.id.mine_toolbar);
    }
}
