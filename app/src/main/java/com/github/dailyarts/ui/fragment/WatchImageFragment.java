package com.github.dailyarts.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.ui.widget.AppActionBar;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class WatchImageFragment extends BaseFragment {
    private TextView tvDownload;
    private AppActionBar appActionBar;
    private SubsamplingScaleImageView ssivImage;

    @Autowired(name = RouterConstant.WatchImageActivityConst.BIG_IMAGE_URL)
    String bigUrl;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_watch_image;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
        appActionBar.showDownload();
    }
}
