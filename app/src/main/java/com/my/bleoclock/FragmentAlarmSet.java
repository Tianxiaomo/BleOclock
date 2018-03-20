package com.my.bleoclock;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by qkz on 2018/1/29.
 */

@SuppressLint("ValidFragment")
public class FragmentAlarmSet extends Fragment {
    @BindView(R.id.activity_oclock_set_rl)
    RelativeLayout rl_oclock_set;
    @BindView(R.id.activity_oclock_set_ll_re)
    View view_re;
    @BindView(R.id.activity_oclock_set_ll_ring)
    View view_ring;
    @BindView(R.id.activity_oclock_set_tv_alarm_label)
    EditText etAlarmLabel;
    @BindView(R.id.activity_oclock_set_time)
    TimePicker timePicker;
    @BindView(R.id.activity_oclock_set_tv_repeat)
    TextView tv_repeat;
    @BindView(R.id.activity_oclock_set_tv_ring)
    TextView tv_ring;
    @BindView(R.id.activity_oclock_set_sw_Vibration)
    Switch swVibration;

    private String TAG = "FragmentAlarmSet";
    private String[] items = new String[] {"一次","每天","工作日","周末","自定义"};
    private String[] weekItems = new String[] {"星期一","星期二","星期三","星期四","星期五","星期六","星期日"};
    private byte[ ] selectedWeek = new byte[7];
    private String[] itemsRing = new String[] {"天空之城","致爱丽丝","卡农","西班牙斗牛曲"};
    AlertDialog repeatDialog;
    AlertDialog weekDialog;
    AlertDialog ringDialog;
    ApiBackCall apiBackCall;

    byte[] Repeat = new byte[7];
    String Ring;
    byte Hour;
    byte Minute;
    boolean OnOff = true;
    boolean Vibration = true;
    String Label;
    private int FLAG;
    private Alarm alarm;

    private int addAlarm = 1;
    private int changeAlarm = 2;

    @SuppressLint("ValidFragment")
    public FragmentAlarmSet(Alarm alarm,ApiBackCall apiBackCall) {
        this.apiBackCall = apiBackCall;
        this.alarm = alarm;
    }

    public FragmentAlarmSet(ApiBackCall apiBackCall) {
        this.apiBackCall = apiBackCall;
    }

    public FragmentAlarmSet newInstance(Alarm alarm) {
        FragmentAlarmSet fragment = new FragmentAlarmSet(apiBackCall);
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
        ButterKnife.bind(this,view);
        timePicker.setIs24HourView(true);
        etAlarmLabel.setCursorVisible(false);

        if(alarm != null) {
            FLAG = changeAlarm;
            loadData(alarm);
        }else{
            FLAG = addAlarm;
            alarm = new Alarm();
        }
        initRepeatDialog();
        return view;
    }

