package com.tufusi.swimmingfish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.tufusi.swimmingfish.fish.FishActivity;

/**
 * Created by 鼠夏目 on 2020/7/17.
 *
 * @See
 * @Description
 */
public class MainFragment extends ListFragment {

    ArrayAdapter<String> arrayAdapter;

    public static Fragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] array = new String[]{
                "游动的鱼",
                "帧动画",
                "属性动画"
        };

        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, array);
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String item = arrayAdapter.getItem(position);
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(getActivity(), FishActivity.class);
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }
}
