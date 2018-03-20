//package com.my.bleoclock.adapter;
//
//import android.content.SharedPreferences;
//
//import java.sql.Time;
//import java.util.Date;
//import java.util.Timer;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by qkz on 2018/1/28.
// */
//
//public class OclockInfo {
//    private String oclock;      //时间
//    private byte[] day;          //那几天定了闹钟，与state 联系，如工作日，周末，等
//    private int state;          //0一次，1每天，2法定工作日，3法定家假日，4周一至周五，5自定义
//    private byte bellVibration;  //闹铃与震动
//    private boolean afterDelect;//响过是否删除
//    private boolean onOff;      //开关 0:off 1:on
//    private String remark;      //备注
//
//    public final static int once = 0;
//    public final static int everyday = 1;
//    public final static int workday = 2;
//    public final static int holiday = 3;
//    public final static int weekday = 4;
//    public final static int custom = 5;
//
//    public OclockInfo(String oclock, byte[] day, int state, byte bellVibration, boolean afterDelect, String remark){
//        this.oclock = oclock;
//        this.day = day;
//        this.state = state;
//        this.bellVibration = bellVibration;
//        this.afterDelect = afterDelect;
//        this.remark = remark;
//    }
//
//    public boolean isOnOff() {
//        return onOff;
//    }
//
//    public void setOnOff(boolean onOff) {
//        this.onOff = onOff;
//    }
//
//    public String getOclock() {
//        return oclock;
//    }
//
//    public void setOclock(String oclock) {
//        this.oclock = oclock;
//    }
//
//    public int[] getDay() {
//        return day;
//    }
//
//    public void setDay(int[] day) {
//        this.day = day;
//    }
//
//    public int getState() {
//        return state;
//    }
//
//    public void setState(int state) {
//        this.state = state;
//    }
//
//    public int getBellVibration() {
//        return bellVibration;
//    }
//
//    public void setBellVibration(int bellVibration) {
//        this.bellVibration = bellVibration;
//    }
//
//    public boolean isAfterDelect() {
//        return afterDelect;
//    }
//
//    public void setAfterDelect(boolean afterDelect) {
//        this.afterDelect = afterDelect;
//    }
//
//    public String getRemark() {
//        return remark;
//    }
//
//    public void setRemark(String remark) {
//        this.remark = remark;
//    }
//
//}
