package com.github.dailyarts.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.dailyarts.R;
import com.github.dailyarts.router.RouterConstant;
import com.github.dailyarts.router.RouterManager;
import com.github.dailyarts.ui.fragment.CommentsFragment;

/**
 * Created by legao005426 on 2018/5/7.
 */

@Route(path = RouterConstant.CommentsActivityConst.PATH)
public class CommentsActivity extends BaseActivity {
    private CommentsFragment mCommentsFragment;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_comments;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_comments_activity_content;
    }

    @Override
    protected void onInitView() {
        RouterManager.getInstance().inject(this);
        mCommentsFragment = getStoredFragment(CommentsFragment.class);
        addFragment(mCommentsFragment);
    }
}
