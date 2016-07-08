package com.example.ozner.mvpdemo.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

public class FileUtils {
	public static String saveBitmap(Bitmap bm) {
		Bitmap bitmap =bm;
		File temp = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/HoYoImage");// 自已缓存文件夹
		if (!temp.exists()) {
			temp.mkdirs();
		}
		File tempFile = new File(temp.getAbsolutePath() + "/"
				+ Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名
		// 图像保存到文件中
		FileOutputStream foutput = null;
		try {
			foutput = new FileOutputStream(tempFile);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutput)) {
				//  filePath=tempFile.getAbsolutePath();
			return	tempFile.getPath();
				//return Uri.fromFile(tempFile);
			}
			else
				return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			try{
				if(foutput!=null)
					foutput.close();

			}catch (Exception ex){ex.printStackTrace();}
		}

	}
	public static String saveBitmap(Bitmap bm,String path) {
		Bitmap bitmap =bm;
		File temp = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/OznerImage/Cache");// 自已缓存文件夹
		if (!temp.exists()) {
			temp.mkdirs();
		}
		File tempFile = new File(temp.getAbsolutePath() + "/"
				+ path + ".jpg"); // 以时间秒为文件名
		// 图像保存到文件中
		FileOutputStream foutput = null;
		try {
			foutput = new FileOutputStream(tempFile);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, foutput)) {
				//  filePath=tempFile.getAbsolutePath();
				return	tempFile.getPath();
				//return Uri.fromFile(tempFile);
			}
			else
				return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		finally {
			try{
				if(foutput!=null)
					foutput.close();

			}catch (Exception ex){ex.printStackTrace();}
		}

	}




	public static  String getRealPath(Context context ,Uri fileUrl){
		String fileName = null;
		Uri filePathUri = fileUrl;
		if(fileUrl!= null){
			if (fileUrl.getScheme().toString().compareTo("content")==0)           //content://开头的uri
			{
				Cursor cursor = context.getContentResolver().query(fileUrl, null, null, null, null);
				if (cursor != null && cursor.moveToFirst())
				{
					int column_index = cursor.getColumnIndexOrThrow("_data");
					fileName = cursor.getString(column_index);          //取出文件路径
					if(!fileName.startsWith("/mnt")){
//检查是否有”/mnt“前缀

						fileName = "/mnt" + fileName;
					}
					cursor.close();
				}
			}else if (fileUrl.getScheme().compareTo("file")==0)         //file:///开头的uri
			{
				fileName = filePathUri.toString();
				fileName = filePathUri.toString().replace("file://", "");
//替换file://
				if(!fileName.startsWith("/mnt")){
//加上"/mnt"头
					fileName += "/mnt";
				}
			}
		}
		return fileName;
	}

}
