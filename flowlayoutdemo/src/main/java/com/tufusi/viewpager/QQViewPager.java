package com.tufusi.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by 鼠夏目 on 2020/7/23.
 *
 * @author 鼠夏目
 * @description 自定义ViewPager - 自适应高度
 * @see
 */
public class QQViewPager extends ViewPager {

    private static final String TAG = "QQViewPager";

    public QQViewPager(@NonNull Context context) {
        super(context);
    }

    public QQViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            //获取子View的父级布局参数
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            //获取子View的测量模式    测量模式要么是父级规定好的，要么就是自定义
            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);
            //设置子View的测量
            child.measure(childWidthSpec, childHeightSpec);

            //获取子View的测量后的高度
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
            Log.d(TAG, "onMeasure: " + h + " height: " + height);
        }
        //获取高度的测量模式
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        //设置测量值
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            ViewGroup.LayoutParams lp =  child.getLayoutParams();
//            int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,0,lp.width);
//            int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,0,lp.height);
//            child.measure(childWidthSpec,childHeightSpec);
//        }
//
//        int height = 0;
//        switch (heightMode) {
//            case MeasureSpec.EXACTLY:
//                height = heightSize;
//                break;
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.UNSPECIFIED:
//                for (int i = 0; i < getChildCount(); i++) {
//                    View child = getChildAt(i);
//                    height = Math.max(height,child.getMeasuredHeight());
//                }
//                break;
//            default:
//                break;
//        }
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
