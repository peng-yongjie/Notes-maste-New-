package com.soulocean.Diary.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.soulocean.Diary.R;
import com.soulocean.Diary.entity.Bean;

import java.util.List;

/**
 * @author soulo
 */
public class BeanAdapter extends RecyclerView.Adapter<BeanAdapter.ViewHolder> {
    private List<Bean> beanList;
    private OnItemClickListener mOnClickListener;
    private OnItemLongClickListener mOnLongClickListener;
    public BeanAdapter(List<Bean> beanList) {
        this.beanList = beanList;
    }

    public void setBeanList(List<Bean> beanList) {
        this.beanList = beanList;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnlongClickListener) {
        this.mOnLongClickListener = mOnlongClickListener;
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    /**
     * 初始化布局视图
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bean_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        int screenHeight = parent.getHeight();
        ViewGroup.LayoutParams linearParams = holder.beanItem_RL.getLayoutParams();
        linearParams.height = ((screenHeight - 150) / 3);
        holder.beanItem_RL.setLayoutParams(linearParams);
        view.setTag(holder);
        return new ViewHolder(view);
    }

    /**
     * 绑定视图组件数据
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText(beanList.get(position).getName());
        holder.content.setText(beanList.get(position).getContent());
        holder.remarks.setText(beanList.get(position).getRemarks());
        holder.beanItem_RL.setBackgroundColor(Color.parseColor(getBackgroundColorByCode(Integer.parseInt(beanList.get(position).getRemarks()))));

        if (mOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mOnClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }

        if (mOnLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getAdapterPosition();
                    mOnLongClickListener.onItemlongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    public String getBackgroundColorByCode(int code) {
        switch (code) {
            case 1:
                return "#CD9B7A";
            case 2:
                return "#CD3439";
            case 3:
                return "#CD6839";
            default:
        }
        return "#80cbc4";
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemlongClick(View view, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout beanItem_RL;
        private final ImageView image;
        private final TextView name;
        private final TextView content;
        private final TextView remarks;

        public ViewHolder(View itemView) {
            super(itemView);
            beanItem_RL = itemView.findViewById(R.id.BeanItem_RL);
            image = itemView.findViewById(R.id.image_bean_item);
            name = itemView.findViewById(R.id.name_bean_item);
            content = itemView.findViewById(R.id.content_bean_item);
            remarks = itemView.findViewById(R.id.remarks_bean_item);
        }
    }
}
