package com.my.bleoclock;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MO on 2018/3/9.
 */

public class Alarm implements Parcelable {
    public byte Hour;
    public byte Minute;
    public boolean OnOff;
    public String Ring;
    public boolean Vibration;
    public byte[] Repeat;
    public String Label;
    public Alarm(byte Hour, byte Minute, boolean OnOff,
                 String Ring, boolean Vibration,
                 byte[] Repeat, String Label){
        this.Hour = Hour;
        this.Minute = Minute;
        this.OnOff = OnOff;
        this.Ring = Ring;
        this.Vibration = Vibration;
        this.Repeat = Repeat;
        this.Label = Label;
    }

    /**
     * 序列化实体类
     */
    public static final Parcelable.Creator<Alarm> CREATOR = new Creator<Alarm>() {
        public Alarm createFromParcel(Parcel source) {
            Alarm alarm = new Alarm();
            alarm.Hour = source.readByte();
            alarm.Minute = source.readByte();
            alarm.OnOff = source.readByte()!=0;
            alarm.Ring = source.readString();
            alarm.Vibration = source.readByte()!=0;
            source.readByteArray(alarm.Repeat);
            alarm.Label = source.readString();
            return alarm;
        }

        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    public Alarm(){}

    public byte getHour() {
        return Hour;
    }

    public void setHour(byte hour) {
        Hour = hour;
    }

    public byte getMinute() {
        return Minute;
    }

    public void setMinute(byte minute) {
        Minute = minute;
    }

    public boolean isOnOff() {
        return OnOff;
    }

    public void setOnOff(boolean onOff) {
        OnOff = onOff;
    }

    public String getRing() {
        return Ring;
    }

    public void setRing(String ring) {
        Ring = ring;
    }

    public boolean isVibration() {
        return Vibration;
    }

    public void setVibration(boolean vibration) {
        Vibration = vibration;
    }

    public byte[] getRepeat() {
        return Repeat;
    }

    public void setRepeat(byte[] repeat) {
        Repeat = repeat;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(Hour);
        parcel.writeByte(Minute);
        parcel.writeByte((byte) (OnOff==true?1:0));
        parcel.writeString(Ring);
        parcel.writeByte((byte) (Vibration==true?1:0));
        parcel.writeByteArray(Repeat);
        parcel.writeString(Label);
    }
}
