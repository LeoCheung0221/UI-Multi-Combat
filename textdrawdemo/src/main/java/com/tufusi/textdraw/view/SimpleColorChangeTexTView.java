package com.tufusi.textdraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by 鼠夏目 on 2020/7/15.
 *
 * @See
 * @Description 继承TextView  文字颜色渐变
 */
public class SimpleColorChangeTexTView extends AppCompatTextView {

    private String mText = "文小胖胖";

    public SimpleColorChangeTexTView(Context context) {
        super(context);
    }

    public SimpleColorChangeTexTView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleColorChangeTexTView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setTextSize(80);
        float baseLine = 100;
        canvas.drawText(mText, 0, baseLine, paint);

        drawCenterLineX(canvas);

        float x = getWidth() / 2;

        //默认LEFT
        canvas.drawText(mText, x, baseLine, paint);

        //设置X位置的居中对齐
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mText, x, baseLine + paint.getFontSpacing(), paint);

        //设置RIGHT
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(mText, x, baseLine + paint.getFontSpacing() * 2  , paint);
    }

    public void drawCenterLineX(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(3);
        canvas.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight(), paint);
    }
}
