package com.github.dailyarts.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.dailyarts.event.NetConnectionChangeEvent;
import com.github.dailyarts.ui.activity.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by legao005426 on 2018/6/11.
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;
    protected BaseActivity mActivity;


    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }


    /**
     * 执行父activity的addfragment  非本fragment的childfragment
     *
     * @param fragment
     */
    protected void addFragment2ParentActivity(BaseFragment fragment) {
        if (null != fragment) {
            getHoldingActivity().addFragment(fragment);
        }
    }

    protected void replaceFragment(BaseFragment fragment) {
        if (null != fragment) {
            getHoldingActivity().replaceFragment(fragment);
        }
    }

    protected void removeFragment() {
        getHoldingActivity().removeFragment();
    }

    public View findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResource(), container, false);
            onInitView();
        }

        ViewGroup parentView = (ViewGroup) rootView.getParent();
        if (parentView != null)
            parentView.removeView(rootView);

        return rootView;
    }

    protected abstract int getLayoutResource();

    protected abstract void onInitView();

    public String getName() {
        return BaseFragment.class.getName();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    public void startLoading() {
        mActivity.startLoading();
    }

    public void stopLoading() {
        mActivity.stopLoading();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 网络连接状态变化时调用
     *
     * @param event 包含网络连接状态和当前网络类型
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChange(NetConnectionChangeEvent event) {
    }
}
