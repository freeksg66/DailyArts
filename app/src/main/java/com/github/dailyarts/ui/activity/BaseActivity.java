package com.github.dailyarts.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.github.dailyarts.BaseApplication;
import com.github.dailyarts.event.NetConnectionChangeEvent;
import com.github.dailyarts.repository.RxLifecycleBinder;
import com.github.dailyarts.ui.fragment.BaseFragment;
import com.github.dailyarts.ui.widget.LoadingDialog;
import com.github.dailyarts.utils.StatusBarUtils;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by legao005426 on 2018/6/11.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements RxLifecycleBinder {

    public BaseApplication autoApp;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfigBeforeSetContentView();
        if (getLayoutResource() != 0)
            setContentView(getLayoutResource());
        onInitData();
        onInitView();
    }

    protected abstract int getLayoutResource();

    protected abstract int getFragmentContentId();

    protected abstract void onInitView();

    @Override
    public <T> LifecycleTransformer<T> bindLifecycle() {
        return bindToLifecycle();
    }

    /**
     * 该方法在初始化各个控件之前调用
     */
    protected void onInitData() {
        autoApp = (BaseApplication) getApplicationContext();
    }

    /**
     * 设置布局之前调用该方法，如果具体Activity需要在setContentView之前配置，实现该方法
     */
    protected void initConfigBeforeSetContentView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StatusBarUtils.setImmerseStatusBar(this);
        StatusBarUtils.setStatusBarDark(this, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadingDialog != null)
            mLoadingDialog.dismiss();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    public void addFragment(BaseFragment fragment) {
        if (null != fragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    public void replaceFragment(BaseFragment fragment) {
        if (null != fragment) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    public <T extends BaseFragment> T getStoredFragment(Class clazz) {
        return getStoredFragment(clazz, null);
    }

    public <T extends BaseFragment> T getStoredFragment(Class clazz, Bundle arguments) {
        T fragment = (T) getSupportFragmentManager().findFragmentByTag(clazz.getName());
        if (null == fragment) {
            try {
                fragment = (T) Class.forName(clazz.getName()).newInstance();
                fragment.setArguments(arguments);
            } catch (Exception e) {
                throw new RuntimeException("Cannot create " + clazz.getName());
            }
        }
        return fragment;
    }

    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    //add by lw 2016.09.23
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
        }
    }

    //显示加载对话框
    public void startLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    //隐藏加载对话框
    public void stopLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
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
