package com.tufusi.slideapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鼠夏目 on 2020/7/25.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public abstract class UniversalAdapter<T> extends RecyclerView.Adapter<KKViewHolder> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    private LayoutInflater mInflater;
    private ViewGroup mRootView;

    public UniversalAdapter(Context context, List<T> datas, int layoutId) {
        mContext = context;
        mDatas = datas;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public KKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        KKViewHolder viewHolder = KKViewHolder.get(mContext, null, parent, mLayoutId);
        if (this.mRootView == null) {
            mRootView = parent;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KKViewHolder holder, int position) {
        this.setListener(position, holder);
        this.convert(holder, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 设置数据源
     */
    public void setDatas(List<T> list) {
        if (mDatas != null) {
            if (list != null && list.size() > 0) {
                mDatas.clear();
                mDatas.addAll(list);
            } else {
                mDatas.clear();
            }
        } else {
            mDatas = list;
        }
        notifyDataSetChanged();
    }

    /**
     * 添加数据源
     */
    public void addDatas(List<T> list) {
        if (null != list) {
            if (mDatas != null) {
                mDatas.addAll(list);
            } else {
                mDatas = list;
            }
            notifyDataSetChanged();
        }
    }

    /**
     * 获取数据源
     */
    public List<T> getDatas() {
        return mDatas;
    }

    /**
     * 删除指定位置数据
     */
    public void remove(int i) {
        if (null != mDatas && mDatas.size() > i && i > -1) {
            mDatas.remove(i);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取指定位置数据集
     */
    public T getItem(int position) {
        return position > -1 && null != mDatas && mDatas.size() > position ? mDatas.get(position) : null;
    }

    /**
     * 往ViewHolder中填充数据
     */
    protected abstract void convert(KKViewHolder holder, T data);

    protected void setListener(final int position, final KKViewHolder holder) {
        if (isEnabled(getItemViewType(position))) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(mRootView, v, mDatas.get(position), position);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getViewPosition(holder);
                        return onItemClickListener.onItemLongClick(mRootView, v, mDatas.get(position), position);
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    protected int getViewPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition();
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    private OnItemClickListener onItemClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
