package com.github.dailyarts.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.dailyarts.R;

/**
 * Created by legao215985 on 2018/7/18.
 */

public class CommonDialog extends DialogFragment {
    private Context mContext;
    private TextView tvTopBtn, tvBottomBtn;
    private String upText;
    private String downText;
    private ButtonClickListener mButtonClickListener;

    public static CommonDialog getInstance(Context context, String upText, String downText, ButtonClickListener listener) {
        CommonDialog dialog = new CommonDialog();
        dialog.mContext = context;
        dialog.upText = upText;
        dialog.downText = downText;
        dialog.mButtonClickListener = listener;
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_common, container, false);
        tvTopBtn = view.findViewById(R.id.tv_common_up);
        tvBottomBtn = view.findViewById(R.id.tv_common_down);

        tvTopBtn.setText(upText);
        tvBottomBtn.setText(downText);
        if(mButtonClickListener != null) {
            tvTopBtn.setOnClickListener(v -> {
                mButtonClickListener.topButtonClick();
                dismiss();
            });
            tvBottomBtn.setOnClickListener(v -> {
                mButtonClickListener.bottomButtonClick();
                dismiss();
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        window.setBackgroundDrawable( new ColorDrawable(0x80000000));
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = R.style.shareDialogAnim;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    public interface ButtonClickListener {
        void topButtonClick();

        void bottomButtonClick();
    }

    public void show(FragmentManager manager, String tag){
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
