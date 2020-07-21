package com.tufusi.swimmingfish.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

/**
 * Created by 鼠夏目 on 2020/7/20.
 *
 * @See
 * @Description
 */
public class FishRelativeLayout extends RelativeLayout {

    private Paint mPaint;
    private ImageView ivFish;
    private FishDrawable fishDrawable;
    private float touchX;
    private float touchY;
    private float ripple;
    private int alpha;

    public FishRelativeLayout(Context context) {
        this(context, null);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //记住 ViewGroup默认不支持onDraw， 如果需要开启必须设置开关, 让绘制到View去实现
        setWillNotDraw(false);

        //设置画笔 此处是为了绘制点击产生的波纹特效， 因此设置画笔样式为stroke
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        //设置页面控件 把鱼加进来
        ivFish = new ImageView(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        ivFish.setLayoutParams(layoutParams);
//        ivFish.setBackgroundColor(Color.BLUE);
        fishDrawable = new FishDrawable();
        ivFish.setBackground(fishDrawable);

        addView(ivFish);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //动态绘制波纹透明度
        mPaint.setAlpha(alpha);
        //绘制波纹 并设置波纹半径
        canvas.drawCircle(touchX, touchY, ripple * 150, mPaint);

        //刷新View
        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取点击区域的位置
        touchX = event.getX();
        touchY = event.getY();

        //设置点击一瞬间的画笔透明度 为全不透明
        mPaint.setAlpha(100);
        //使用属性值动画ObjectAnimator ， 它在ValueAnimator基础上，通过反射技术实现动画功能，传参进去的值要务必确保对外提供Getter和Setter控制器
        //波纹的扩大全靠此处实现
        @SuppressLint("ObjectAnimatorBinding")
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "ripple", 0, 1);
        animator.start();

        makeBodyMove();
        return super.onTouchEvent(event);
    }

