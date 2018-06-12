package com.github.dailyarts.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dailyarts.R;
import com.github.dailyarts.ui.widget.AppActionBar;

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
    }
}
