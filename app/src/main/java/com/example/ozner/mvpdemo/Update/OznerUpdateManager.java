package com.example.ozner.mvpdemo.Update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ozner.mvpdemo.R;
import com.example.ozner.mvpdemo.Update.HttpHelper.HoYoDataHttp;
import com.example.ozner.mvpdemo.Update.HttpHelper.bean.NetJsonObject;
import com.example.ozner.mvpdemo.Utils.FileUtils;
import com.example.ozner.mvpdemo.Utils.LogUtilsLC;
import com.example.ozner.mvpdemo.Utils.MessageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by ozner_67 on 2016/5/17.
 */
public class OznerUpdateManager {
    private static final String TAG = "tag";
//    private final String testdownloadUrl = "http://wechat.hoyofuwu.com/App_Download/160513118.apk";

    private final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
    /*检查网络APK版本*/
    private static final int CHECK_NET_VERSON = 1;
    /*更新下载进度*/
    private static final int UPDATE_UI = 2;
    private static final int INSTALL_APK = 3;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 4;
    long downloadId = -1;//下载任务id
    private int isMustUpdate = 0;//是否必须更新
    private int netVersionCode = 0;//网络软件版本号
    private String downLoadUrl = "";//下载链接
    private String folderPath = "";//文件下载路径
    private String instalPackageName = "";//下载的文件名
    private Context mContext;
    private boolean isShowMsg = false;
    private CheckVersionListener checkListener;
    DownloadManager downloadManager;
    private DownloadChangeObserver downloadObserver;
    /* 更新进度条 */
    private ProgressBar mProgress;
    private TextView tv_totalLen, tv_readLen, tv_loadper;
    private Dialog mDownloadDialog;
    private AlertDialog.Builder builder;
    private boolean isInstalling = false;
    private boolean isChecking = false;

    public OznerUpdateManager(Context context, boolean isShowToastMsg) {
        this.mContext = context;
        this.isShowMsg = isShowToastMsg;
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        this.folderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
//        this.folderPath = Environment.getExternalStorageDirectory().getPath() + "/Download/";
        this.folderPath = Environment.getExternalStorageDirectory() + "/Download/";
        Log.e(TAG, "OznerUpdateManager: folderPath:" + folderPath);
    }

    public interface CheckVersionListener {
        public void CheckVersionState(boolean isCheckComplete);
    }

    public void setCheckVersionListener(CheckVersionListener listener) {
        this.checkListener = listener;
    }

    /*
    *检测软件更新
     */
    public void checkUpdate() {
        if (checkListener != null) {
            checkListener.CheckVersionState(false);
        }
//        showNoticeDialog("");
        if (!isChecking) {
            getNetVerson();
        } else {
            Toast.makeText(mContext, "正在检查更新", Toast.LENGTH_SHORT).show();
        }
    }

    private void getNetVerson() {
        isChecking = true;
        final String checkVerUrl = HoYoPreference.ServerAddress(mContext) + "/Command/NewVersion";
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NameValuePair> pars = new ArrayList<NameValuePair>();
                pars.add(new BasicNameValuePair("code", String.valueOf(getVersionCode(mContext))));
                pars.add(new BasicNameValuePair("os", "android"));
//                pars.add(new BasicNameValuePair("appname", mContext.getPackageName()));
                pars.add(new BasicNameValuePair("appname", "com.hoyo.ozner.hoyoproject"));
                NetJsonObject result = HoYoDataHttp.HoYoWebServer(mContext, checkVerUrl, pars);
                Message message = new Message();
                message.what = CHECK_NET_VERSON;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 获取软件版本号
     *
     * @param context
     * @return
     */
    private int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = context.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            if (LogUtilsLC.APP_DBG)
                Log.e("tag", "updateManage:curVersion: " + versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 显示软件更新对话框
     */
    private void showNoticeDialog(String updatemsg) {
        if (checkListener != null) {
            checkListener.CheckVersionState(true);
        }
        // 构造对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_update_title);
        builder.setMessage(R.string.soft_update_info);
        View view = LayoutInflater.from(mContext).inflate(R.layout.upload_dialog, null);
        builder.setView(view);
        if (updatemsg != null && updatemsg.length() > 0) {
            ((LinearLayout) view.findViewById(R.id.llay_content)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.tv_content)).setText(updatemsg);
        }
        // 更新
        builder.setPositiveButton(R.string.soft_update_updatebtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                showDownloadDialog();
            }
        });

