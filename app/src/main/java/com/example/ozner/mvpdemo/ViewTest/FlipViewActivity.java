package com.example.ozner.mvpdemo.ViewTest;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.ozner.mvpdemo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlipViewActivity extends AppCompatActivity {


    List<View> viewList = new ArrayList<>();
    @InjectView(R.id.iv_img)
    ImageView ivImg;
    List<String> imageUrlList = new ArrayList<>();

    ImageOptions imageOptions;
    @InjectView(R.id.if_flip)
    ViewFlipper ifFlip;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_view);
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        imageLoader = ImageLoader.getInstance();
        imageLoader.setDefaultLoadingListener(new SimpleImageLoadingListener());
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)
                .setFailureDrawableId(R.mipmap.ic_launcher)
                .build();

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 加载图片时会在内存中加载缓存
                .cacheOnDisc(true) // 加载图片时会在磁盘中加载缓
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
        ButterKnife.inject(this);
        x.image().bind(ivImg, "http://images.17173.com/2013/news/2013/02/27/lj0227cos32s.jpg");

        imageUrlList.add("http://images.17173.com/2013/news/2013/02/27/lj0227cos32s.jpg");
        imageUrlList.add("http://p.3761.com/pic/95251434674907.jpg");
        imageUrlList.add("http://images.17173.com/2013/news/2013/02/27/lj0227cos32s.jpg");
        imageUrlList.add("http://images.ali213.net/picfile/pic/2012-04-23/927_992471857.jpg");
        imageUrlList.add("http://images.17173.com/2014/news/2014/06/03/lj0603cos35s.jpg");


//        ImageView imageView0 = new ImageView(this);
////        x.image().bind(imageView0, imageUrlList.get(0));
//        imageLoader.displayImage(imageUrlList.get(0), imageView0, options);
//        ifFlip.addView(imageView0);
//        ImageView imageView1 = new ImageView(this);
////        x.image().bind(imageView1, imageUrlList.get(1));
//        imageLoader.displayImage(imageUrlList.get(1), imageView1, options);
//        ifFlip.addView(imageView1);
//        ImageView imageView2 = new ImageView(this);
//        imageLoader.displayImage(imageUrlList.get(2), imageView2, options);
//        ifFlip.addView(imageView2);
//        ImageView imageView3 = new ImageView(this);
//        imageLoader.displayImage(imageUrlList.get(3), imageView3, options);
//        ifFlip.addView(imageView3);
//        ImageView imageView4 = new ImageView(this);
//        imageLoader.displayImage(imageUrlList.get(4), imageView4, options);
//        ifFlip.addView(imageView4);
        for (String url : imageUrlList) {
            ImageView imageView2 = new ImageView(this);
            imageLoader.displayImage(url, imageView2, options);
            ifFlip.addView(imageView2);
        }
//        ImageLoader.getInstance().displayImage(Uri.decode);

//        ifFlip.setImagUrlList(imageUrlList);
        ifFlip.startFlipping();
    }

    public Uri pathToUri(String path) {

        Uri mUri = Uri.parse("content://media/external/images/media");
        Uri mImageUri = null;

        Cursor cursor = managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String data = cursor.getString(cursor
                    .getColumnIndex(MediaStore.MediaColumns.DATA));
            if (path.equals(data)) {
                int ringtoneID = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.MediaColumns._ID));
                mImageUri = Uri.withAppendedPath(mUri, ""
                        + ringtoneID);
                break;
            }
            cursor.moveToNext();
        }

        return mImageUri;
    }
}
