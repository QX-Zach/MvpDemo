package com.example.ozner.mvpdemo.ViewTest.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.example.ozner.mvpdemo.R;

/**
 * Created by ozner_67 on 2016/4/18.
 */
public class ShowImgAdapter extends BaseAdapter {
    private Context mContext;
    private int[] mData;

    public ShowImgAdapter(Context context, int[] mData) {
        this.mContext = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int position) {
        return mData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imgMezi = new ImageView(mContext);
        imgMezi.setImageResource(mData[position]);         //创建一个ImageView
        imgMezi.setScaleType(ImageView.ScaleType.FIT_XY);      //设置imgView的缩放类型
        imgMezi.setLayoutParams(new Gallery.LayoutParams(250, 250));    //为imgView设置布局参数
//        TypedArray typedArray = mContext.obtainStyledAttributes(R.styleable.Gallery);
//        imgMezi.setBackgroundResource(typedArray.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0));
        return imgMezi;
    }
}