        if (0 == isMustUpdate) {
            // 稍后更新
            builder.setNegativeButton(R.string.soft_update_later, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (checkListener != null) {
                        checkListener.CheckVersionState(true);
                    }
                    dialog.dismiss();
                }
            });
        }
        Dialog noticeDialog = builder.create();
        if (1 == isMustUpdate) {
            noticeDialog.setCanceledOnTouchOutside(false);
            noticeDialog.setCancelable(false);
        }
        try {
            if (mContext != null) {
                noticeDialog.show();
            }
        } catch (Exception ex) {

        }
    }

    /**
     * 显示软件下载对话框
     */
    private void showDownloadDialog() {
        Log.e(TAG, "showDownloadDialog: 开始下载");
        // 构造软件下载对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(R.string.soft_updating);
        // 给下载对话框增加进度条
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.softupdate_progress, null);
        mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
        tv_totalLen = (TextView) v.findViewById(R.id.tv_totalLen);
        tv_readLen = (TextView) v.findViewById(R.id.tv_readLen);
        tv_loadper = (TextView) v.findViewById(R.id.tv_loadper);
        builder.setView(v);
        mDownloadDialog = builder.create();
        mDownloadDialog.setCanceledOnTouchOutside(false);
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.show();
        // 下载文件
        downloadApk();
    }

    /*
    *检查是否拥有权限
     */
    private boolean hasPermission(@NonNull String permission) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            return false;
        } else {
            return true;
        }
    }


    /*
    *下载文件
     */
    public void downloadApk() {
        Log.e(TAG, "downloadApk");
        try {
            File filerDir = new File(folderPath);
            if (!filerDir.exists()) {
                try {
                    filerDir.mkdir();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (filerDir.exists()) {
                Log.e(TAG, "downloadApk: fileDir exists");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downLoadUrl));
                request.setMimeType("application/vnd.android.package-archive");
                request.setDestinationInExternalPublicDir(filerDir.getAbsolutePath(), instalPackageName);
//            request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, instalPackageName);
                request.allowScanningByMediaScanner();
                request.setTitle(mContext.getString(R.string.app_name));
                request.setVisibleInDownloadsUi(true);

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                downloadId = downloadManager.enqueue(request);
                downloadObserver = new DownloadChangeObserver(null);
                mContext.getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            LogUtilsLC.E("tag", "下载更新：" + ex.getMessage());
            if (checkListener != null) {
                checkListener.CheckVersionState(true);
            }
        }
    }


    //更新下载进度
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
                    sb.append("下载完成\n");
                    if (checkListener != null) {
                        checkListener.CheckVersionState(true);
                    }
                    mDownloadDialog.cancel();
                    if (!isInstalling) {
                        isInstalling = true;
                        installAkp(downloadId);
//                        sixInstallAPK(mContext);
                    }
                    break;
                case DownloadManager.STATUS_FAILED:
                    //清除已下载的内容，重新下载
                    Log.e("tag", "STATUS_FAILED");
                    if (checkListener != null) {
                        checkListener.CheckVersionState(true);
                    }
                    mDownloadDialog.cancel();
                    break;
            }
            int per = (int) ((float) byteDl / fileSize) * 100;
            Message message = new Message();
            message.what = UPDATE_UI;
            message.arg1 = byteDl;
            message.arg2 = fileSize;
            mHandler.sendMessage(message);
            if (LogUtilsLC.APP_DBG) {
                LogUtilsLC.E("tag", "下载进度：" + sb.toString());
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    /*
    *安装下载的APK
     */
    private void installAkp(long myDwonloadID) {
        Log.e(TAG, "installAkp");
//        try {
//            downloadManager.openDownloadedFile(downloadId);
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//            Toast.makeText(mContext, "没有找到安装文件", Toast.LENGTH_SHORT).show();
//        }

//        Message message = mHandler.obtainMessage();
//        message.what = INSTALL_APK;
        mHandler.sendEmptyMessage(INSTALL_APK);
//        final Uri downloadFileUri = downloadManager.getUriForDownloadedFile(myDwonloadID);
//        String mimeType = downloadManager.getMimeTypeForDownloadedFile(myDwonloadID);
//
//
//        Log.e("tag", "downloadFileUri:" + downloadFileUri);
//        if (Build.VERSION.SDK_INT < 23) {
//            Log.e("tag", "installAkp_version: 23以下");
//            Intent install = new Intent(Intent.ACTION_VIEW);
//            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mContext.startActivity(install);
//        } else {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    MessageHelper.showMsgDialog(mContext, "新的版本下载完成，请手动安装");
//                    Intent installInent = new Intent(Intent.ACTION_PACKAGE_ADDED, downloadFileUri);
//                    mContext.startActivity(installInent);
//                }
//            });
//
//        }
    }


    private void sixInstallAPK(final Context context) {
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
        String mimeType = downloadManager.getMimeTypeForDownloadedFile(downloadId);
        Log.e(TAG, "sixInstallAPK: mimeType:" + mimeType);
        Log.e(TAG, "sixInstallAPK: realPath:" + FileUtils.getRealPath(context, downloadManager.getUriForDownloadedFile(downloadId)));
        Log.e(TAG, "sixInstallAPK: downloadFileUri:" + downloadFileUri);

        final Intent oepnIntent = new Intent(Intent.ACTION_VIEW);
        oepnIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        oepnIntent.setData(downloadFileUri);

        try {
            context.startActivity(oepnIntent);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "sixInstallAPK: 没有找到打开此类文件的程序");
        }
    }

    public void openFile(File var0, Context var1) {
        Intent var2 = new Intent();
        var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        var2.setAction("android.intent.action.VIEW");
        var2.addCategory("android.intent.category.DEFAULT");
        String var3 = getMIMEType(var0);
        var2.setDataAndType(Uri.fromFile(var0), var3);
        try {
            var1.startActivity(var2);
        } catch (Exception var5) {
            var5.printStackTrace();
            Toast.makeText(var1, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
        }

    }

    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //检查版本
                case CHECK_NET_VERSON:

                    NetJsonObject result = (NetJsonObject) msg.obj;
                    if (result != null) {
                        if (result.state > 0) {
                            if (LogUtilsLC.APP_DBG)
                                Log.e("tag", "UpdateManager:" + result.value);
                            try {
                                JSONObject data = result.getJSONObject().getJSONObject("data");
                                downLoadUrl = data.getString("downloadurl");
                                isMustUpdate = data.getInt("mustupdate");
                                netVersionCode = data.getInt("versioncode");
                                String updateCon = data.getString("updatecon");
                                instalPackageName = downLoadUrl.substring(downLoadUrl.lastIndexOf("/") + 1);

                                if (netVersionCode > getVersionCode(mContext)) {
                                    showNoticeDialog(updateCon);
                                } else {
                                    if (isShowMsg) {
                                        MessageHelper.showToastCenter(mContext, mContext.getString(R.string.soft_update_no));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                if (LogUtilsLC.APP_DBG)
                                    Log.e("tag", "update_Ex:" + e.getMessage());
                                if (isShowMsg)
                                    MessageHelper.showToastCenter(mContext, "数据解析异常");
                            }
                        } else {
                            if (LogUtilsLC.APP_DBG)
                                Log.e("tag", "update:" + result.value);
                            if (isShowMsg) {
//                                MessageHelper.showToastCenter(mContext, NetErrDecode.getErrMsg(result.state));
                                MessageHelper.showToastCenter(mContext, mContext.getString(R.string.soft_update_no));
                            }
                        }

                    } else {
                        if (LogUtilsLC.APP_DBG)
                            Log.e("tag", "update:retrn null");
                        if (isShowMsg)
                            MessageHelper.showToastCenter(mContext, mContext.getString(R.string.soft_update_check_err));
                    }
                    if (checkListener != null) {
                        checkListener.CheckVersionState(true);
                    }

                    isChecking = false;
                    break;
                case UPDATE_UI:
//                    if (LogUtilsLC.APP_DBG)
//                        Log.e("tag", "UPDATE_UI:" + msg.arg1 + "/" + msg.arg2);
                    int readlen = msg.arg1;
                    int totallen = msg.arg2;
                    int per = (int) (((float) readlen / totallen) * 100);
                    tv_loadper.setText(per + "%");
                    break;
                case INSTALL_APK:
                    Uri downloadFileUri = downloadManager.getUriForDownloadedFile(downloadId);
                    String mimeType = downloadManager.getMimeTypeForDownloadedFile(downloadId);


                    Log.e("tag", "downloadFileUri:" + downloadFileUri);
                    if (Build.VERSION.SDK_INT < 23) {
                        Log.e("tag", "installAkp_version: 23以下");
                        Log.e(TAG, "handleMessage: Install apk");
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(install);
                    } else {
                        Log.e(TAG, "handleMessage:23以上");
//                        MessageHelper.showMsgDialog(mContext, "新的版本下载完成，请手动安装");
//                        Intent installInent = new Intent(Intent.ACTION_PACKAGE_ADDED, downloadFileUri);
//                        mContext.startActivity(installInent);
                        Intent install23 = new Intent(Intent.ACTION_VIEW);
                        install23.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                        install23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(install23);
                    }
                    break;
            }
        }
    };

    @Override
    protected void finalize() throws Throwable {
//        mContext.getContentResolver().unregisterContentObserver(downloadObserver);
        super.finalize();
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
