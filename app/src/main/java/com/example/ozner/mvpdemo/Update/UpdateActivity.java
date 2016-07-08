package com.example.ozner.mvpdemo.Update;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ozner.mvpdemo.R;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UpdateActivity";
    Button btn_cancle;
    String downloadUrl;
    String updateCon;
    String installPackageName;
    boolean isMustUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_cancle.setOnClickListener(this);

//        bundle.putString("downloadurl", downLoadUrl);
//        bundle.putInt("mustupdate", isMustUpdate);
//        bundle.putString("updatecon", "updatecon");
//        bundle.putString("installPackageName", instalPackageName);
        try {
            Bundle bundle = getIntent().getExtras();
            downloadUrl = bundle.getString("downloadurl");
            updateCon = bundle.getString("updatecon");
            installPackageName = bundle.getString("installPackageName");
            isMustUpdate = bundle.getInt("mustupdate") == 1 ? true : false;
            Log.e(TAG, "onCreate: isMustUpdate:" + isMustUpdate
                    + "\tUrl:" + downloadUrl + "\nName:" + installPackageName
                    + "\nupdateCon:" + updateCon);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "onCreate_Ex:" + ex.getMessage());
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
}
