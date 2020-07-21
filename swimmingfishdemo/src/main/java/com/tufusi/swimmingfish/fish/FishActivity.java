package com.tufusi.swimmingfish.fish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.tufusi.swimmingfish.R;
import com.tufusi.swimmingfish.view.FishDrawable;
import com.tufusi.swimmingfish.view.FishHeaderView;

public class FishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局使用自定义drawable
        setContentView(R.layout.activity_fish);
        //代码使用
//        setContentView(new FishHeaderView(this));

//        ImageView ivFish = findViewById(R.id.iv_fish);
//        ivFish.setImageDrawable(new FishDrawable());
    }
}
