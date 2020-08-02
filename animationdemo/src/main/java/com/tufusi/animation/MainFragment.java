package com.tufusi.animation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;

/**
 * Created by 鼠夏目 on 2020/8/3.
 *
 * @author 鼠夏目
 * @description
 * @see
 */
public class MainFragment extends ListFragment {

    private ArrayAdapter<String> arrayAdapter;

    public static Fragment getInstance() {
        return new MainFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] array = new String[]{
                "视图动画",
                "逐帧动画",
                "属性动画"
        };
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent gotoAct;
        switch (position) {
            //视图动画
            case 0:
                gotoAct = new Intent(getActivity(), ViewAnimationActivity.class);
                startActivity(gotoAct);
                break;
            //帧动画
            case 1:
                gotoAct = new Intent(getActivity(), DrawableAnimationActivity.class);
                startActivity(gotoAct);
                break;
            //属性动画
            case 2:
                gotoAct = new Intent(getActivity(), PropertyAnimationActivity.class);
                startActivity(gotoAct);
                break;
            default:
                break;
        }
    }
}
