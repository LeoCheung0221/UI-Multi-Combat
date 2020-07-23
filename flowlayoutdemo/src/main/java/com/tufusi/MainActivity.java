package com.tufusi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tufusi.flowlayout.FlowLayoutActivity;
import com.tufusi.flowlayout.R;
import com.tufusi.viewpager.ViewPagerActivity;

/**
 * @author SZSS
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_goto_viewpager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewPagerActivity.class));
            }
        });
        findViewById(R.id.btn_goto_flow_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FlowLayoutActivity.class));
            }
        });
    }
}
