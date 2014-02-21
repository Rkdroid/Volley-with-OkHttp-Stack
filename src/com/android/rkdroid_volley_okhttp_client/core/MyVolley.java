package com.android.rkdroid_volley_okhttp_client.core;

import android.content.Context;

import com.android.rkdroid_volley_okhttp_client.toolbox.DiskBitmapCache;
import com.android.rkdroid_volley_okhttp_client.toolbox.OkHttpStack;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MyVolley {
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	
	private MyVolley(){
		
	}
	
	static void init(Context context){
		mRequestQueue= Volley.newRequestQueue(context, new OkHttpStack());
		//int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		// Use 1/8th of the available memory for this memory cache.
		//int cacheSize = 1024 * 1024 * memClass / 8; 
		int max_cache_size = 1000000;
		mImageLoader = new ImageLoader(mRequestQueue, new DiskBitmapCache(context.getCacheDir(), max_cache_size));
	}
	
	public static RequestQueue getRequestQueue(){
		if(mRequestQueue != null){
			return mRequestQueue;
		}else{
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	
	/* 			Need to look into this		*/
	
	public static ImageLoader getImageLoader(){
		if(mImageLoader != null){
			return mImageLoader;
		}else{
			throw new IllegalStateException("Imageloader not initizlized");
		}
	}
}
