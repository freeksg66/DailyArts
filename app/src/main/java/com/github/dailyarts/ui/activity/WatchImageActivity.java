package com.github.dailyarts.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.fragment.WatchImageFragment;

/**
 * Created by legao005426 on 2018/6/11.
 */

@Route(path = RouterConstant.WatchImageActivityConst.PATH)
public class WatchImageActivity extends BaseActivity {
    private WatchImageFragment mWatchImageFragment;

    @Autowired
    String bigImageUrl;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_watch_image;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_watch_image_activity_content;
    }

    @Override
    protected void onInitView() {
        RouterManager.getInstance().inject(this);
        Bundle bundle = new Bundle();
        bundle.putString("BigImageUrl", bigImageUrl);
        mWatchImageFragment = getStoredFragment(WatchImageFragment.class, bundle);
        addFragment(mWatchImageFragment);
    }
}
