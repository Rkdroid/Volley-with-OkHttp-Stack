package com.android.rkdroid_volley_okhttp_client.util;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.os.Build;

public class BitmapUtil {
	
	public static byte[] convertBitmapToBytes(Bitmap bitmap){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
			bitmap.copyPixelsToBuffer(buffer);
			return buffer.array();
		}else{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			return baos.toByteArray();
		}
	}

}
