package com.timark.zhou.zhoutest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZHOU on 2017/6/27.
 */

public class MainActivity extends AppCompatActivity {

    private GridView mGv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        mGv = (GridView) this.findViewById(R.id.grid);
        mGv.setAdapter(new GridAdapter(this, getStringList(), mGv));
    }

    private List<String> getStringList(){
        int size = 6;
        List<String> list = new ArrayList<>(size);
//        for (int i = 0; i < size; i++){
//            String str = "测试测试测试测试";
//            for (int j = 0; j < i; j++){
//                str += "测试测试测试测试";
//            }
//            list.add(str);
//        }
        for (int i = size; i > 0; i--){
            String str = "测试测试测试测试";
            for (int j = 0; j < i; j++){
                str += "测试测试测试测试";
            }
            list.add(str);
        }
        return list;
    }
}
