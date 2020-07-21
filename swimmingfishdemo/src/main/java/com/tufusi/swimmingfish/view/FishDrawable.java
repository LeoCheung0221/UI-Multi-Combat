package com.tufusi.swimmingfish.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by 鼠夏目 on 2020/7/17.
 *
 * @See
 * @Description
 */
public class FishDrawable extends Drawable {

    private Path mPath;
    private Paint mPaint;

    /**
     * 除身体之外的其他部位的透明度
     */
    private int OTHER_ALPHA = 110;

    /**
     * 身体部位的透明度值
     */
    private int BODY_ALPHA = 160;

    //~~~~~~~~~~~~~~~~~~~~~~~~~ 鱼相关属性 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 鱼的重心也是中心 一切绘制点的参照点对象，比如知道鱼头的圆心到重心的距离，就能知道鱼头圆心的坐标
     */
    private PointF middlePoint;

    /**
     * 鱼的初始朝向角度(默认鱼头朝向右边)
     */
    private float fishMainAngle = 90;

    /**
     * 鱼头的半径 后面几乎所有长度均基于鱼头半径来确定，做到比例协调
     */
    private float HEAD_RADIUS = 50;

    /**
     * 鱼身长度
     */
    private float BODY_LENGTH = HEAD_RADIUS * 3.2f;

    /**
     * 设置鱼鳍起始点坐标到重心的线长
     */
    private float FIND_FINS_LENGTH = 0.9f * HEAD_RADIUS;

    /**
     * 设置鱼鳍的长度
     */
    private float FINS_LENGTH = 1.3f * HEAD_RADIUS;

    /**
     * 尾鳍大圆半径
     */
    private float BIG_CIRCLE_RADIUS = 0.7f * HEAD_RADIUS;

    /**
     * 尾鳍中圆半径
     */
    private float MIDDLE_CIRCLE_RADIUS = 0.6f * BIG_CIRCLE_RADIUS;

    /**
     * 尾鳍小圆半径
     */
    private float SMALL_CIRCLE_RADIUS = 0.4f * MIDDLE_CIRCLE_RADIUS;

