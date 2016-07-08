package com.example.ozner.mvpdemo.Buletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ozner.mvpdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BluetoothScan_2_Activity extends AppCompatActivity {

    private static final long SCAN_PERIOD = 30000;
    private final int FOUND_DEVICE = 0X01;
    private final int SCAN_END = 0X02;
    private boolean mScanning = false;
    private BluetoothAdapter mBluetoothAdapter;
    private MyLeScanCallback myLeScanCallback;
    HashMap<String, BluetoothDevice> deviceMap = new HashMap<>();
    @InjectView(R.id.btn_scanBluetooth)
    Button btnScanBluetooth;
    @InjectView(R.id.tv_scanResult)
    TextView tvScanResult;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FOUND_DEVICE:
                    StringBuilder sb = new StringBuilder();
                    sb.append("开始扫描：");
                    sb.append("\n");

                    for (String address : deviceMap.keySet()) {
                        BluetoothDevice device = deviceMap.get(address);
                        sb.append("蓝牙：mac:");
                        sb.append(device.getAddress());
                        sb.append("，名称:");
                        sb.append(device.getName());
                        sb.append("，类型：");
                        sb.append(device.getType());
                        sb.append("\n");
                    }
                    tvScanResult.setText(sb.toString());
                    break;
                case SCAN_END:
                    tvScanResult.setText(tvScanResult.getText() + "\n扫描结束！");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scan_2_);
        ButterKnife.inject(this);
        mBluetoothAdapter = ((BluetoothManager) getSystemService(BLUETOOTH_SERVICE)).getAdapter();
        myLeScanCallback = new MyLeScanCallback();
        if (mBluetoothAdapter != null || !mBluetoothAdapter.isEnabled()) {
            Intent enalbeBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enalbeBtIntent);
        }
    }

    @OnClick({R.id.btn_scanBluetooth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_scanBluetooth:
                scanLeDevice(true);
                break;

        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            deviceMap.clear();
            mBluetoothAdapter.stopLeScan(myLeScanCallback);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(myLeScanCallback);
                    mHandler.sendEmptyMessage(SCAN_END);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(myLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(myLeScanCallback);
        }
    }

    private class MyLeScanCallback implements BluetoothAdapter.LeScanCallback {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!deviceMap.containsKey(device.getAddress())) {
                        deviceMap.put(device.getAddress(), device);
                        mHandler.sendEmptyMessage(FOUND_DEVICE);
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.stopLeScan(myLeScanCallback);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter = null;
        }
    }
}
