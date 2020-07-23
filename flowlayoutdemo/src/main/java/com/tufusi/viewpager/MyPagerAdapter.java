package com.tufusi.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.tufusi.flowlayout.R;

import java.util.List;

/**
 * Created by 鼠夏目 on 2020/7/23.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Integer> mImages;

    public MyPagerAdapter(Context context, List<Integer> images) {
        mContext = context;
        mImages = images;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        position = position % mImages.size();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_pager_view, container, false);
        ImageView imageView = view.findViewById(R.id.iv_banner_pic);
        imageView.setImageResource(mImages.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
