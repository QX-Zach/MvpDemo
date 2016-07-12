package com.example.ozner.mvpdemo.Update;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ozner.mvpdemo.R;
import com.example.ozner.mvpdemo.SysDownload.DownLoadBroadcastReceiver;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UpdateActivity";
    private final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    Button btn_cancle;
    String downloadUrl;
    String updateCon;
    String folerPath = "";
    String installPackageName;
    boolean isMustUpdate;
    DownloadManager downloadManager = null;
    DowndLoadReceiver receiver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        receiver = new DowndLoadReceiver();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        registerReceiver(receiver, filter);
        folerPath = Environment.getExternalStorageDirectory() + "/Download/";
        try {
            Bundle bundle = getIntent().getExtras();
            downloadUrl = bundle.getString("downloadurl");
            updateCon = bundle.getString("updatecon");
            installPackageName = bundle.getString("installPackageName");
            isMustUpdate = bundle.getInt("mustupdate") == 1 ? true : false;
            Log.e(TAG, "onCreate: isMustUpdate:" + isMustUpdate
                    + "\tUrl:" + downloadUrl + "\nName:" + installPackageName
                    + "\nupdateCon:" + updateCon);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                downloadApk(downloadUrl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "onCreate_Ex:" + ex.getMessage());
        }
    }

    private void downloadApk(String uri) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(uri));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle(getString(R.string.app_name));
        request.setVisibleInDownloadsUi(true);
        request.setMimeType("application/vnd.android.package-archive");
//        request.setDestinationInExternalPublicDir("Download", installPackageName);
        request.setDestinationInExternalPublicDir(folerPath, installPackageName);
        //指定在WIFI状态下，执行下载操作。
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadApk(downloadUrl);
            } else {
                Toast.makeText(UpdateActivity.this, "用户拒绝了权限", Toast.LENGTH_SHORT).show();
                UpdateActivity.this.finish();
            }
        }
    }

    class DowndLoadReceiver extends DownLoadBroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            Log.e(TAG, "onReceive: Action:" + intent.getAction());
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.e(TAG, "onReceive: 下载完成！downloadID:" + downloadId);
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.e(TAG, "onReceive: MimeType:" + downloadManager.getMimeTypeForDownloadedFile(downloadId));
                Log.e(TAG, "onReceive: fileUri:" + downloadManager.getUriForDownloadedFile(downloadId));
                Log.e(TAG, "onReceive: filePath:" + Uri.parse("file://" + folerPath + installPackageName));
                installIntent.setDataAndType(Uri.parse("file://" + folerPath + installPackageName), downloadManager.getMimeTypeForDownloadedFile(downloadId));
                if (installIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(installIntent);
                }
                UpdateActivity.this.finish();
            }
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
                this.finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
    }
}
