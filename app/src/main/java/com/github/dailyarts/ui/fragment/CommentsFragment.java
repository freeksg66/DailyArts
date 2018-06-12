package com.github.dailyarts.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dailyarts.R;
import com.github.dailyarts.ui.widget.AppActionBar;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class CommentsFragment extends BaseFragment {

    private AppActionBar appActionBar;
    private EditText etCommentMessage;
    private TextView tvCommentSubmit;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_comments;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
        etCommentMessage = rootView.findViewById(R.id.et_comments_message);
        tvCommentSubmit = rootView.findViewById(R.id.tv_comments_publish);

        tvCommentSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment();
            }
        });

        appActionBar.setTitle("Comments");
    }

    private void submitComment(){
        //
    }
}
