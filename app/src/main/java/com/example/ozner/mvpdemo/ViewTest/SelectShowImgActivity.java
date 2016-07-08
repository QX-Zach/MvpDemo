package com.example.ozner.mvpdemo.ViewTest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;

import com.example.ozner.mvpdemo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SelectShowImgActivity extends AppCompatActivity {

    @InjectView(R.id.img_choose)
    ImageView imgChoose;
    @InjectView(R.id.btn_choose)
    Button btnChoose;
    @InjectView(R.id.gay_choose)
    Gallery gayChoose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_show_img);
        ButterKnife.inject(this);
    }
}
