package com.android.rkdroid_volley_okhttp_client;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;



public class BookDetailsActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if(getResources().getBoolean(R.bool.has_two_panes)){
			finish();
			return;
		}
		
		BookDetailsFragment mBookFrag = new BookDetailsFragment();
		getSupportFragmentManager().beginTransaction().add(android.R.id.content, mBookFrag).commit();
	}

}
