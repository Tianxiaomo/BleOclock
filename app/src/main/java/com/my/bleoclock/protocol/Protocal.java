package com.my.bleoclock.protocol;

/**
 * Created by qkz on 2018/3/5.
 */

public class Protocal {
    private byte startByte = 0x55;
    private byte[] longthByte = new byte[2];
    private byte devicetypeByte;
    private byte deviceIdByte;
    private byte controlByte;
    private byte dataByte;
    private byte[] sourceByte = new byte[4];
    private byte[] data;
    private byte[] crcByte = new byte[2];
    private byte endByte = (byte) 0xaa;


    public byte[] getLongthByte() {
        return longthByte;
    }

    public void setLongthByte(byte[] longthByte) {
        this.longthByte = longthByte;
    }

    public byte getDevicetypeByte() {
        return devicetypeByte;
    }

    public void setDevicetypeByte(byte devicetypeByte) {
        this.devicetypeByte = devicetypeByte;
    }

    public byte getDeviceIdByte() {
        return deviceIdByte;
    }

    public void setDeviceIdByte(byte deviceIdByte) {
        this.deviceIdByte = deviceIdByte;
    }

    public byte getControlByte() {
        return controlByte;
    }

    public void setControlByte(byte controlByte) {
        this.controlByte = controlByte;
    }

    public byte getDataByte() {
        return dataByte;
    }

    public void setDataByte(byte dataByte) {
        this.dataByte = dataByte;
    }

    public byte[] getSourceByte() {
        return sourceByte;
    }

    public void setSourceByte(byte[] sourceByte) {
        this.sourceByte = sourceByte;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getCrcByte() {
        return crcByte;
    }

    public void setCrcByte(byte[] crcByte) {
        this.crcByte = crcByte;
    }

}
