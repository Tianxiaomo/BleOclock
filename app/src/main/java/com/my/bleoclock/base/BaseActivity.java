package com.my.bleoclock.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.my.bleoclock.util.Logger;

/**
 * Created by qkz on 2018/1/27.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Logger.e("Ble_Oclock",getClass().getSimpleName());
    }
}
