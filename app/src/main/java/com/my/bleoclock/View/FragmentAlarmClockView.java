package com.my.bleoclock.View;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.my.bleoclock.protocol.Alarm;
import com.my.bleoclock.ItemSwitchChangeListener;
import com.my.bleoclock.R;
import com.my.bleoclock.adapter.OclockAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by qkz on 2018/3/23.
 */

public class FragmentAlarmClockView {
    @BindView(R.id.review_oclock)
    RecyclerView reviewOclock;
    @BindView(R.id.fragment_oclock_scan_ble)
    TextView tvScan;
    private List<Alarm> listAlarmClockInfo = new ArrayList<>();

//
//    private void init(){
//        for(int i=0;i<5;i++){
//            Alarm oclockInfo = new Alarm((byte)12,(byte)43,true,
//                    "天空之城",true,
//                    new byte[]{1,1,1,0,0,1,0},"早起");
//            listAlarmClockInfo.add(oclockInfo);
//        }
//    }

    public FragmentAlarmClockView(final Activity activity, View view,
                                  List<Alarm> mListAlarmClockInfo,
                                  final FragmentAlarmClockView.ClickListener clickListener){
        ButterKnife.bind(this,view);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        reviewOclock.setLayoutManager(linearLayoutManager);
        final OclockAdapter oclockAdapter = new OclockAdapter(activity, mListAlarmClockInfo);
        reviewOclock.setAdapter(oclockAdapter);

        //点击进入设置页面可以修改
            oclockAdapter.setClickItem(new OclockAdapter.ClickItem() {
            @Override
            public void clickItem(View view, final int position) {
                clickListener.clickItem(oclockAdapter,position);
            }
        });

        //长按判断是否删除
            oclockAdapter.setOnLongClickListener(new OclockAdapter.ClickItem() {
            @Override
            public void clickItem(View view, final int position) {
                clickListener.longClick(oclockAdapter,position);
            }
        });

            oclockAdapter.setSwitchVibration(new ItemSwitchChangeListener() {
            @Override
            public void change(int position, boolean b) {
                clickListener.clickSwitchVibration(oclockAdapter,position,b);
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.clickFAB();
            }
        });
    }

    public interface ClickListener {
        public void longClick(OclockAdapter oclockAdapter,int position);
        public void clickItem(OclockAdapter oclockAdapter,int position);
        public void clickSwitchVibration(OclockAdapter oclockAdapter,int position,boolean b);
        public void clickFAB();
    }
}
