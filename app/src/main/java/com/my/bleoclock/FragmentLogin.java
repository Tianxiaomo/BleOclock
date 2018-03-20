package com.my.bleoclock;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.my.bleoclock.adapter.DeviceAdapter;
import com.my.bleoclock.adapter.MainSectionsPagerAdapter;
import com.my.bleoclock.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MO on 2018/3/8.
 */

public class FragmentLogin extends Fragment {
    @BindView(R.id.bleconnect)
    TextView btnBle;
    @BindView(R.id.netconnect)
    TextView btnNet;
    @BindView(R.id.blesend)
    TextView btnSend;

    RecyclerView rvDialogBle;

    private String TAG = getTag();
    private String KEY = "Bledevice";
    private byte Flag=0;
    AlertDialog bleDialog;
    DeviceAdapter deviceAdapter;
    private ProgressDialog progressDialog;
    private AlertDialog bleSendDialog;
    BleDevice bleDevice;

    private MainSectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    public FragmentLogin() {
        // Required empty public constructor
    }


    public FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bleDialog = new AlertDialog.Builder(getContext()).create();
        bleSendDialog = new AlertDialog.Builder(getActivity()).create();
        progressDialog = new ProgressDialog(getContext());
        deviceAdapter = new DeviceAdapter();

        BleManager.getInstance().init(getActivity().getApplication());
        BleManager.getInstance()
                .enableLog(true)
                .setMaxConnectCount(7)
                .setOperateTimeout(5000);
    }

    @OnClick({R.id.bleconnect,R.id.netconnect,R.id.blesend})
    void finishA(View view) {
        switch (view.getId()){
            case R.id.bleconnect:
                bleDialog();
                break;
            case R.id.netconnect:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.blesend:
                bleSendDialogShow();
            default:
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
                ButterKnife.bind(this,view);
        return view;
    }

    private void bleSendDialogShow() {
        bleSendDialog.show();
        Window window = bleSendDialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = bleSendDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度
        bleSendDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_ble_send);
        final EditText etLock = window.findViewById(R.id.layout_dialog_add_card_ed_lock);
        window.findViewById(R.id.layout_dialog_add_card_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bleSendDialog.dismiss();
            }
        });
        window.findViewById(R.id.layout_dialog_add_card_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String data = etLock.getText().toString();
            writeToBle(data);
            bleSendDialog.dismiss();
            }
        });
    }

    private void writeToBle(String data){
        byte[] hex = data.getBytes();
//        byte[] hex = HexUtil.hexStringToBytes("1234");
        BleManager.getInstance().write(
                bleDevice,
                "0000abf0-0000-1000-8000-00805f9b34fb",
                "0000abf1-0000-1000-8000-00805f9b34fb",
                hex,
                false,
        new BleWriteCallback() {

            @Override
            public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(),"写成功",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onWriteFailure(final BleException exception) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"写失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void bleDialog() {
        bleDialog.show();
        Window window = bleDialog.getWindow();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = bleDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()*0.8); // 设置宽度

//        if(display.getHeight() > 1000){
//            lp.height = 1000;
//        }

        bleDialog.getWindow().setAttributes(lp);
        window.setContentView(R.layout.dialog_ble);

        rvDialogBle = window.findViewById(R.id.dialog_ble_rv);
        rvDialogBleInit();

        window.findViewById(R.id.dialog_ble_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bleDialog.dismiss();
            }
        });
        TextView tvDialogBleConfirm = window.findViewById(R.id.dialog_ble_confirm);
        tvDialogBleConfirm.setText("重新扫描");
        window.findViewById(R.id.dialog_ble_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        checkPermissions();
    }

    private void rvDialogBleInit(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rvDialogBle.setLayoutManager(linearLayoutManager);
        rvDialogBle.setHasFixedSize(true);
        rvDialogBle.setAdapter(deviceAdapter);
        deviceAdapter.setClickItem(new DeviceAdapter.ClickItem() {
            @Override
            public void clickItem(final View view, final int position) {
                bleDevice = deviceAdapter.getScanDevice(position);
                if (!BleManager.getInstance().isConnected(bleDevice)) {
                    BleManager.getInstance().cancelScan();
                    connect(bleDevice);
                }
            }
        });
    }

    //ble 的扫描，权限等
    private void setScanRule() {
        Logger.e(TAG,"设置扫描规则");
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(2000)              // 扫描超时时间，可选，默认10秒
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
    }

    private void startScan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                Logger.e(TAG, "onScanStarted");
                deviceAdapter.clearScanDevice();
                deviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeScan(BleDevice bleDevice) {
                super.onLeScan(bleDevice);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Logger.e(TAG, "scanning");
                if(!TextUtils.isEmpty(bleDevice.getName())) {
                    String temp = bleDevice.getName();
                    String tempa = bleDevice.getDevice().getName();
                    deviceAdapter.addDevice(bleDevice);
                    deviceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Logger.e(TAG, "scan finish");
            }
        });
    }

    private void connect(final BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleException exception) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                deviceAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), getString(R.string.connect_success), Toast.LENGTH_SHORT).show();
                setMtu(bleDevice, 512);
                bleDialog.dismiss();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("BleDevice",bleDevice);
                startActivity(intent);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                if (isActiveDisConnected) {
                    Toast.makeText(getContext(), "成功" + getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "失败" + getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void readRssi(BleDevice bleDevice) {
        BleManager.getInstance().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Logger.i(TAG, "onRssiFailure" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.i(TAG, "onRssiSuccess: " + rssi);
            }
        });
    }

    private void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Logger.e(TAG, "onsetMTUFailure" + exception.toString());
            }

            @Override
            public void onMtuChanged(int mtu) {
                Logger.e(TAG, "onMtuChanged: " + mtu);
            }
        });
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }

    private void checkPermissions() {
        Logger.e(TAG,"check permission");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Logger.e(TAG,"blue is close");
            Toast.makeText(getContext(), getString(R.string.please_open_blue), Toast.LENGTH_LONG).show();
            return;
        }
        Logger.e(TAG,"ble is open");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(getContext(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
            ActivityCompat.requestPermissions(getActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
        }
    }

    private void onPermissionGranted(String permission) {
        Logger.e(TAG,"permission grant ");
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.notifyTitle)
                            .setMessage(R.string.gpsNotifyMsg)
                            .setNegativeButton(R.string.cancel,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Logger.e(TAG,"点击了取消");
//                                            dismiss();
                                        }
                                    })
                            .setPositiveButton(R.string.setting,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Logger.e(TAG,"跳转设置打开GPS");
                                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                        }
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    setScanRule();
                    startScan();
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        Logger.e(TAG,"检查GPS是否打开");
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OPEN_GPS) {
            if (checkGPSIsOpen()) {
                setScanRule();
                startScan();
            }
        }
    }
}
