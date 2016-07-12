package com.example.ozner.mvpdemo.Update;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ozner.mvpdemo.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.ut.vrautocyclingcontroller.utils.UpdateVersionManager.java
 * @author: xutailian
 * @date: 2016-06-16 16:00
 * @describe :版本更新
 */
public class UpdateVersionManager {
    private Context mContext;
    //提示语
    private String updateMsg ="有最新的软件包哦~，快下载吧~";
    //返回的安装包
    private String apkUrl = "";

    private Dialog noticeDialog;
    private Dialog downloadDialog;


    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/updatedemo/";

    private static final String saveFileName = savePath + "youtu.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;


    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    System.out.println("下载完成");
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };

    public UpdateVersionManager(Context context,String url) {
        this.mContext = context;
        this.apkUrl = url;
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo(){
        showNoticeDialog();
    }


    private void showNoticeDialog(){
        //	AlertDialog.Builder builder = new Builder(mContext);
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void showDownloadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("软件版本更新");

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.progress, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.i("Test", "============================" + apkUrl);
                URL url = new URL(apkUrl);
//				URL url = new URL("http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk");

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if(!file.exists()){
                    file.mkdir();
                }
                //String apkFile = saveFileName;
                String fileName = "youtu.apk";
                File ApkFile = new File(file,fileName);
                ApkFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(ApkFile);

                BufferedInputStream bis = new BufferedInputStream(is);

                int count = 0;
                byte buf[] = new byte[1024];

                do{
                    int numread = bis.read(buf);
                    count += numread;
                    progress =(int)(((float)count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if(numread <= 0){
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf,0,numread);
                    fos.flush();
                }while(!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     * @param
     */

    private void downloadApk(){
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }
    /**
     * 安装apk
     * @param
     */
    private void installApk(){
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        Log.e("tag", "installApk: Slience:"+Uri.parse("file://" + apkfile.toString()) );
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }
}
