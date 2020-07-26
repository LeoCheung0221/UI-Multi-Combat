package com.tufusi.slideapp.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 鼠夏目 on 2020/7/25.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public interface OnItemClickListener<T> {

    /**
     * 子View点击事件
     */
    void onItemClick(ViewGroup var1, View var2, T var3, int var4);

    /**
     * 子View长按事件
     */
    boolean onItemLongClick(ViewGroup var1, View var2, T var3, int var4);

}
