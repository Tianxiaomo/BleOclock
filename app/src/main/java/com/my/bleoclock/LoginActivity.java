package com.my.bleoclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.my.bleoclock.Fragment.FragmentLogin;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_login_fl,new FragmentLogin())
                // .addToBackStack(null)
                .commit();
    }
}
