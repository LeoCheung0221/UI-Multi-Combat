package com.tufusi.slideapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tufusi.slideapp.adapter.KKViewHolder;
import com.tufusi.slideapp.adapter.UniversalAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private UniversalAdapter<SlideCardBean> mAdapter;
    private List<SlideCardBean> mListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListBean = SlideCardBean.initDatas();

        mRecyclerView = findViewById(R.id.rv_slide_view);
        mRecyclerView.setLayoutManager(new SlideCardLayoutManager());

        mAdapter = new UniversalAdapter<SlideCardBean>(this, mListBean, R.layout.item_slide_card_layout) {
            @Override
            protected void convert(KKViewHolder holder, SlideCardBean data) {
                holder.setText(R.id.tvName, data.getName());
                holder.setText(R.id.tvPrecent, data.getPosition() + "/" + mDatas.size());
                Glide.with(MainActivity.this)
                        .load(data.getUrl())
                        .into((ImageView) holder.getView(R.id.iv));
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        CardConfig.initConfig(this);

        //设置手势操作
        SlideCallback callback = new SlideCallback(mRecyclerView, mAdapter, mListBean);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        //依附到View容器
        helper.attachToRecyclerView(mRecyclerView);
    }
}
