package com.my.bleoclock.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.my.bleoclock.ApiBackCall;
import com.my.bleoclock.R;
import com.my.bleoclock.View.FragmentAlarmClockSetView;
import com.my.bleoclock.protocol.Alarm;


/**
 * Created by qkz on 2018/1/29.
 */

@SuppressLint("ValidFragment")
public class FragmentAlarmClockSet extends Fragment {
    ApiBackCall apiBackCall;
    private Alarm alarm;
    FragmentAlarmClockSetView clockSetView;

    @SuppressLint("ValidFragment")
    public FragmentAlarmClockSet(Alarm alarm, ApiBackCall apiBackCall) {
        this.apiBackCall = apiBackCall;
        this.alarm = alarm;
    }

    public FragmentAlarmClockSet(ApiBackCall apiBackCall) {
        this.apiBackCall = apiBackCall;
    }

    public FragmentAlarmClockSet newInstance(Alarm alarm) {
        FragmentAlarmClockSet fragment = new FragmentAlarmClockSet(apiBackCall);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oclock_set, container, false);
        clockSetView = new FragmentAlarmClockSetView(getActivity(),view,alarm);
        return view;
    }


    //重新加载toolbar上的布局menu_home
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_android) {
            getActivity().getSupportFragmentManager().popBackStack();
            alarm = clockSetView.getAlarm();
            apiBackCall.success(alarm);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
