package com.android.rkdroid_volley_okhttp_client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.rkdroid_volley_okhttp_client.core.MyVolley;
import com.android.rkdroid_volley_okhttp_client.model.BookDetailModel;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class BookDetailsFragment extends Fragment{
	private View BookDetailView;
	private TextView Title;
	private TextView Author;
	private TextView Price;
	private NetworkImageView Book_image;
	ImageLoader mImageLoader;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BookDetailView = inflater.inflate(R.layout.book_detail, container, false);
		Title = (TextView) BookDetailView.findViewById(R.id.book_title);
		Author = (TextView) BookDetailView.findViewById(R.id.book_author);
		Price = (TextView) BookDetailView.findViewById(R.id.book_price);
		Book_image = (NetworkImageView) BookDetailView.findViewById(R.id.book_image);
		mImageLoader = MyVolley.getImageLoader();
		if((MainActivity.mBook!=null) && (!getResources().getBoolean(R.bool.has_two_panes))){
			displayBookdetails(MainActivity.mBook);
		}
		return BookDetailView;
	}
	
	public void displayBookdetails(BookDetailModel book){
	try{
		if(book!=null){
			Book_image.setImageUrl(book.getImage(), mImageLoader);
			Title.setText(book.getTitle());
			Author.setText(book.getAuthor());
			Price.setText("EUR " + book.getPrice().toString());
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

}
