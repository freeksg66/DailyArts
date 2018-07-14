package com.github.dailyarts.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.fragment.MyGalleryFragment;

/**
 * Created by legao005426 on 2018/5/7.
 */

@Route(path = RouterConstant.MyGalleryActivityConst.PATH)
public class MyGalleryActivity extends BaseActivity {
    private MyGalleryFragment mMyGalleryFragment;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_my_gallery;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_my_gallery_activity_content;
    }

    @Override
    protected void onInitView() {
        RouterManager.getInstance().inject(this);
        mMyGalleryFragment = getStoredFragment(MyGalleryFragment.class);
        addFragment(mMyGalleryFragment);
    }
}