    private void loadData(Alarm alarm){
        etAlarmLabel.setText(alarm.getLabel());
        timePicker.setHour(alarm.getHour());
        timePicker.setMinute(alarm.getMinute());
        tv_repeat.setText(loadRepeat(alarm.getRepeat()));
        tv_ring.setText(alarm.getRing());
        swVibration.setChecked(alarm.isVibration());
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
            Toast.makeText(getContext(),"点击了确定",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
            alarm.setHour((byte)timePicker.getHour());
            alarm.setMinute((byte)timePicker.getMinute());
            if(FLAG == addAlarm){
                alarm.setOnOff(true);
            }else{
                alarm.setOnOff(alarm.isOnOff());
            }

            alarm.setRepeat(Repeat);
            alarm.setLabel(etAlarmLabel.getText().toString());
            alarm.setRing(tv_ring.getText().toString());
            alarm.setVibration(Vibration);
            apiBackCall.success(alarm);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String loadRepeat(byte[] repeat){
        String temString = "";
        if(Arrays.equals(repeat,new byte[]{0,0,0,0,0,0,0})){
            temString = items[0];
        }else if(Arrays.equals(repeat,new byte[]{1,1,1,1,1,1,1})){
            temString = items[1];
        }else if(Arrays.equals(repeat,new byte[]{1,1,1,1,1,0,0})){
            temString = items[2];
        }else if(Arrays.equals(repeat , new byte[]{0,0,0,0,0,1,1})){
            temString = items[3];
        }else{
            for(int j=0;j<7;j++){
                if(repeat[j] == 1)
                    temString += weekItems[j];
            }
        }
        Repeat = repeat;
        return temString;
    }

    //对弹出的对话框初始化
    private void initRepeatDialog() {
        //初始化重复对话框，设置弹出位置和透明度
        repeatDialog = new AlertDialog.Builder(getContext())
        .setSingleChoiceItems(items, -1, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                switch (which){
                    case 0: //once
                        Repeat = new byte[]{0,0,0,0,0,0,0};
                        break;
                    case 1: //everyday
                        Repeat = new byte[]{1,1,1,1,1,1,1};
                        break;
                    case 2: //weekday
                        Repeat = new byte[]{1,1,1,1,1,0,0};
                        break;
                    case 3: //weekend
                        Repeat = new byte[]{0,0,0,0,0,1,1};
                        break;
                    case 4: //cusmnet
                        weekDialog.show();
                        break;
                    default:
                        break;
                }
                if(!items[which].equals("自定义")){
                    tv_repeat.setText(items[which]);
                }
                dialog.dismiss();
            }
        }).create();
        repeatDialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = repeatDialog.getWindow().getAttributes();
        lp.alpha = 0.8f;
        repeatDialog.getWindow().setAttributes(lp);

        //铃声对话框，设置弹出位置和透明度
        ringDialog = new AlertDialog.Builder(getContext())
                .setSingleChoiceItems(itemsRing, -1, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), itemsRing[which], Toast.LENGTH_SHORT).show();
                        tv_ring.setText(itemsRing[which]);
                        Ring = itemsRing[which];
                        dialog.dismiss();
                    }
                }).create();
        ringDialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpRing = ringDialog.getWindow().getAttributes();
        lpRing.alpha = 0.8f;
        ringDialog.getWindow().setAttributes(lpRing);

        //初始化重复自定义对话框，设置弹出位置和透明度
        weekDialog = new AlertDialog.Builder(getContext()).setTitle("重复")
                .setNegativeButton("取消", null).setPositiveButton("确定", new OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String repreatString = "";
                        for(int j=0;j<7;j++){
                            if(selectedWeek[j] == 1)
                                repreatString += weekItems[j];
                        }
                        Repeat = selectedWeek;
                        tv_repeat.setText(repreatString);
                    }
                })
                .setMultiChoiceItems(weekItems, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        selectedWeek[which] = 1;
                    }
                }).create();
        weekDialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lpRp = repeatDialog.getWindow().getAttributes();
        lpRp.alpha = 0.8f;
        weekDialog.getWindow().setAttributes(lp);

        //振动选定
        swVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(getActivity(),"振动",Toast.LENGTH_SHORT).show();
                    Vibration = true;
                }else{
                    Toast.makeText(getActivity(),"取消震动",Toast.LENGTH_SHORT).show();
                    Vibration = false;
                }
            }
        });
    }


    @OnClick({R.id.activity_oclock_set_ll_re,R.id.activity_oclock_set_ll_ring,
            R.id.activity_oclock_set_tv_alarm_label,R.id.activity_oclock_set_rl})
    void finishA(View view) {
        switch (view.getId()){
            case R.id.activity_oclock_set_ll_re:
                Toast.makeText(getActivity(),"点击了重复",Toast.LENGTH_SHORT).show();
                repeatDialog.show();
                break;
            case R.id.activity_oclock_set_ll_ring:
                Toast.makeText(getActivity(),"点击了铃声"+timePicker.getHour()+timePicker.getMinute() +' ',Toast.LENGTH_SHORT).show();
                ringDialog.show();
                break;

            case R.id.activity_oclock_set_tv_alarm_label:
                etAlarmLabel.setCursorVisible(true);
                break;
            case R.id.activity_oclock_set_rl:
                etAlarmLabel.setCursorVisible(false);
                break;
            default:
                break;
        }
    }

}
