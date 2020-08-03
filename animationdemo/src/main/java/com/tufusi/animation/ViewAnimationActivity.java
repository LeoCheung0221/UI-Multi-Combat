package com.tufusi.animation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class ViewAnimationActivity extends AppCompatActivity {

    private static final String TAG = "View Animation";
    private View mTargetView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);

        initToolbar();
        mTargetView = findViewById(R.id.view_target);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        switch (i) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_alpha:
                doAnimation(getAlphaAnimation(), getString(R.string.alpha_animation));
                break;
            case R.id.action_rotate:
                doAnimation(getRotateAnimation(), getString(R.string.rotate_animation));
                break;
            case R.id.action_scale:
                doAnimation(getScaleAnimation(), getString(R.string.scale_animation));
                break;
            case R.id.action_translate:
                doAnimation(getTranslateAnimation(), getString(R.string.translate_animation));
                break;
            case R.id.action_set:
                doAnimation(getAnimationSet(true), getString(R.string.animation_set));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Animation getAnimationSet(boolean isFromXML) {
        if (isFromXML) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_animation);
            return animation;
        } else {
            AnimationSet innerAnimationSet = new AnimationSet(true);
            innerAnimationSet.setInterpolator(new BounceInterpolator());
            innerAnimationSet.setStartOffset(1000);
            innerAnimationSet.addAnimation(getScaleAnimation());
            innerAnimationSet.addAnimation(getTranslateAnimation());

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.setInterpolator(new LinearInterpolator());
            animationSet.addAnimation(getAlphaAnimation());
            animationSet.addAnimation(getRotateAnimation());
            animationSet.addAnimation(innerAnimationSet);

            return animationSet;
        }
    }

    private Animation getTranslateAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, getWidth() * 2,
                0, getHeight() * 2);
        translateAnimation.setDuration(2000);
        translateAnimation.setRepeatCount(2);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillBefore(false);
        translateAnimation.setRepeatMode(Animation.REVERSE);

        return translateAnimation;
    }

    private Animation getScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f,
                1f, 2f,
                getWidth() / 2, getHeight() / 2);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(2);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillBefore(false);

        return scaleAnimation;
    }

    private Animation getRotateAnimation() {
        //0°旋转到360°  View中心为旋转对称中心
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f,
                getWidth() / 2, getHeight() / 2);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatMode(Animation.REVERSE);
        rotateAnimation.setRepeatCount(1);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setFillBefore(false);

        return rotateAnimation;
    }

    /**
     * 代码实现渐变动画
     */
    private Animation getAlphaAnimation() {
        //从显示到隐藏
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        //设置持续时间
        alphaAnimation.setDuration(1000);
        //设置动画重复模式，是正着重复还是倒着重复
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        //设置动画重复次数
        alphaAnimation.setRepeatCount(1);
        //设置动画结束后是否保持其样式(注：这里是根据重复次数最后一次所呈现的样式而定) 只改变动画的属性，不改变View位置
        alphaAnimation.setFillAfter(true);
        //设置动画结束前是否保留最后一帧 这里是衔接上面的FillAfter
        alphaAnimation.setFillBefore(false);

        return alphaAnimation;
    }


    private void doAnimation(Animation animation, final String animationType) {
        Animation oldAnimation = mTargetView.getAnimation();
        if (oldAnimation != null) {
            if (oldAnimation.hasStarted() || oldAnimation.hasEnded()) {
                oldAnimation.cancel();
                mTargetView.clearAnimation();
            }
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, animationType + " start;");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, animationType + " end;");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, animationType + " repeat;");
            }
        });
        mTargetView.startAnimation(animation);
    }

    private int getWidth() {
        return mTargetView.getWidth();
    }

    private int getHeight() {
        return mTargetView.getHeight();
    }
}
