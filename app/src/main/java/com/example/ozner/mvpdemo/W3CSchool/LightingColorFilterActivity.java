package com.example.ozner.mvpdemo.W3CSchool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ozner.mvpdemo.R;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LightingColorFilterActivity extends AppCompatActivity {

    @InjectView(R.id.iv_image)
    ImageView ivImage;
    @InjectView(R.id.et_mul)
    EditText etMul;
    @InjectView(R.id.et_add)
    EditText etAdd;
    @InjectView(R.id.btn_change)
    Button btnChange;

    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighting_color_filter);
        ButterKnife.inject(this);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_1);
//        PorterDuff
    }

    @OnClick(R.id.btn_change)
    public void onClick() {
        int mul = Integer.parseInt(etMul.getText().toString());
        int add = Integer.parseInt(etAdd.getText().toString());
        ivImage.setImageBitmap(processImage(mBitmap, mul, add));
    }

    private Bitmap processImage(Bitmap bp, int mul, int add) {
        Bitmap bitmap = Bitmap.createBitmap(bp.getWidth(), bp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new LightingColorFilter(mul, add));
        canvas.drawBitmap(bp, 0, 0, paint);
        return bitmap;
    }
}
