package com.example.ozner.mvpdemo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.ozner.mvpdemo.UIView.FilterProgressView;

import java.util.Calendar;
import java.util.Date;

public class FilterProgressActivity extends AppCompatActivity {
    FilterProgressView fpv_filter;
    ProgressBar progressBar1;
    Button btn_add, btn_minus;
    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_progress);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date starDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 3);
        Date endDate = calendar.getTime();

        fpv_filter = (FilterProgressView) findViewById(R.id.fpv_filter);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_minus = (Button) findViewById(R.id.btn_minus);
        fpv_filter.initTime(starDate, endDate);
        fpv_filter.setThumb(R.drawable.filter_status_thumb);
        fpv_filter.update(new Date());
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = progressBar1.getProgress();
                if (value <= 95) {
                    value += 5;
                }
                progressBar1.setProgress(value);
            }
        });
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = progressBar1.getProgress();
                if (value >= 5) {
                    value -= 5;
                }
                progressBar1.setProgress(value);
            }
        });
    }
}
