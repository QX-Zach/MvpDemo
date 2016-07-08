package com.example.ozner.mvpdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PermissTestActivity extends AppCompatActivity {
    final int PerCameraRequestCode = 0x01;

    @InjectView(R.id.btn_checkPermission)
    Button btnCheckPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permiss_test);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_checkPermission)
    public void onClick() {
        //检查是否有相机权限
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},PerCameraRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(PermissTestActivity.this,"获取权限：成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(PermissTestActivity.this,"获取权限：失败",Toast.LENGTH_SHORT).show();
        }

    }
}
