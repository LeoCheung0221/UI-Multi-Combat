package com.tufusi.swimmingfish.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


/**
 * Created by 鼠夏目 on 2020/7/17.
 *
 * @See
 * @Description
 */
public class FishHeaderView extends View {

    private Context mContext;
    private Path mPath;
    private Paint mPaint;

    public FishHeaderView(Context context) {
        this(context, null);
    }

    public FishHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        initAttr();
    }

    /**
     * 初始化路径 、 画笔 、
     */
    private void initAttr() {
        //路径
        mPath = new Path();
        //画笔
        mPaint = new Paint();
        //身体画笔

    }

}
