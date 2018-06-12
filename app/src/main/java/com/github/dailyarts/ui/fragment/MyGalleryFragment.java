package com.github.dailyarts.ui.fragment;

import android.widget.ImageView;

import com.github.dailyarts.R;
import com.github.dailyarts.ui.widget.AppActionBar;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class MyGalleryFragment extends BaseFragment {
    private AppActionBar appActionBar;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_gallery;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
    }
}