    /**
     * 设置鱼游动 这是最难最重要的一步
     * 这里主要是理解三阶贝塞尔曲线，找到起始点、控制点1、控制点2、和结束点，就能通过path的cubicTo()画出路径
     * 这里设定： 起始点 = 鱼的重心坐标
     * 结束点 = 手指触摸点击的位置
     * 控制点1 = 鱼头圆心坐标
     * 控制点2 = 鱼头（H点） - 鱼重心（M点） - 触摸点（T点） 三者夹角的一半上的某点（难点）
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void makeBodyMove() {
        //鱼的重心 -- 相对ImageView的位置
        PointF middleRelativePoint = fishDrawable.getMiddlePoint();
        //鱼的重心 -- 绝对坐标
        PointF middleAbsolutePoint = new PointF(middleRelativePoint.x + ivFish.getX(), middleRelativePoint.y + ivFish.getY());

        //鱼头圆心坐标  控制点1 -- 相对ImageView位置
        PointF headRelativePoint = fishDrawable.getHeadPoint();
        //鱼头圆心坐标  控制点1 -- 绝对坐标
        PointF headAbsolutePoint = new PointF(headRelativePoint.x + ivFish.getX(), headRelativePoint.y + ivFish.getY());

        //结束点 这里需要注意
        PointF touchPoint = new PointF(touchX, touchY);

        //控制点2的寻找
        //首先计算 ∠HMT 这里用反余弦计算角度公式
        float HMTAngle = calculateAngle(middleAbsolutePoint, headAbsolutePoint, touchPoint) / 2;
        float headDirectionAngle = calculateAngle(middleAbsolutePoint, new PointF(middleAbsolutePoint.x + 1, middleAbsolutePoint.y), headAbsolutePoint);

        //控制点2坐标
        PointF controlPoint = fishDrawable.calculatePoint(middleAbsolutePoint, fishDrawable.getHEAD_RADIUS() * 1.6f, HMTAngle + headDirectionAngle);

        //绘制三阶贝塞尔曲线路径
        final Path path = new Path();
        //这里需要减去fishDrawable相对容器ImageView的距离 不然鱼游过来是外层ImageView正对好，而不是鱼头正对好触摸点
        //移动到起始点
        path.moveTo(middleAbsolutePoint.x - middleRelativePoint.x, middleAbsolutePoint.y - middleRelativePoint.y);
        //贝塞尔曲线设置 两个控制点 和 终点
        path.cubicTo(headAbsolutePoint.x - middleRelativePoint.x, headAbsolutePoint.y - middleRelativePoint.y,
                controlPoint.x - middleRelativePoint.x, controlPoint.y - middleRelativePoint.y,
                touchX - middleRelativePoint.x, touchY - middleRelativePoint.y);

        //设置属性动画 对象是ivFish 也是根据x,y属性值设置路径移动动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivFish, "x", "y", path);
        objectAnimator.setDuration(2000);
        //设置监听
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //设置鱼结束动画的时候鱼尾摆动频率回归正常
                fishDrawable.setFrequency(1f);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                //设置鱼开始动画的时候鱼尾摆动频率
                fishDrawable.setFrequency(3f);
            }
        });

        //绘制路径 操作Path 可以获取Path的长度、path上某个点的坐标、正切值、甚至是部分path
        //此处获取正切值 传入数组，取得正切值后计算鱼头朝向，动态设置方向
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        final float[] tan = new float[2];
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //计算执行了整个周期的百分之比
                float fraction = animation.getAnimatedFraction();

                pathMeasure.getPosTan(pathMeasure.getLength() * fraction, null, tan);
                float angle = (float) Math.toDegrees(Math.atan2(-tan[1], tan[0]));
                fishDrawable.setFishMainAngle(angle);
            }
        });
        objectAnimator.start();
    }

    /**
     * 计算两边夹角
     *
     * @param middlePoint M 鱼重心
     * @param headPoint   H 鱼头中心
     * @param touchPoint  T 手指触摸点
     * @return 返回两边夹角
     */
    private float calculateAngle(PointF middlePoint, PointF headPoint, PointF touchPoint) {
        //cosHMT = (HM * MT)/(|HM| * |MT|)
        //向量积：HM * MT = (H.x - M.x)*(T.x - M.x) + (H.y - M.y)*(T.y - M.y)
        float HMT = (headPoint.x - middlePoint.x) * (touchPoint.x - middlePoint.x) +
                (headPoint.y - middlePoint.y) * (touchPoint.y - middlePoint.y);

        float HMLength = (float) Math.sqrt((headPoint.x - middlePoint.x) * (headPoint.x - middlePoint.x) +
                (headPoint.y - middlePoint.y) * (headPoint.y - middlePoint.y));
        float MTLength = (float) Math.sqrt((touchPoint.x - middlePoint.x) * (touchPoint.x - middlePoint.x) +
                (touchPoint.y - middlePoint.y) * (touchPoint.y - middlePoint.y));
        float cosHMT = HMT / (HMLength * MTLength);

        //反余弦计算公式
        float angleHMT = (float) Math.toDegrees(Math.acos(cosHMT));

        //HT连线（鱼头圆心与触摸点连线）与X轴的夹角的tan值 - MT连线（鱼重心与触摸点连线）与x轴的夹角的tan值
        //通过差值与0比较判断鱼转头方向
        float direction = (headPoint.x - touchPoint.x) / (headPoint.y - touchPoint.y) -
                (middlePoint.x - touchPoint.x) / (middlePoint.y - touchPoint.y);

        if (direction == 0) {
            //如果三点在一条直线上 一个是鱼头正对着触摸点 还有一个就是鱼头背对着触摸点 那么就要看HMT向量积的正负 大于0那么就是同向 反之反向
            if (HMT > 0) {
                return 0f;
            } else {
                return 180f;
            }
        } else {
            //如果不在一条直线上 这里主要是为了考虑鱼转头游向触摸点是往左掉头还是往右  最短路径判断 除非有傻鱼当然也会有
            //如果鱼头中心点和触摸点连线（HT）与x轴连线的角度 > 鱼重心点和触摸点（MT）与x轴的连线的角度(direction < 0)，那么鱼的整体游动方向是MT连线的往左方向掉头
            //如果鱼头中心点和触摸点连线（HT）与x轴连线的角度 < 鱼重心点和触摸点（MT）与x轴的连线的角度(direction > 0)，那么鱼的整体游动方向是MT连线的往右方向掉头
            if (direction > 0) {
                return -angleHMT;
            } else {
                return angleHMT;
            }
        }
    }

    public float getRipple() {
        return ripple;
    }

    public void setRipple(float ripple) {
        //透明度的变化放在这里
        alpha = (int) ((1 - ripple) * 100);
        this.ripple = ripple;
    }
}
