package com.github.dailyarts.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dailyarts.R;
import com.github.dailyarts.entity.ImageModel;
import com.github.dailyarts.utils.ImageLoadUtils;

import java.util.List;

/**
 * Created by legao005426 on 2018/6/13.
 */

public class FindArtsAdapter extends RecyclerView.Adapter<FindArtsAdapter.FindArtsViewHolder> {
    private Context mContext;
    private List<ImageModel> dataList;
    private OnItemClickListener mOnItemClickListener;

    public FindArtsAdapter(Context context, List<ImageModel> list) {
        super();
        mContext = context;
        dataList = list;
    }

    @Override
    public FindArtsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_arts, parent, false);
        FindArtsViewHolder viewHolder = new FindArtsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FindArtsViewHolder holder, int position) {
        ImageModel model = dataList.get(position);
        ImageLoadUtils.load(mContext, R.drawable.image_placeholder, model.getImage(), holder.ivImage);
        holder.tvAuthor.setText(model.getAuthor());
        holder.tvName.setText(model.getName());
        holder.rootView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList == null || dataList.size() <= 0) ? 0 : dataList.size();
    }

    public void setData(List<ImageModel> list) {
        if (list == null || list.size() < 0) return;
        dataList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(ImageModel model);
    }

    class FindArtsViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView ivImage;
        TextView tvName, tvAuthor;

        FindArtsViewHolder(View v) {
            super(v);
            rootView = v;
            ivImage = v.findViewById(R.id.iv_find_image);
            tvName = v.findViewById(R.id.tv_find_image_name);
            tvAuthor = v.findViewById(R.id.tv_find_image_author);
        }
    }
}
