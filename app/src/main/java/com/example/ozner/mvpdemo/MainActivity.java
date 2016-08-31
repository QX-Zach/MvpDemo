package com.example.ozner.mvpdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ozner.mvpdemo.Buletooth.BluetoothScan_2_Activity;
import com.example.ozner.mvpdemo.Buletooth.BluetoothUUIDActivity;
import com.example.ozner.mvpdemo.Buletooth.BuletoothScanActivity;
import com.example.ozner.mvpdemo.DateFormatTest.DateUtilsActivity;
import com.example.ozner.mvpdemo.Login.LoginActivity;
import com.example.ozner.mvpdemo.SwipeMenu.SwipeMenu_1Activity;
import com.example.ozner.mvpdemo.SysDownload.DownloadTestActivity;
import com.example.ozner.mvpdemo.ViewTest.BoardActivity;
import com.example.ozner.mvpdemo.ViewTest.FlipViewActivity;
import com.example.ozner.mvpdemo.W3CSchool.LightingColorFilterActivity;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    @InjectView(R.id.btn_mvpTest)
    Button btnMvpTest;
    @InjectView(R.id.btn_boardTest)
    Button btnBoardTest;
    @InjectView(R.id.btn_flipView)
    Button btnFlipView;
    @InjectView(R.id.btn_encodeUrl)
    Button btnEncodeUrl;
    @InjectView(R.id.btn_buletooth)
    Button btnBuletooth;
    @InjectView(R.id.btn_scanBlueTest)
    Button btnScanBlueTest;
    @InjectView(R.id.btn_dateUtils)
    Button btnDateUtils;
    @InjectView(R.id.btn_perCheck)
    Button btnPerCheck;
    @InjectView(R.id.btn_lightingColor)
    Button btnLightingColor;
    @InjectView(R.id.btn_slip_list)
    Button btnSlipList;
    @InjectView(R.id.btn_downloadFile)
    Button btnDownloadFile;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.btn_filter)
    Button btnFilter;
    @InjectView(R.id.btn_checkLanguage)
    Button btnCheckLanguage;
    @InjectView(R.id.btn_btUUID)
    Button btnBtUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setListener();
        //this is test
        //this is second test
    }

    public void setListener() {
        btnMvpTest.setOnClickListener(this);
        btnBoardTest.setOnClickListener(this);
        btnFlipView.setOnClickListener(this);
        btnEncodeUrl.setOnClickListener(this);
        btnScanBlueTest.setOnClickListener(this);
        btnBuletooth.setOnClickListener(this);
        btnDateUtils.setOnClickListener(this);
        btnPerCheck.setOnClickListener(this);
        btnLightingColor.setOnClickListener(this);
        btnSlipList.setOnClickListener(this);
        btnDownloadFile.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnFilter.setOnClickListener(this);
        btnCheckLanguage.setOnClickListener(this);
        btnBtUUID.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_downloadFile:
                startActivity(new Intent(this, DownloadTestActivity.class));
                break;
            case R.id.btn_mvpTest:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btn_boardTest:
                startActivity(new Intent(this, BoardActivity.class));
                break;
            case R.id.btn_flipView:
                startActivity(new Intent(this, FlipViewActivity.class));
                break;
            case R.id.btn_encodeUrl:

                break;
            case R.id.btn_slip_list:
                startActivity(new Intent(this, SwipeMenu_1Activity.class));
                break;
            case R.id.btn_buletooth:
                startActivity(new Intent(this, BuletoothScanActivity.class));
                break;
            case R.id.btn_scanBlueTest:
                startActivity(new Intent(this, BluetoothScan_2_Activity.class));
                break;
            case R.id.btn_dateUtils:
                startActivity(new Intent(this, DateUtilsActivity.class));
                break;
            case R.id.btn_perCheck:
                startActivity(new Intent(this, PermissTestActivity.class));
                break;
            case R.id.btn_lightingColor:
                startActivity(new Intent(this, LightingColorFilterActivity.class));
                break;
            case R.id.btn_login:
//                Call<String> call = BaseApplication.getApiService().login("13166398575","123321");
                Call<String> call = BaseApplication.getApiService().getBaiDu();
                call.enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.e("tag", "resopnse:" + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("tag", "resopnse_fail:" + t.getMessage());
                    }
                });
                break;
            case R.id.btn_filter:
                Intent intent = new Intent(this, FilterProgressActivity.class);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Log.e(TAG, "onClick: btn_filter:Activity Not Found");
                    Toast.makeText(MainActivity.this, "Application not exits", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_checkLanguage:
                if (isLanguageCN()) {
                    Toast.makeText(MainActivity.this, "中文", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "其他语言", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_btUUID:
                Intent blueUUIDIntent = new Intent(this, BluetoothUUIDActivity.class);
                if(blueUUIDIntent.resolveActivity(getPackageManager())!=null){
                    startActivity(blueUUIDIntent);
                }else {
                    Log.e(TAG, "onClick: btn_btUUID:Activity Not Found");
                    Toast.makeText(MainActivity.this, "Application not exits", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean isZh() {
        Locale local = getResources().getConfiguration().locale;
        Log.w(TAG, "isZh: Country:" + local.getCountry());
        Log.w(TAG, "isZh: DisplayName:" + local.getDisplayName());
        Log.w(TAG, "isZh: Language:" + local.getLanguage());
        Log.w(TAG, "isZh: DisplayLanguage:" + local.getDisplayLanguage());
        if (local.getLanguage().endsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isLanguageCN() {
        if (Locale.getDefault().getLanguage().endsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }


//    @OnClick({R.id.btn_buletooth, R.id.btn_scanBlueTest})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_buletooth:
//                startActivity(new Intent(this, BuletoothScanActivity.class));
//                break;
//        }
//    }

}
