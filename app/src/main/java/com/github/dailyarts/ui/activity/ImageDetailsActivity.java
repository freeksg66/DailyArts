package com.github.dailyarts.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;

import com.github.dailyarts.R;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.ui.fragment.ImageDetailsFragment;
import com.github.dailyarts.ui.transformation.DetailTransition;
import com.github.dailyarts.utils.SharedPreferencesUtils;

/**
 * Created by legao215985 on 2018/7/14.
 */

public class ImageDetailsActivity extends BaseActivity {
    private ImageDetailsFragment mImageDetailsFragment;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_details;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_details_activity_content;
    }

    @Override
    protected void onInitView() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        ImageModel model = intent.getParcelableExtra("BigImage");
        mImageDetailsFragment = getStoredFragment(ImageDetailsFragment.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("BigImage", model);
        bundle.putBoolean("HasCollected", SharedPreferencesUtils.checkCollect(this, model));
        mImageDetailsFragment.setArguments(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageDetailsFragment.setSharedElementEnterTransition(new DetailTransition());
            mImageDetailsFragment.setEnterTransition(new Fade().setDuration(500));
            mImageDetailsFragment.setExitTransition(new Fade().setDuration(500));
            mImageDetailsFragment.setSharedElementReturnTransition(new DetailTransition());
        }
        addFragment(mImageDetailsFragment);
    }

    @Override
    public void onBackPressed() {
        mImageDetailsFragment.backFunction();
    }
}
