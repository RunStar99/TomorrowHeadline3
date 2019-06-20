package com.example.eight.scannews.utils;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eight.scannews.R;
import com.example.eight.scannews.beans.NewsBean;

import java.util.List;



public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<NewsBean.NewslistBean> data;
    private boolean isShowFooter = true;
    private Context context;

    private OnItemClickListener onItemClickListener;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<NewsBean.NewslistBean> data) {
        this.data = data;
        this.notifyDataSetChanged();                                                                            //通知recycleView数据已发生改变
    }

    public NewsBean.NewslistBean getItem(int position) {
        return data == null ? null : data.get(position);
    }

    public boolean isShowFooter() {
        return isShowFooter;
    }

    public void setShowFooter(boolean showFooter) {
        isShowFooter = showFooter;
    }

    @Override
    public int getItemViewType(int position) {
        if (!isShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())                        //实例化news_list_item
                    .inflate(R.layout.news_list_item, parent, false);
            //ImageView newspicture = (ImageView) view.findViewById(R.id.news_picture);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.footer_item, parent, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,                           //布局参数，实现动态布局
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            NewsBean.NewslistBean newslistBean = data.get(position);
            if (newslistBean == null) {
                return;
            }
            ((ItemViewHolder) holder).newsContent.setText(newslistBean.getTitle());
            ((ItemViewHolder) holder).newsPublic.setText(newslistBean.getDescription());
            ((ItemViewHolder) holder).newsTime.setText(newslistBean.getCtime());
            HttpUtils.showPicture(context, ((ItemViewHolder) holder).newsPicture, newslistBean.getPicUrl());
        }
    }

    @Override
    public int getItemCount() {
        int f = isShowFooter ? 1 : 0;
        if (data == null) {
            return f;
        }
        return data.size() + f;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView newsTime;
        public TextView newsPublic;
        public TextView newsContent;
        public ImageView newsPicture;

        public ItemViewHolder(View itemView) {
            super(itemView);
            newsTime =   itemView.findViewById(R.id.news_time);
            newsPublic =   itemView.findViewById(R.id.news_public);
            newsContent =   itemView.findViewById(R.id.news_content);
            newsPicture =   itemView.findViewById(R.id.news_picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, this.getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
