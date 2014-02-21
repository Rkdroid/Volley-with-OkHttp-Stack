package com.android.rkdroid_volley_okhttp_client;

import com.android.rkdroid_volley_okhttp_client.model.BookDetailModel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;



public class MainActivity extends FragmentActivity implements BookListFragment.onBookSelectedListener{
	
	// Whether or not we are in dual-pane mode
    boolean mIsDualPane = false;
    BookListFragment mBookListFragment;
    BookDetailsFragment mBookDetailsFragment;
    public static BookDetailModel mBook;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		mBookListFragment = (BookListFragment) getSupportFragmentManager().findFragmentById(R.id.booklist);
		mBookDetailsFragment = (BookDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.bookdetails);
		
		// Determine whether we are in single-pane or dual-pane mode by testing the visibility
        // of the article view.
        View detailView = findViewById(R.id.bookdetails);
        mIsDualPane = detailView != null && detailView.getVisibility() == View.VISIBLE;
        
        mBookListFragment.setOnBookSelectedListener(this);
	}



	@Override
	public void onBookSelected(BookDetailModel book) {
		if(mIsDualPane){
			mBookDetailsFragment.displayBookdetails(book);
		}else{
			mBook = book;
			Intent i = new Intent(this, BookDetailsActivity.class);
			startActivity(i);
		}
	}

}
