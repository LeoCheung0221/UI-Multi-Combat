package com.tufusi.slideapp;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by 鼠夏目 on 2020/7/25.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public class SlideCardLayoutManager extends RecyclerView.LayoutManager {

    /**
     * 让ItemView获得决定权
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 实现回收复用 这里需要对源码做进一步了解
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        // ViewHolder的回收复用
        // 先把所有的View先从RecyclerView中detach掉，然后标记为"Scrap"状态，表示这些View处于可被重用状态(非显示中)。
        // 实际就是把View放到了Recycler中的一个集合中。
        detachAndScrapAttachedViews(recycler);

        // tip: 记住这里的位置重叠是依次往上重叠，在用户看来是依次增大，最上面是最大位置
        // 因此下面的设置偏移缩放都是针对最上面的位置固定不变来操作的，比如8/8就是居中的卡片，下面的依次偏移从而展示出重叠感
        int bottomPosition = 0;
        // 先判断itemCount是否超过显示数量 如果没超过，超出部分的位置索引为0 否则，超出部分的位置索引先找出来
        int itemCount = getItemCount();
        if (itemCount > CardConfig.MAX_SHOW_COUNT) {
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }

        // 将bottomPosition位置以上的卡片设置偏移量
        // 比如卡片有8张，设置的显示最大数量为4 则 重叠的上面四张有偏移，bottomPosition = 4 那么接下来就是布局 4 5 6 7 位置的卡片
        for (int i = bottomPosition; i < itemCount; i++) {
            // 找出复用的View
            View view = recycler.getViewForPosition(i);
            // 将其填充进RecyclerView
            addView(view);

            // 测量ChildView
            measureChildWithMargins(view, 0, 0);
            //获得上下、左右边距范围
            int widthSpace = getWidth() - getDecoratedMeasuredWidth(view);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);

            //布局 draw  这里其实就是自适应布局（WRAP_CONTENT）之后，让其居中展示
            layoutDecoratedWithMargins(view, widthSpace / 2, heightSpace / 2,
                    widthSpace / 2 + getDecoratedMeasuredWidth(view),
                    heightSpace / 2 + getDecoratedMeasuredHeight(view));

            // 处理 3 2 1  相对位置索引的卡片（这里不是列表里的position，而是相对8/8位置的 1 2 3）
            int offSet = itemCount - i - 1;
            if (offSet > 0) {
                // 2 1 的位置卡片处理设置偏移缩放
                if (offSet < CardConfig.MAX_SHOW_COUNT - 1) {
                    view.setTranslationY(CardConfig.TRANS_Y_GAP * offSet);
                    view.setScaleX(1 - CardConfig.SCALE_GAP * offSet);
                    view.setScaleY(1 - CardConfig.SCALE_GAP * offSet);
                } else {
                    //最后一张保持和下面的布局同样  3位置卡片处理
                    view.setTranslationY(CardConfig.TRANS_Y_GAP * (offSet - 1));
                    view.setScaleX(1 - CardConfig.SCALE_GAP * (offSet - 1));
                    view.setScaleY(1 - CardConfig.SCALE_GAP * (offSet - 1));
                }
            }
        }
    }
}
