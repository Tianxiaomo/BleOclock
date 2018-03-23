package com.my.bleoclock.Ble;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

/**
 * Created by qkz on 2018/3/23.
 */

public class BleUUID {
    public final static String UUID_SERVICE = "0000abf0-0000-1000-8000-00805f9b34fb";
    public final static String UUID_WIFISET   = "0000abf1-0000-1000-8000-00805f9b34fb";

    public final static String UUID_WRITE   = "0000abf3-0000-1000-8000-00805f9b34fb";
    public final static String UUID_NOTIFYTION  = "0000abf4-0000-1000-8000-00805f9b34fb";


    public static void write(BleDevice bleDevice, String data){
        byte[] hex = data.getBytes();
        BleManager.getInstance().write(
            bleDevice,
            UUID_SERVICE,
            UUID_WRITE,
            hex,
            false,
            new BleWriteCallback() {
                @Override
                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {

                };

                @Override
                public void onWriteFailure(final BleException exception) {

                }
            });
    }

    public void notifycation(BleDevice bleDevice, String data){
        byte[] hex = data.getBytes();
        BleManager.getInstance().notify(bleDevice, UUID_SERVICE, UUID_NOTIFYTION, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                //打开成功
            }
            @Override
            public void onNotifyFailure(BleException exception) {
                //打开失败
            }
            @Override
            public void onCharacteristicChanged(byte[] data) {
                //收到数据
            }
        });
    }

}
