package com.my.bleoclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.data.BleDevice;
import com.google.gson.Gson;
import com.my.bleoclock.adapter.OclockAdapter;
import com.my.bleoclock.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentOclock extends Fragment {
    @BindView(R.id.review_oclock)
    RecyclerView reviewOclock;
    @BindView(R.id.fragment_oclock_scan_ble)
    TextView tvScan;

    private String TAG = getTag();

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Alarm> listOclockInfo = new ArrayList<>();
    private BleDevice bleDevice;

    public FragmentOclock() {
    }

    //bleDevice 传递过来，供添加时使用
    public static Fragment newInstance(BleDevice bleDevice) {
        FragmentOclock fragment = new FragmentOclock();
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
        ButterKnife.bind(this,view);

        RecyclerView reviewOclock = (RecyclerView)view.findViewById(R.id.review_oclock);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        reviewOclock.setLayoutManager(linearLayoutManager);
        final OclockAdapter oclockAdapter = new OclockAdapter(getActivity(),listOclockInfo);
        reviewOclock.setAdapter(oclockAdapter);

        //点击进入设置页面可以修改
        oclockAdapter.setClickItem(new OclockAdapter.ClickItem() {
            @Override
            public void clickItem(View view, final int position) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.activity_main_ll,new FragmentAlarmSet(listOclockInfo.get(position),new ApiBackCall() {
                            @Override
                            public void success(Object object) {
                                Alarm alarm = (Alarm) object;
                                listOclockInfo.remove(position);
                                listOclockInfo.add(position,alarm);
                                oclockAdapter.notifyDataSetChanged();
                            }
                        }))
                        .addToBackStack(null)
                        .commit();
            }
        });

        //长按判断是否删除
        oclockAdapter.setOnLongClickListener(new OclockAdapter.ClickItem() {
            @Override
            public void clickItem(View view, final int position) {
                Basedialog basedialog = new Basedialog(getActivity(), bleDevice, new ApiBackCall() {
                    @Override
                    public void success(Object object) {
                        listOclockInfo.remove(position);
                        oclockAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        oclockAdapter.setSwitchVibration(new ItemSwitchChangeListener() {
            @Override
            public void change(int position, boolean b) {
                    Alarm alarm = listOclockInfo.get(position);
                    alarm.setOnOff(b);
                    listOclockInfo.remove(position);
                    listOclockInfo.add(position,alarm);
                    oclockAdapter.notifyDataSetChanged();
            }
        });

        //oclock fragment 右下角按钮
        FloatingActionButton fba = view.findViewById(R.id.fab);
        fba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//                    @Override
//                    public void onBackStackChanged() {
//                        FragmentManager manager = getActivity().getSupportFragmentManager();
//                        if (manager != null) {
//                            Fragment currFrag = (Fragment) manager.findFragmentById(R.id.activity_main_ll);
//                            currFrag.onResume();
//                        }
//                    }
//                });
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.activity_main_ll,new FragmentAlarmSet(new ApiBackCall() {
                            @Override
                            public void success(Object object) {
                                Alarm alarm = (Alarm) object;
                                listOclockInfo.add(alarm);
                                Gson gson = new Gson();
                                String temString = gson.toJson(alarm);
                                Logger.e("asdfsadf",temString);
                                Alarm tem = gson.fromJson(temString,Alarm.class);
                                tem.getHour();
                            }
                        }))
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
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
            listOclockInfo.add(oclockInfo);
        }
    }
}
