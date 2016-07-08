package com.example.ozner.mvpdemo.UIView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.example.ozner.mvpdemo.R;

/**
 * Created by ozner_67 on 2016/6/3.
 */
public class UIZProgressBar extends ProgressBar {
    final int thumbWidth = (int) dpToPx(10);
    final int thumbHeight = (int) dpToPx(14);
    int thumbId;//指示器id
    Bitmap thumbBit = null;
    Paint thumbPaint = new Paint();


    public UIZProgressBar(Context context) {
        super(context, null);
    }

    public UIZProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public UIZProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.UIZProgressBar, defStyleAttr, 0);
        thumbPaint.setAntiAlias(true);
        thumbId = a.getResourceId(R.styleable.UIZProgressBar_thumb, R.drawable.filter_status_thumb);
        thumbBit = BitmapFactory.decodeResource(context.getResources(), thumbId);
        thumbBit = Bitmap.createScaledBitmap(thumbBit, thumbHeight / 3 * 2, thumbHeight, true);
        a.recycle();
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight() + thumbHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setThumbId(int resId) {
        this.thumbId = resId;
        thumbBit = BitmapFactory.decodeResource(getResources(), thumbId);
        thumbBit = Bitmap.createScaledBitmap(thumbBit, thumbWidth, thumbHeight, true);
        this.invalidate();
    }

    private void drawThumb(Canvas canvas) {
        int drawPro = getProgress();
        int drawX = getWidth() * drawPro / getMax();
        canvas.drawBitmap(thumbBit, drawX, 0, new Paint());
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        drawThumb(canvas);
        super.onDraw(canvas);
    }

    protected float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


}
