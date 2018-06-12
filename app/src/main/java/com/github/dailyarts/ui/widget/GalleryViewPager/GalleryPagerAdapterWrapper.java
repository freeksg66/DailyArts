package com.github.dailyarts.ui.widget.GalleryViewPager;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.github.dailyarts.entity.DateModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by legao005426 on 2018/5/7.
 */

public class GalleryPagerAdapterWrapper extends PagerAdapter {

    private PagerAdapter mAdapter;
    private List<DateModel> mList;

    GalleryPagerAdapterWrapper(PagerAdapter adapter) {
        this.mAdapter = adapter;
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DateModel dateModel = new DateModel(calendar.get(Calendar.YEAR) - 1, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), 0);
        mList = new ArrayList<>();
        mList.add(dateModel.getTomorrow());
        mList.add(dateModel.getYesterday());
        mList.add(dateModel.getTomorrow());
        mList.add(dateModel.getYesterday());
    }

    int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        int realPosition = (position - 1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;

        return realPosition;
    }

    public int toInnerPosition(int realPosition) {
        return getRealCount() == 1 ? realPosition : (realPosition + 1);
    }

    public PagerAdapter getRealAdapter() {
        return mAdapter;
    }

    public int getRealCount() {
        return mAdapter.getCount();
    }

    @Override
    public int getCount() {
        return mAdapter.getCount() == 1 ? 1 : mAdapter.getCount() + 2;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return mAdapter.instantiateItem(container, toRealPosition(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mAdapter.destroyItem(container, toRealPosition(position), object);
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */

    @Override
    public void finishUpdate(ViewGroup container) {
        mAdapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        mAdapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }
}