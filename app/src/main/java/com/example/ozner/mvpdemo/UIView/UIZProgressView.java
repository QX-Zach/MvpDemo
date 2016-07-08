package com.example.ozner.mvpdemo.UIView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.ozner.mvpdemo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Created by ozner_67 on 2016/6/3.
 */
public class UIZProgressView extends View {
    private static int startColor = 0xff3387f9;
    private static int centerColor = 0xff6b3ee1;
    private static int endColor = 0xfff83636;
    private static int textColor = 0xff3387f9;
    private static int lineColor = 0xffc9c9c9;
    private Paint bgPaint, valuePaint;
    private int linwidth = 10;
    private int value = 0;
    private int max = 100;
    private Bitmap thumb = null;

    public UIZProgressView(Context context) {
        super(context, null);
    }

    public UIZProgressView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public UIZProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.UIZProgressBar, defStyleAttr, 0);
        max = a.getInt(R.styleable.UIZProgressBar_max, 100);
        thumb = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.UIZProgressBar_thumb, R.drawable.filter_status_thumb));
        a.recycle();
    }


    //更新保修时间
    public void updateValue(int value) {
        this.value = value;
        this.invalidate();
    }

    public void setThumb(int id) {
        this.thumb = BitmapFactory.decodeResource(getResources(), id);
        this.invalidate();
    }

    public float getValue() {
        return this.value;
    }

    private void init() {
        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeWidth(dpToPx(linwidth));
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);

        valuePaint = new Paint();
        valuePaint.setAntiAlias(true);
        valuePaint.setStrokeWidth(dpToPx(linwidth));
        valuePaint.setStyle(Paint.Style.STROKE);
        valuePaint.setStrokeCap(Paint.Cap.ROUND);
        valuePaint.setColor(lineColor);

    }

    private void drawVlueLine(Canvas canvas, RectF rect, float value) {
//
//        float lineLenght = rect.width() - getPaddingLeft() - getPaddingRight();
//        float valueWidth = (value / max) * lineLenght;
//        float remain = lineLenght - valueWidth;
//        if (value < 0 || value > max) {
//            valueWidth = lineLenght;
//            remain = 0;
//        }
//        //绘制进度
//        canvas.drawLine(rect.left, rect.top, rect.right - remain, rect.top, valuePaint);
//
//        //绘制滑动块
//        if (null != thumb) {
//            thumb = Bitmap.createScaledBitmap(thumb, (int) dpToPx(10), (int) dpToPx(14), true);
//            canvas.drawBitmap(thumb, rect.left - thumb.getWidth() / 2, rect.top, new Paint());
//        }
    }

    private void drawBackgroundLine(Canvas canvas, RectF rect) {

//        bgPaint.setShader(new LinearGradient(rect.left, rect.top, rect.right, rect.top,
//                new int[]{startColor, centerColor, endColor}, null, Shader.TileMode.REPEAT));
        float offset = dpToPx(10);// bgPaint.getStrokeWidth() / 2;
        canvas.drawLine(rect.left, rect.top+offset, rect.right, rect.top+offset, bgPaint);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());

        drawBackgroundLine(canvas, rect);
        drawVlueLine(canvas, rect, value);
    }

    protected float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    protected float spToPx(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
