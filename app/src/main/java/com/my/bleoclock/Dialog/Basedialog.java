package com.my.bleoclock.Dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.my.bleoclock.ApiBackCall;
import com.my.bleoclock.R;

import java.util.Date;

/**
 * Created by MO on 2018/3/13.
 */


public class Basedialog {
    private AlertDialog addcardDialog;
    private Activity activity;
    private BleDevice bleDevice;
    private ApiBackCall apiBackCall;

    public Basedialog(Activity activity, BleDevice bleDevice,ApiBackCall apiBackCall){
        addcardDialog = new AlertDialog.Builder(activity).create();
        this.activity = activity;
        this.bleDevice = bleDevice;
        this.apiBackCall = apiBackCall;
        initDialog();
        show();
    }

    private void initDialog() {
        addcardDialog.show();
        Window window = addcardDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Display display = activity.getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = addcardDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度
        addcardDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.base_dialog);
        window.findViewById(R.id.layout_dialog_add_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_add_card_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addcardDialog.dismiss();
                apiBackCall.success("ok");
            }
        });
    }

    private void show(){
        addcardDialog.show();
    }




    private void writeToBle(byte lock,byte cmd){
        Date dataNow = new Date();
        Date date = new Date(110,1,1,0,0,0);
        byte hex[] = intToHex((dataNow.getTime() - date.getTime())/1000);
        hex[4] = lock;
        hex[5] = cmd;
        BleManager.getInstance().write(
                bleDevice,
                "0000abf0-0000-1000-8000-00805f9b34fb",
                "0000abf3-0000-1000-8000-00805f9b34fb",
//            HexUtil.hexStringToBytes(lock +" " + cmd),
                hex,
                false,
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(activity,"写成功",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"写失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

    private byte[] intToHex(long value){
        byte[] hex = new byte[4];
        hex[0] = (byte)(value%0xFF);
        hex[1] = (byte)(value%0xFFFF/0xFF);
        hex[2] = (byte)(value%0xFFFFFF/0xFFFF);
        hex[3] = (byte)(value/0xFFFFFF);
        return hex;
    }

}
