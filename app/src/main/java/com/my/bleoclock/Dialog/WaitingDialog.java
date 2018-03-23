package com.my.bleoclock.Dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

/**
 * Created by qkz on 2018/3/21.
 */

public class WaitingDialog {
    private static ProgressDialog waitingDialog;

    public static void show(Activity activity) {
        waitingDialog = new ProgressDialog(activity);
        waitingDialog.setTitle("发成功了，给我数据");
        waitingDialog.setMessage("等待中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        waitingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    waitingDialog.dismiss();
                }
                return false;
            }
        });
    }

    public static void dismiss(){
        waitingDialog.dismiss();
    }
}
