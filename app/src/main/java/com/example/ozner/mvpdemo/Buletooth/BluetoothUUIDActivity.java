package com.example.ozner.mvpdemo.Buletooth;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ozner.mvpdemo.R;
import com.example.ozner.mvpdemo.Update.CheckVersionCodeUtil;
import com.example.ozner.mvpdemo.Update.HttpHelper.bean.NetJsonObject;
import com.example.ozner.mvpdemo.Update.OznerUpdateManager;
import com.example.ozner.mvpdemo.Utils.LogUtilsLC;
import com.example.ozner.mvpdemo.Utils.MessageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class BluetoothUUIDActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "tag";
    private final int CheckVersionCode = 0x01;
    private static final int ServiceID = 0xfff0;
    private static final int Input = 0xfff2;
    private static final int Output = 0xfff1;
    private static final int CFGClient = 0x2902;
    Button btn_checkUpdate, btn_newUpdate;
    private boolean isChecking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_uuid);
        Log.e(TAG, "onCreate: ServiceID ---> UUID:" + getUUID(ServiceID).toString());
        Log.e(TAG, "onCreate: Input ---> UUID:" + getUUID(Input).toString());
        Log.e(TAG, "onCreate: Output ---> UUID:" + getUUID(Output).toString());
        Log.e(TAG, "onCreate: CFGClient ---> UUID:" + getUUID(CFGClient).toString());
        btn_checkUpdate = (Button) findViewById(R.id.btn_checkUpdate);
        btn_newUpdate = (Button) findViewById(R.id.btn_newUpdate);
        btn_checkUpdate.setOnClickListener(this);
        btn_newUpdate.setOnClickListener(this);
    }

    private UUID getUUID(int id) {
        return UUID.fromString(String.format(
                "%1$08x-0000-1000-8000-00805f9b34fb", id));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_checkUpdate:
                new OznerUpdateManager(BluetoothUUIDActivity.this, true).checkUpdate();
                break;
            case R.id.btn_newUpdate:
                if (!isChecking) {
                    isChecking = true;
                    CheckVersionCodeUtil.CheckNetVersion(BluetoothUUIDActivity.this, mHandler, CheckVersionCode);
                } else {
                    Toast.makeText(BluetoothUUIDActivity.this, "正在检查更新", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CheckVersionCode:
                    NetJsonObject result = (NetJsonObject) msg.obj;
                    if (result != null) {
                        if (result.state > 0) {
                            if (LogUtilsLC.APP_DBG)
                                Log.e("tag", "UpdateManager:" + result.value);
                            try {
                                JSONObject data = result.getJSONObject().getJSONObject("data");
                                String downLoadUrl = data.getString("downloadurl");
                                int isMustUpdate = data.getInt("mustupdate");
                                int netVersionCode = data.getInt("versioncode");
                                String updateCon = data.getString("updatecon");
                                String instalPackageName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/") + 1);

                                if (netVersionCode > CheckVersionCodeUtil.getVersionCode(BluetoothUUIDActivity.this)) {
//                                    showNoticeDialog(updateCon);
                                    MessageHelper.showMsgDialog(BluetoothUUIDActivity.this, "有新版本");
                                } else {
                                    MessageHelper.showToastCenter(BluetoothUUIDActivity.this, getString(R.string.soft_update_no));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (LogUtilsLC.APP_DBG)
                                    Log.e("tag", "update_Ex:" + e.getMessage());

                                MessageHelper.showToastCenter(BluetoothUUIDActivity.this, "数据解析异常");
                            }
                        } else {
                            if (LogUtilsLC.APP_DBG)
                                Log.e("tag", "update:" + result.value);
                            MessageHelper.showToastCenter(BluetoothUUIDActivity.this, getString(R.string.soft_update_no));
                        }
                    } else {
                        if (LogUtilsLC.APP_DBG)
                            Log.e("tag", "update:retrn null");
                        MessageHelper.showToastCenter(BluetoothUUIDActivity.this, getString(R.string.soft_update_check_err));
                    }
                    isChecking = false;
                    break;
            }
        }
    };
}
