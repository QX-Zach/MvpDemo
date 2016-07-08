package com.example.ozner.mvpdemo.SysDownload;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ozner.mvpdemo.R;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DownloadTestActivity extends AppCompatActivity {
    private final String apkUrl = "http://wechat.hoyofuwu.com/App_Download/160511117.apk";
    private final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    @InjectView(R.id.tv_downloadPath)
    TextView tvDownloadPath;
    @InjectView(R.id.btn_download)
    Button btnDownload;
    DownloadManager downloadManager;
    long downloadId = -1;
    @InjectView(R.id.tv_status)
    TextView tvStatus;
    @InjectView(R.id.pb_loading)
    ProgressBar pbLoading;
    private DownloadChangeObserver downloadObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_test);
        ButterKnife.inject(this);
        tvDownloadPath.setText(apkUrl);
        init();
        Log.e("tag", "downloadApk_filename:");
    }

    private void init() {
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    }

    @OnClick(R.id.btn_download)
    public void onClick() {
        downloadApk(apkUrl);
    }

    private void downloadApk(String url) {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        Log.e("tag", "downloadApk_filename:" + filename);
        String folderPath = Environment.getExternalStorageDirectory().getPath() + "/Download/";
        File fileDir = new File(folderPath);

        if (!fileDir.exists()) {
            try {
                fileDir.mkdir();
            } catch (Exception ex) {
                Toast.makeText(this, "文件路径不存在", Toast.LENGTH_SHORT).show();
            }
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(fileDir.getAbsolutePath(), "HoYo" + filename);
        request.allowScanningByMediaScanner();
        request.setTitle("浩优测试下载");
        request.setVisibleInDownloadsUi(true);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadId = downloadManager.enqueue(request);
        pbLoading.setProgress(1);
        pbLoading.setVisibility(View.VISIBLE);
        SharedPreferences sPreferences = getSharedPreferences("downloadplato", 0);

        sPreferences.edit().putLong("plato", downloadId).commit();
        downloadObserver = new DownloadChangeObserver(null);
        getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
    }


    private void updateView() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cr = downloadManager.query(query);
        if (cr != null && cr.moveToFirst()) {
            int status = cr.getInt(cr.getColumnIndex(DownloadManager.COLUMN_STATUS));

            int resonIdx = cr.getColumnIndex(DownloadManager.COLUMN_REASON);
            int titleIdx = cr.getColumnIndex(DownloadManager.COLUMN_TITLE);
            int fileSizeIdx = cr.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int bytesDLIdx = cr.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            String title = cr.getString(titleIdx);
            int fileSize = cr.getInt(fileSizeIdx);
            int byteDl = cr.getInt(bytesDLIdx);
            int reason = cr.getInt(resonIdx);
            StringBuilder sb = new StringBuilder();
//            sb.append(title).append("\n");
            sb.append("Downloaded ").append(byteDl).append(" / ").append(fileSize);

            int per = (int) (byteDl / (float) fileSize * 100);
            pbLoading.setProgress(per);
            Log.e("tag", sb.toString());
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.e("tag", "STATUS_PAUSED");
                case DownloadManager.STATUS_PENDING:
                    Log.e("tag", "STATUS_PENDING");
                case DownloadManager.STATUS_RUNNING:
                    //正在下载，不做任何事情
                    Log.e("tag", "STATUS_RUNNING");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //完成
                    Log.e("tag", "下载完成");
//                  dowanloadmanager.remove(lastDownloadId);
                    sb.append("下载完成\n");
                    installAkp(downloadId);
                    break;
                case DownloadManager.STATUS_FAILED:
                    //清除已下载的内容，重新下载
                    Log.e("tag", "STATUS_FAILED");
                    break;
            }

//            tvStatus.setText(sb.toString());
            Message message = new Message();
            message.what = 1;
//            message.arg1 = fileSize;
//            message.arg2 = byteDl;
            message.obj = sb.toString();
            handler.sendMessage(message);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                tvStatus.setText((String) msg.obj);
            }
        }
    };

    private void installAkp(long myDwonloadID) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(myDwonloadID);
        Log.e("tag", "downloadFileUri:" + downloadFileUri);
        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(install);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(downloadObserver);
    }

    class DownloadChangeObserver extends ContentObserver {


        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateView();
        }
    }
}
