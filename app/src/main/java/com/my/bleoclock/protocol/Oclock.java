package com.my.bleoclock.protocol;

/**
 * Created by qkz on 2018/3/5.
 */

public class Oclock {
    private byte Hour;
    private byte Minute;
    private byte Second;
    private byte OnOff;
    private byte Ring;
    private byte Vibration;
    private byte Repeat;
    private byte[] Label;
    private byte[] Data;

    public void setHour(byte hour) {
        Hour = hour;
    }

    public void setMinute(byte minute) {
        Minute = minute;
    }

    public void setSecond(byte second) {
        Second = second;
    }

    public void setOnOff(byte onOff) {
        OnOff = onOff;
    }

    public void setRing(byte ring) {
        Ring = ring;
    }

    public void setVibration(boolean vibration) {
        if(vibration) Vibration = 0x01;
        else Vibration = 0x10;
    }

    public void setRepeat(boolean[] repeat) {
        for(int i=0;i<7;i++){
            if(repeat[i]==true){
                Repeat += 1<<i;
            }
        }
    }

    public void setRepeat(byte repeat)
    {
        Repeat = repeat;
    }

    public void setLabel(String label) {
        Label = new byte[label.length()*2];
        Label = stringToByte(label);
    }

    public byte[] getData(){
        Data = new byte[7+Label.length];
        Data[0] = Hour;
        Data[1] = Minute;
        Data[2] = Second;
        Data[3] = OnOff;
        Data[4] = Ring;
        Data[5] = Vibration;
        Data[6] = Repeat;
        System.arraycopy(Label,0,Data,7,Label.length);
        return Data;
    }

    private byte[] stringToByte(String value){
        char[] chars = value.toCharArray();
        byte[] ascii = new byte[chars.length*2];
        for (int i = 0; i < chars.length; i+=2) {
            int temp = (int)chars[i/2];
            ascii[i] = (byte)(temp%0xFF);
            ascii[i+1] = (byte)((temp/0xFFFF)%0xFF);
        }
        return ascii;
    }
}
