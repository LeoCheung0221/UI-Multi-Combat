package com.tufusi.slideapp;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by 鼠夏目 on 2020/7/25.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public class CardConfig {

    /**
     * 屏幕上最多显示卡片数量
     */
    public static int MAX_SHOW_COUNT;

    /**
     * 每一级缩放相差0.05f，
     */
    public static float SCALE_GAP;

    /**
     * 每一级卡片偏移量 translation相差7dp
     */
    public static int TRANS_Y_GAP;

    public static void initConfig(Context context) {
        MAX_SHOW_COUNT = 4;
        SCALE_GAP = 0.05f;
        // 把非标准尺寸转换成标准尺寸
        TRANS_Y_GAP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources().getDisplayMetrics());
    }

}
