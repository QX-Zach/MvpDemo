package com.example.ozner.mvpdemo.Buletooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ozner.mvpdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import android.util.Log;

public class BuletoothScanActivity extends AppCompatActivity {

    @InjectView(R.id.btn_sanBuletooth)
    Button btnSanBuletooth;
    @InjectView(R.id.tv_scanresule)
    TextView tvScanresule;
    @InjectView(R.id.btn_showhas)
    Button btnShowhas;

    BluetoothAdapter bluetoothAdapter;
    List<BluetoothDevice> deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buletooth_scan);
        ButterKnife.inject(this);


        deviceList = new ArrayList<>();
        //得到蓝牙对象
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            }
        });

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(new BluetoothReceiver(), intentFilter);
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.e("tag", "蓝牙低功耗");
        }
    }

    @OnClick({R.id.btn_showhas, R.id.btn_sanBuletooth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_showhas:
                showHasBluetooth();
                break;
            case R.id.btn_sanBuletooth:
                scanDevice();
                break;
        }
    }

    private void scanDevice() {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            }
            deviceList.clear();
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();
        }

    }

    private void showHasBluetooth() {

        StringBuilder stringBuilder = new StringBuilder();
        //判断蓝牙对象是否为空，如果为空则表明没有蓝牙设备
        if (bluetoothAdapter != null) {
            System.out.print("本机拥有蓝牙设备");
            stringBuilder.append("已连接蓝牙设备\n");
            if (!bluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivity(intent);
            }
            Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
            if (devices.size() > 0) {
                for (BluetoothDevice device : devices) {
                    stringBuilder.append("蓝牙:mac:");
                    stringBuilder.append(device.getAddress());
                    stringBuilder.append("，名字：");
                    stringBuilder.append(device.getName());
                    stringBuilder.append("，类型：");
                    stringBuilder.append(device.getType());
                }
            }
        } else {
            stringBuilder.append("没有蓝牙设备");
        }
        tvScanresule.setText(stringBuilder.toString());
    }

    private void showScanedDevice(List<BluetoothDevice> devices) {
        StringBuilder sb = new StringBuilder();
        sb.append("扫描到的设备\n");
        for (BluetoothDevice device : devices) {
            sb.append("蓝牙：mac：");
            sb.append(device.getAddress());
            sb.append("，名称：");
            sb.append(device.getName());
            sb.append("，类型：");
            sb.append(device.getType());
            sb.append("\n");
        }
        tvScanresule.setText(sb.toString());
    }

    private class BluetoothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                //Intent代表刚刚发现远程蓝牙设备适配器的对象,可以从收到的Intent对象取出一些信息
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("tag", "蓝牙:mac:" + bluetoothDevice.getAddress() + " , 名称：" + bluetoothDevice.getName());
                deviceList.add(bluetoothDevice);
                showScanedDevice(deviceList);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothAdapter = null;
    }
}