    /**
     * 寻找尾鳍中圆线长
     */
    private float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6F + 1);

    /**
     * 寻找尾鳍小圆线长（记住：线长都是相对于鱼重心的距离）
     */
    private float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);

    /**
     * 寻找大三角形底边中心的线长距离 (同上解释)
     */
    private float FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7F;

    //~~~~~~~~~~~~~~~~~~~~~~~~~ 鱼相关属性 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 当前摆动值
     */
    float currentValue = 0f;

    /**
     * 鱼头圆心坐标
     */
    private PointF headPoint;

    /**
     * 鱼摆动频率比例
     */
    private float frequency;

    public FishDrawable() {
        init();
    }

    private void init() {
        //路径 注意每次绘制线都必须reset一次，防止重复绘制已绘制过的线
        mPath = new Path();

        //设置画笔
        mPaint = new Paint();
        //设置画笔样式-全填充
        mPaint.setStyle(Paint.Style.FILL);
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置防抖动
        mPaint.setDither(true);
        //设置画笔颜色ARGB值
        mPaint.setARGB(OTHER_ALPHA, 244, 92, 71);

        //首先确定鱼的重心点坐标 把这条鱼圈在设定打这个方形区域
        middlePoint = new PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS);

        //属性动画实现鱼摆动动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 3600f);
        valueAnimator.setDuration(8000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (float) animation.getAnimatedValue();

                invalidateSelf();
            }
        });
        valueAnimator.start();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        //设置鱼的朝向角度 同时设置鱼头的周期性摆动角度 限制左右10°摆幅
        //此处添加1.2是多次调整后取与1.5 的360°公倍数，和上面ValueAnimator.ofFloat(0, 3600f) 达成一致性，使得鱼摆动更加自然，系数可微调，但防止卡顿，最好设置公倍数
        float fishAngle = (float) (fishMainAngle + Math.sin(Math.toRadians(currentValue * 1.2)) * 10);

        //第一步：鱼头的圆心坐标
        headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);

        //第二步：画右鱼鳍（初始鱼头朝向是水平向右）
        //先找到右鱼鳍的绘制起点坐标，再通过贝塞尔曲线绘制鱼鳍轮廓
        PointF rightFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle - 110);
        makeFins(canvas, rightFinsPoint, fishAngle, true);

        //第三步：画左鱼鳍（贝塞尔曲线绘制）
        //再找到左鱼鳍的绘制起点坐标，再通过贝塞尔曲线绘制鱼鳍轮廓
        PointF leftFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle + 110);
        makeFins(canvas, leftFinsPoint, fishAngle, false);

        //第四步：画鱼身下肢上半截（大半圆）
        //首先计算body下肢大圆圆心坐标
        PointF bodyBigBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle - 180);
        PointF bodyMiddleBottomCenterPoint = makeSegment(canvas, bodyBigBottomCenterPoint, BIG_CIRCLE_RADIUS,
                MIDDLE_CIRCLE_RADIUS, FIND_MIDDLE_CIRCLE_LENGTH, fishAngle, true);

        //第五步：画鱼身下肢下半截（小半圆）
        makeSegment(canvas, bodyMiddleBottomCenterPoint, MIDDLE_CIRCLE_RADIUS,
                SMALL_CIRCLE_RADIUS, FIND_SMALL_CIRCLE_LENGTH, fishAngle, false);

        //第六步：画鱼尾（双三角形重叠）三角形的边长等于尾鳍大圆的直径 || 三角形尖角方向也与鱼朝向一致
        makeTriangle(canvas, bodyMiddleBottomCenterPoint, FIND_TRIANGLE_LENGTH, BIG_CIRCLE_RADIUS, fishAngle);
        makeTriangle(canvas, bodyMiddleBottomCenterPoint, FIND_TRIANGLE_LENGTH - 10, BIG_CIRCLE_RADIUS - 20, fishAngle);

        //最后一步：画鱼身
        makeBody(canvas, headPoint, bodyBigBottomCenterPoint, fishAngle);
    }

    private void makeBody(Canvas canvas, PointF headPoint, PointF bodyBigBottomCenterPoint, float fishAngle) {
        //身体四个点坐标
        PointF topLeftPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle + 90);
        PointF topRightPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle - 90);
        PointF bottomLeftPoint = calculatePoint(bodyBigBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle + 90);
        PointF bottomRightPoint = calculatePoint(bodyBigBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle - 90);

        //二阶贝塞尔曲线控制点 --决定鱼肚肚大小
        PointF controlLeft = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF controlRight = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130);

        //连线绘制
        mPath.reset();
        mPath.moveTo(topLeftPoint.x, topLeftPoint.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, bottomLeftPoint.x, bottomLeftPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.quadTo(controlRight.x, controlRight.y, topRightPoint.x, topRightPoint.y);

        mPaint.setAlpha(BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);
    }

    private void makeTriangle(Canvas canvas, PointF startPoint,
                              float findCenterLength, float findEdgeLength, float fishAngle) {
        //尾鳍上的三角摆动 要与下节肢小圆同频， 因此保持参数一致即可
        float triangleAngle = (float) (fishAngle + Math.sin(Math.toRadians(currentValue * 1.5)) * 35);

        //三角形底边的中心坐标
        PointF centerPoint = calculatePoint(startPoint, findCenterLength, triangleAngle - 180);

        //三角形底边两端坐标
        PointF leftPoint = calculatePoint(centerPoint, findEdgeLength, triangleAngle + 90);
        PointF rightPoint = calculatePoint(centerPoint, findEdgeLength, triangleAngle - 90);

        mPath.reset();
        mPath.moveTo(startPoint.x, startPoint.y);
        mPath.lineTo(leftPoint.x, leftPoint.y);
        mPath.lineTo(rightPoint.x, rightPoint.y);

        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 绘制尾鳍部分 包括两个大小不一的圆 以及 连起梯形
     *
     * @param canvas                画布
     * @param bottomCenterPoint     尾鳍大圆圆心坐标
     * @param bigRadius             大圆 半径（大小均是相对的 因为下半部分的小圆就是small 中圆就是big）
     * @param smallRadius           小圆 半径
     * @param findSmallCircleLength 鱼重心到小圆的线长距离
     * @param fishAngle             鱼朝向
     * @param isBigCircle           是否是尾鳍大圆绘制
     * @return 这个返回是为了尾鳍下肢下半截所用
     */
    private PointF makeSegment(Canvas canvas, PointF bottomCenterPoint, float bigRadius, float smallRadius,
                               float findSmallCircleLength, float fishAngle, boolean isBigCircle) {

        //设置鱼尾摆动角度 如果是大圆（下节肢big）摆动角度略小  如果是小圆摆动幅度偏大，因此上节肢选取摆幅-15~15  下节肢选取-35~35
        //而且两者摆动的的位置相差一个相位，也就是尾鳍上半部分刚滑到中间，可能下半截还处在最右，或者最左。 因此上半节肢领先一个相位选取cos
        //但相比于鱼头摆动，尾鳍摆动的幅度要更大一点 所以此处乘以1.5
        float segmentAngle;
        if (isBigCircle) {
            segmentAngle = (float) (fishAngle + Math.cos(Math.toRadians(currentValue * 1.5)) * 15);
        } else {
            segmentAngle = (float) (fishAngle + Math.sin(Math.toRadians(currentValue * 1.5)) * 35);
        }

        //计算梯形的上底中心坐标
        PointF upperCenterPoint = calculatePoint(bottomCenterPoint, findSmallCircleLength, segmentAngle - 180);

        //梯形的四个点坐标
        PointF bottomLeftPoint = calculatePoint(bottomCenterPoint, bigRadius, segmentAngle + 90);
        PointF bottomRightPoint = calculatePoint(bottomCenterPoint, bigRadius, segmentAngle - 90);
        PointF upperLeftPoint = calculatePoint(upperCenterPoint, smallRadius, segmentAngle + 90);
        PointF upperRightPoint = calculatePoint(upperCenterPoint, smallRadius, segmentAngle - 90);

        if (isBigCircle) {
            //画大圆 - 只在绘制大中圆的时候才用到
            canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, bigRadius, mPaint);
        }
        //画小圆 - 一个是大中圆里的中圆，还有就是鱼尾的小圆
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, smallRadius, mPaint);

        //绘制大中圆连起的梯形区域 先从梯形下底部左边开始，再到下底右边，然后上底右边，最后自动连线封闭
        mPath.reset();
        mPath.moveTo(bottomLeftPoint.x, bottomLeftPoint.y);
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y);
        mPath.lineTo(upperRightPoint.x, upperRightPoint.y);
        mPath.lineTo(upperLeftPoint.x, upperLeftPoint.y);
        canvas.drawPath(mPath, mPaint);

        return upperCenterPoint;
    }

    /**
     * 绘制鱼鳍 利用贝塞尔曲线的控制点拉伸 找到合适的鱼鳍轮廓线
     *
     * @param canvas      画布
     * @param startPoint  鱼鳍起始点
     * @param fishAngle   鱼头朝向角度
     * @param isRightSide 是否是右鱼鳍
     */
    private void makeFins(Canvas canvas, PointF startPoint, float fishAngle, boolean isRightSide) {
        float controlAngle = 115;

        //鱼鳍的终点 -- 二阶贝塞尔曲线的终点
        PointF endPoint = calculatePoint(startPoint, FINS_LENGTH, fishAngle - 180);
        //找到贝塞尔曲线的曲率控制点 -- 这个很关键
        PointF controlPoint = calculatePoint(startPoint, FINS_LENGTH * 1.8f,
                isRightSide ? fishAngle - controlAngle : fishAngle + controlAngle);

        //连线绘制
        mPath.reset();
        //将画笔移动到绘制起始点
        mPath.moveTo(startPoint.x, startPoint.y);
        //画二阶贝塞尔曲线
        mPath.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y);
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 一切的一切都是这个方法算出来的坐标
     *
     * @param startPoint 起始点坐标
     * @param length     目标点距离起始点直线距离 - 线长
     * @param angle      鱼当前的朝向角度
     * @return 返回目标点坐标
     */
    public PointF calculatePoint(PointF startPoint, float length, float angle) {
        //x坐标 (此处三角余弦函数必须转成弧度计算)
        float xCoordinate = (float) (Math.cos(Math.toRadians(angle)) * length);
        //y坐标（同上）
        float yCoordinate = (float) (Math.sin(Math.toRadians(angle - 180)) * length);

        return new PointF(startPoint.x + xCoordinate, startPoint.y + yCoordinate);
    }

    /**
     * 设置透明度
     *
     * @param alpha 透明度值
     */
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /**
     * 设置颜色过滤
     *
     * @param colorFilter 过滤颜色
     */
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    /**
     * 获取透明度
     *
     * @return 半透明
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * 下面两个方法必须覆写
     * 获得drawable固有的宽高  如果不覆写，界面无法感知绘制区域大小，会出现draw()中绘制的图形不展示问题
     */
    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * HEAD_RADIUS);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * HEAD_RADIUS);
    }

    public PointF getMiddlePoint() {
        return middlePoint;
    }

    public PointF getHeadPoint() {
        return headPoint;
    }

    public float getHEAD_RADIUS() {
        return HEAD_RADIUS;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getFishMainAngle() {
        return fishMainAngle;
    }

    public void setFishMainAngle(float fishMainAngle) {
        this.fishMainAngle = fishMainAngle;
    }
}
