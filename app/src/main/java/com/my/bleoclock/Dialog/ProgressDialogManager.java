package com.my.bleoclock.Dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.text.TextUtils;

public class ProgressDialogManager {

   public ProgressDialog mSystemProgressDialog;
    private Activity baseAty;

    public ProgressDialogManager(Activity baseAty) {
        this.baseAty = baseAty;
    }

    /**
     * 显示系统类型的加载框
     */
    public void showLaoding(String content) {
        if (null == mSystemProgressDialog) {
            mSystemProgressDialog = new ProgressDialog(baseAty);
        }
        mSystemProgressDialog.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(content)) {
            mSystemProgressDialog.setMessage(content);
        } else {
            mSystemProgressDialog.setMessage("loading now");
        }
        if (!baseAty.isFinishing()) {
            mSystemProgressDialog.show();
        }
    }

//    /**
//     * 加载时按返回
//     * /
    public void backPressed(final BackbackPressed backbackPressed){
        mSystemProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                backbackPressed.onCallback();
            }
        });
    }

    /**
     * 取消系统类型的加载框
     */
    public void dismiss() {
        if (null != mSystemProgressDialog) {
            mSystemProgressDialog.dismiss();
        }
    }


}
