package com.tufusi.slideapp;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.tufusi.slideapp.adapter.UniversalAdapter;

import java.util.List;

/**
 * Created by 鼠夏目 on 2020/7/25.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public class SlideCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerView mRv;
    private UniversalAdapter<SlideCardBean> mAdapter;
    private List<SlideCardBean> mDatas;

    /**
     * 为给定的拖拽和滑动 创建回调  ，这些值是作为默认值
     * 这里写死 是从第0个位置拖拽  第二个参数由于不限制滑动方向，因此将四个方向的二进制或上
     * UP = 1
     * DOWN = 1 << 1
     * LEFT = 1 << 2
     * RIGHT = 1 << 3
     * 1 | 10 | 100 | 1000 = 1111（8421） = 15
     */
    public SlideCallback(RecyclerView rv, UniversalAdapter<SlideCardBean> adapter, List<SlideCardBean> datas) {
        super(0, 15);
        mRv = rv;
        mAdapter = adapter;
        mDatas = datas;
    }

    /**
     * 拖拽
     *
     * @param recyclerView 依赖的容器View
     * @param viewHolder   拖拽的itemView
     * @param target       目标viewHolder
     * @return // keep target visible  如果返回true，则不会显示target view 即拖拽过后仍然展示在那边 这里要返回false
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * 滑动 滑动成功之后就将其删除并往数据源起始位置插入 实现循环列表的效果
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // 获得移除的数据 并将其添加到第0位置 实现循环往复
        SlideCardBean removeBean = mDatas.remove(viewHolder.getLayoutPosition());
        mDatas.add(0, removeBean);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 这里绘制叠加卡片样式
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        //设置滑动距离超出 recyclerView.getWidth() 宽度的一半 释放手指就滑出界面，不再恢复其原始位置
        double maxDistance = recyclerView.getWidth() * 0.5f;
        //手指操作的滑动距离
        double distance = Math.sqrt(dX * dX + dY * dY);
        //两者百分比
        double fraction = distance / maxDistance;

        if (fraction > 1) {
            fraction = 1;
        }

        //获取显示的个数
        int itemCount = recyclerView.getChildCount();

        for (int i = 0; i < itemCount; i++) {
            View view = recyclerView.getChildAt(i);

            int offset = itemCount - i - 1;
            //随着最上面的卡片滑动，其下面三个随着手势平移的距离百分比进行相应的缩放平移
            if (offset > 0 && offset < CardConfig.MAX_SHOW_COUNT - 1) {
                view.setTranslationY((float) (CardConfig.TRANS_Y_GAP * offset - fraction * CardConfig.TRANS_Y_GAP));
                view.setScaleX((float) (1 - CardConfig.SCALE_GAP * offset + fraction * CardConfig.SCALE_GAP));
                view.setScaleY((float) (1 - CardConfig.SCALE_GAP * offset + fraction * CardConfig.SCALE_GAP));
            }
        }
    }

    /**
     * 恢复位置的动画持续时间  默认是250ms
     */
    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return 300;
    }
}
