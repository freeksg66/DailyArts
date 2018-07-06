package com.github.dailyarts.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.dailyarts.R;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.ui.transformation.ScalePageTransformer;
import com.github.dailyarts.ui.widget.AppActionBar;
import com.github.dailyarts.utils.DeviceInfo;
import com.github.dailyarts.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class MyGalleryFragment extends BaseFragment {
    private AppActionBar appActionBar;
    private ViewPager vpCollection;

    private IdiotGalleryAdapter mAdapter;
    private List<Fragment> mFragments;
    private List<ImageModel> mDataList;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_gallery;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
        vpCollection = rootView.findViewById(R.id.vp_my_gallery);
        ViewGroup.LayoutParams params = vpCollection.getLayoutParams();
        params.width = DeviceInfo.getDisplayMetrics(getContext()).widthPixels;
        params.height = DeviceInfo.getDisplayMetrics(getContext()).heightPixels;
        vpCollection.setLayoutParams(params);
        initFragments();
        mAdapter = new IdiotGalleryAdapter(getChildFragmentManager(), mFragments);
        vpCollection.setAdapter(mAdapter);
        vpCollection.setOffscreenPageLimit(1); // 默认值
        vpCollection.setCurrentItem(mFragments.size() - 1);
        int vrPaddingLeft = vpCollection.getPaddingLeft();
        int vrPageWidth = params.width - vrPaddingLeft * 2;
        float excursion = -(float) vrPaddingLeft / (float) vrPageWidth;
        vpCollection.setPageTransformer(true, new ScalePageTransformer(excursion));
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mDataList = SharedPreferencesUtils.getCollectImages(getContext());
        if (mDataList != null && mDataList.size() > 0) {
            for (ImageModel item : mDataList) {
                GalleryItemFragment fragment = new GalleryItemFragment();
                fragment.setData(item);
                mFragments.add(fragment);
            }
        }
    }

    class IdiotGalleryAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragments;

        public IdiotGalleryAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return null == mFragments ? 0 : mFragments.size();
        }
    }
}
