package com.my.bleoclock.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.data.BleDevice;
import com.google.gson.Gson;
import com.my.bleoclock.ApiBackCall;
import com.my.bleoclock.Ble.BleUUID;
import com.my.bleoclock.Dialog.Basedialog;
import com.my.bleoclock.R;
import com.my.bleoclock.View.FragmentAlarmClockView;
import com.my.bleoclock.adapter.OclockAdapter;
import com.my.bleoclock.protocol.Alarm;
import com.my.bleoclock.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentAlarmClock extends Fragment implements FragmentAlarmClockView.ClickListener{
    @BindView(R.id.review_oclock)
    RecyclerView reviewOclock;
    @BindView(R.id.fragment_oclock_scan_ble)
    TextView tvScan;

    private FragmentAlarmClockView clockView;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Alarm> listAlarmClockInfo = new ArrayList<>();
    private BleDevice bleDevice;

    public FragmentAlarmClock() {
    }

    //bleDevice 传递过来，供添加时使用
    public static Fragment newInstance(BleDevice bleDevice) {
        FragmentAlarmClock fragment = new FragmentAlarmClock();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SECTION_NUMBER, bleDevice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            bleDevice = getArguments().getParcelable(ARG_SECTION_NUMBER);
        }
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_oclock, container, false);
        clockView = new FragmentAlarmClockView(getActivity(),view,listAlarmClockInfo, (FragmentAlarmClockView.ClickListener) this);
        return view;
    }

    @OnClick(R.id.fragment_oclock_scan_ble)
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.fragment_oclock_scan_ble:
                Toast.makeText(getContext(),"点击了扫描",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Bundle bundle = data.getExtras();
            byte[] data1 = bundle.getByteArray("data");
            Logger.e("data",data1[0]+"");
            for(byte tem:data1){
                System.out.print(tem+" ");
            }
        }
    }


    private void init(){
        for(int i=0;i<5;i++){
            Alarm oclockInfo = new Alarm((byte)12,(byte)43,true,
            "天空之城",true,
                    new byte[]{1,1,1,0,0,1,0},"早起");
            listAlarmClockInfo.add(oclockInfo);
        }
    }

    @Override
    public void longClick(final OclockAdapter oclockAdapter,final int position) {
        Basedialog basedialog = new Basedialog(getActivity(), bleDevice, new ApiBackCall() {
            @Override
            public void success(Object object) {
                listAlarmClockInfo.remove(position);
                oclockAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void clickItem(final OclockAdapter oclockAdapter,final int position) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_main_ll,new FragmentAlarmClockSet(listAlarmClockInfo.get(position),new ApiBackCall() {
                    @Override
                    public void success(Object object) {
                        Alarm alarm = (Alarm) object;
                        listAlarmClockInfo.remove(position);
                        listAlarmClockInfo.add(position,alarm);
                        oclockAdapter.notifyDataSetChanged();
                    }
                }))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void clickSwitchVibration(OclockAdapter oclockAdapter, int position,boolean b) {
        Alarm alarm = listAlarmClockInfo.get(position);
        alarm.setOnOff(b);
        listAlarmClockInfo.remove(position);
        listAlarmClockInfo.add(position,alarm);
        oclockAdapter.notifyDataSetChanged();
    }

    @Override
    public void clickFAB() {
        getActivity().getSupportFragmentManager().beginTransaction()
            .add(R.id.activity_main_ll,new FragmentAlarmClockSet(new ApiBackCall() {
                @Override
                public void success(Object object) {
                Alarm alarm = (Alarm) object;
                listAlarmClockInfo.add(alarm);

                Gson gson = new Gson();
                String json = gson.toJson(alarm);
                Logger.e("gson",json);

                BleUUID.write(bleDevice , json);

                Alarm tem = gson.fromJson(json,Alarm.class);
                tem.getHour();
                }
            }))
            .addToBackStack(null)
            .commit();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
