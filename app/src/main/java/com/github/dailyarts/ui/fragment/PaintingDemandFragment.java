package com.github.dailyarts.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.arts_email.SendMailUtil;
import com.github.dailyarts.R;
import com.github.dailyarts.ui.widget.AppActionBar;
import com.github.dailyarts.utils.ToastUtils;

/**
 * Created by legao005426 on 2018/6/11.
 */

public class PaintingDemandFragment extends BaseFragment {
    private AppActionBar appActionBar;
    private TextView tvDemandSubmit;
    private EditText etDemandMessage, etDemandContractInfo;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_painting_demand;
    }

    @Override
    protected void onInitView() {
        appActionBar = rootView.findViewById(R.id.mine_toolbar);
        tvDemandSubmit = appActionBar.findViewById(R.id.tv_submit);
        etDemandMessage = rootView.findViewById(R.id.et_demand_message);
        etDemandContractInfo = rootView.findViewById(R.id.et_demand_contract_info);

        appActionBar.showSubmit();

        tvDemandSubmit.setOnClickListener(v -> sendEamil());
    }

    private void sendEamil(){
        if(etDemandContractInfo.getText() == null || etDemandMessage.getText().toString().equals("")){
            ToastUtils.show(getContext(), "请输入您的联系方式");
            return;
        }
        if(etDemandMessage.getText() == null || etDemandMessage.getText().toString().equals("")){
            ToastUtils.show(getContext(), "请输入画家或名画名称");
            return;
        }
        startLoading();
        String content = "内容：" +etDemandMessage.getText()+"\n联系方式："+ etDemandContractInfo.getText();
        boolean sendState = SendMailUtil.send_qqmail("le_gao@163.com", "DailyArts每日名画反馈", content);
        if(sendState){
            stopLoading();
            ToastUtils.show(getContext(), "提交成功！");
            getActivity().finish();
        }else {
            ToastUtils.show(getContext(), "提交失败，请重试");
        }
    }
}
