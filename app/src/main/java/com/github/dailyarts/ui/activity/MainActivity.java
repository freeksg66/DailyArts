package com.github.dailyarts.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.fragment.MainFragment;
import com.github.dailyarts.utils.StatusBarUtils;
import com.github.dailyarts.utils.ToastUtils;

@Route(path = RouterConstant.MainActivityConst.PATH)
public class MainActivity extends BaseActivity {

    private MainFragment mainFragment;
    // 第一次按下返回键的事件
    private long firstPressedTime;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_main_activity_content;
    }

    @Override
    protected void initConfigBeforeSetContentView() {
        super.initConfigBeforeSetContentView();
        StatusBarUtils.setStatusBarDark(this, false);
        StatusBarUtils.changeStatusBarColor(this, Color.BLACK);
    }

    @Override
    protected void onInitView() {
        RouterManager.getInstance().inject(this);
        mainFragment = getStoredFragment(MainFragment.class);
        addFragment(mainFragment);
    }

    @Override
    public void onBackPressed() {
        if(mainFragment.rightFragmentClose()) return;
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            super.onBackPressed();
        } else {
            ToastUtils.show(MainActivity.this, "再按一次退出");
            firstPressedTime = System.currentTimeMillis();
        }
    }
}
