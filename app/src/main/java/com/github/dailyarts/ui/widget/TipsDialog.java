package com.github.dailyarts.ui.widget;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.github.dailyarts.R;

/**
 * Created by legao005426 on 2018/6/20.
 */

public class TipsDialog extends DialogFragment {
    private TextView tvLeft, tvRight, tvTitle, tvContent;
    private ButtonClickListener mListener;
    private String mTitle, mContent, mLeftBtnTxt, mRightBtnTxt;

    public static TipsDialog getInstance(String title, String content, String leftBtnTxt, String rightBtnTxt, ButtonClickListener listener) {
        TipsDialog dialog = new TipsDialog();
        dialog.mTitle = title;
        dialog.mContent = content;
        dialog.mLeftBtnTxt = leftBtnTxt;
        dialog.mRightBtnTxt = rightBtnTxt;
        dialog.mListener = listener;
        dialog.initData();
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_tips, container, false);
        tvLeft = view.findViewById(R.id.tv_cancel);
        tvRight = view.findViewById(R.id.tv_ok);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        tvLeft.setText(mLeftBtnTxt);
        tvRight.setText(mRightBtnTxt);
        tvTitle.setText(mTitle);
        tvContent.setText(mContent);
        if (mListener != null) {
            tvLeft.setOnClickListener(v -> {
                mListener.leftButtonClick();
                dismiss();
            });
            tvRight.setOnClickListener(v -> {
                mListener.rightButtonClick();
                dismiss();
            });
        } else {
            tvLeft.setOnClickListener(v -> dismiss());
            tvRight.setOnClickListener(v -> dismiss());
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        window.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    private void initData() {
        mTitle = mTitle == null ? "提示" : mTitle;
        mContent = mContent == null ? "" : mContent;
        mLeftBtnTxt = mLeftBtnTxt == null ? "取消" : mLeftBtnTxt;
        mRightBtnTxt = mRightBtnTxt == null ? "确定" : mRightBtnTxt;
    }

    public interface ButtonClickListener {
        void leftButtonClick();

        void rightButtonClick();
    }

    public void show(FragmentManager manager, String tag){
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
