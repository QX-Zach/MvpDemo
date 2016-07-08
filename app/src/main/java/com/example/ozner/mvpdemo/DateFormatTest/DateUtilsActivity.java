package com.example.ozner.mvpdemo.DateFormatTest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ozner.mvpdemo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

public class DateUtilsActivity extends AppCompatActivity {

    @InjectView(R.id.et_datestring)
    EditText etDatestring;
    @InjectView(R.id.btn_format)
    Button btnFormat;
    @InjectView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_utils);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.btn_format)
    public void onClick() {
        if (etDatestring.getText().length() > 0) {
            try {
                showFormatDate(Long.parseLong(etDatestring.getText().toString().trim()));
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("tag", "Dateutils_Ex:" + ex.getMessage());
            }
        } else {
            Toast.makeText(this, "输入时间", Toast.LENGTH_SHORT).show();

        }
    }

    public void showFormatDate(long time) {
        StringBuilder sb = new StringBuilder();
        sb.append("DateUtils.FORMAT_ABBREV_ALL:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_ABBREV_ALL));
        sb.append("\n");

        sb.append("DateUtils.FORMAT_ABBREV_MONTH:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_ABBREV_MONTH));
        sb.append("\n");
        sb.append("DateUtils.FORMAT_ABBREV_RELATIVE:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_ABBREV_RELATIVE));
        sb.append("\n");
        sb.append("DateUtils.FORMAT_ABBREV_TIME:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_ABBREV_TIME));
        sb.append("\n");
        sb.append("DateUtils.FORMAT_ABBREV_WEEKDAY:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_ABBREV_WEEKDAY));
        sb.append("\n");
        sb.append("DateUtils.FORMAT_NO_MIDNIGHT:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_NO_MIDNIGHT));
        sb.append("\n");
        sb.append("DateUtils.FORMAT_NO_YEAR:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_NO_YEAR));
        sb.append("\n");

        sb.append("DateUtils.FORMAT_NUMERIC_DATE:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_NUMERIC_DATE));
        sb.append("\n");

        sb.append("DateUtils.FORMAT_SHOW_DATE:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_DATE));
        sb.append("\n");

        sb.append("DateUtils.FORMAT_SHOW_YEAR:");
        sb.append(DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_YEAR));
        sb.append("\n");

        sb.append("formatDateRange.FORMAT_SHOW_YEAR:");
        sb.append(DateUtils.formatDateRange(this, time,time+(1000*3600*35), DateUtils.FORMAT_SHOW_YEAR));
        sb.append("\n");

        sb.append("formatElapsedTime:");
        sb.append(DateUtils.formatElapsedTime(time));
        sb.append("\n");


        sb.append("formatSameDayTime:");
        sb.append(DateUtils.formatSameDayTime(time, time,DateUtils.FORMAT_SHOW_DATE,DateUtils.FORMAT_SHOW_TIME));
        sb.append("\n");


        sb.append("ChatDateUtils:");
        sb.append(ChatDateUtils.getChatTime(time));
        sb.append("\n");



        tvResult.setText(sb.toString());
    }
}
