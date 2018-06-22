package com.github.dailyarts.ui.widget.GalleryViewPager;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by legao005426 on 2018/5/7.
 * LoopViewPager的改进
 */

public class GalleryViewPager extends ViewPager {
    public static final int RESUME = 0;
    public static final int PAUSE = 1;
    public static final int DESTROY = 2;

    private int offsetDate = 0;

    @IntDef({RESUME, PAUSE, DESTROY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LifeCycle {
    }

    private int mLifeCycle = RESUME;

    private GalleryPagerAdapterWrapper mAdapter;
    private boolean mIsTouching = false; //when touching, stop the auto scroll
    private ScheduledExecutorService mCarouselTimer; //using this instead of Timer

    private List<OnPageChangeListener> mOuterOnPageChangeListeners;


    public GalleryViewPager(Context context) {
        super(context);
        init();
    }

    public GalleryViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        super.addOnPageChangeListener(mDefaultPageChangeListener);
    }

    public void setLifeCycle(@LifeCycle int mLifeCycle) {
        this.mLifeCycle = mLifeCycle;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new GalleryPagerAdapterWrapper(adapter);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        throw new UnsupportedOperationException("Not support any more by LoopViewPager");
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mOuterOnPageChangeListeners == null)
            mOuterOnPageChangeListeners = new ArrayList<>();
        mOuterOnPageChangeListeners.add(listener);
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mOuterOnPageChangeListeners != null) {
            mOuterOnPageChangeListeners.remove(listener);
        }
    }

    @Override
    public void clearOnPageChangeListeners() {
        if (mOuterOnPageChangeListeners != null) {
            mOuterOnPageChangeListeners.clear();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIsTouching = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsTouching = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //startScroll();
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        shutdownTimer();
    }

    public void startScroll() {
        shutdownTimer();
        mCarouselTimer = Executors.newSingleThreadScheduledExecutor();
        mCarouselTimer.scheduleAtFixedRate(() -> {
            switch (mLifeCycle) {
                case RESUME:
                    if (!mIsTouching && getAdapter() != null && getAdapter().getCount() > 1) {
                        post(() -> setCurrentItem(getCurrentItem() + 1));
                    }
                    break;
                case PAUSE:
                    break;
                case DESTROY:
                    shutdownTimer();
                    break;
            }
        }, 1000 * 4, 1000 * 4, TimeUnit.MILLISECONDS);
    }

    private void shutdownTimer() {
        if (mCarouselTimer != null && mCarouselTimer.isShutdown() == false) {
            mCarouselTimer.shutdown();
        }
        mCarouselTimer = null;
    }


    /**
     * proxy any OnPageChangeListener
     */
    private OnPageChangeListener mDefaultPageChangeListener = new OnPageChangeListener() {

        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;


        @Override
        public void onPageSelected(int position) {
            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;

                if (mOuterOnPageChangeListeners != null) {
                    for (int i = 0, z = mOuterOnPageChangeListeners.size(); i < z; i++) {
                        OnPageChangeListener listener = mOuterOnPageChangeListeners.get(i);
                        listener.onPageSelected(realPosition);
                    }
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0
                        && mPreviousOffset == 0
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            mPreviousOffset = positionOffset;

            if (mOuterOnPageChangeListeners != null) {
                float realPositionOffset = positionOffset;
                int realPositionOffsetPixels = positionOffsetPixels;
                if (realPosition == mAdapter.getRealCount() - 1) {
                    if (positionOffset > .5) {
                        realPosition = 0;
                        realPositionOffset = 0;
                        realPositionOffsetPixels = 0;
                    } else {
                        realPositionOffset = 0;
                        realPositionOffsetPixels = 0;
                    }
                }

                for (int i = 0, z = mOuterOnPageChangeListeners.size(); i < z; i++) {
                    OnPageChangeListener listener = mOuterOnPageChangeListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrolled(realPosition, realPositionOffset, realPositionOffsetPixels);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = GalleryViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }

            if (mOuterOnPageChangeListeners != null) {
                for (int i = 0, z = mOuterOnPageChangeListeners.size(); i < z; i++) {
                    OnPageChangeListener listener = mOuterOnPageChangeListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }
    };
}
