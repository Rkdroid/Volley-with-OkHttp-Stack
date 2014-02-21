package com.android.rkdroid_volley_okhttp_client.core;

import java.util.ArrayList;
import java.util.List;

import com.android.rkdroid_volley_okhttp_client.model.BookDataModel;
import com.android.rkdroid_volley_okhttp_client.model.BookDetailModel;

import android.app.Application;


public class VolleyApplication extends Application{
	
	public static List<BookDataModel> mBookList;
	public static List<BookDetailModel> page_data;
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	
	private void init(){
		mBookList = new ArrayList<BookDataModel>();
		page_data = new ArrayList<BookDetailModel>();
		MyVolley.init(this);
	}
}
