package com.tufusi.viewpager;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by 鼠夏目 on 2020/7/23.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public class PageTransform implements ViewPager.PageTransformer {

    private static final float DEFAULT_MIN_ALPHA = 0.3f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;

    private static final float DEFAULT_MAX_ROTATE = 15.0f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;

    @Override
    public void transformPage(@NonNull View view, float position) {
        if (position < -1) {
            //透明度
            view.setAlpha(mMinAlpha);
            //旋转
            view.setRotation(mMaxRotate * -1);
            view.setPivotX(view.getWidth());
            view.setPivotY(view.getHeight());
        } else if (position <= 1) {
            if (position < 0) {
                //position是0到-1的变化,p1+position就是从1到0的变化
                //(p1 - mMinAlpha) * (p1 + position)就是(p1 - mMinAlpha)到0的变化
                //再加上一个mMinAlpha，就变为1到mMinAlpha的变化。
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 + position);
                view.setAlpha(factor);

                view.setRotation(mMaxRotate * position);
                //position为width/2到width的变化
                view.setPivotX(view.getWidth() * 0.5f * (1 - position));
                view.setPivotY(view.getHeight());
            } else {
                //minAlpha到1的变化
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 - position);
                view.setAlpha(factor);

                view.setRotation(mMaxRotate * position);
                view.setPivotX(view.getWidth() * 0.5f * (1 - position));
                view.setPivotY(view.getHeight());
            }
        } else {
            view.setAlpha(mMinAlpha);

            view.setRotation(mMaxRotate);
            view.setPivotX(0);
            view.setPivotY(view.getHeight());
        }
    }
}
