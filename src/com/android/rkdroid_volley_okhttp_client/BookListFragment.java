package com.android.rkdroid_volley_okhttp_client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.rkdroid_volley_okhttp_client.adapter.BookAdapter;
import com.android.rkdroid_volley_okhttp_client.core.MyVolley;
import com.android.rkdroid_volley_okhttp_client.core.VolleyApplication;
import com.android.rkdroid_volley_okhttp_client.model.BookDetailModel;
import com.android.rkdroid_volley_okhttp_client.model.BookItems;
import com.android.rkdroid_volley_okhttp_client.toolbox.GsonRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class BookListFragment extends Fragment{
	GsonRequest<BookItems> bookItems;
	GsonRequest<BookDetailModel> bookDetails;
	private View BookListView;
	private RequestQueue bookRequest;
	private Button mbtn_prev;
	private Button mbtn_next;
	private int pageCount ;
	private int page_increment = 0;
	private ListView mBookListView;
	private BookAdapter mBookAdapter;
	private TextView title;
	List<BookDetailModel> mBookDetails;
	private HashMap<Integer, BookDetailModel> BookMap;
	private ProgressDialog m_cProgressDialog;
	private static final int START_PROGRESS = 1;
	private static final int END_PROGRESS = 2;
	private final int NUM_ITEMS_PAGE   = 5;
	private boolean load_more = false;
	onBookSelectedListener mBookSelectedListener = null;
	
	
	
	private Handler m_cHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			int lCondition = msg.what;
			switch(lCondition){
			case START_PROGRESS:
				m_cProgressDialog = new ProgressDialog(getActivity());
				m_cProgressDialog.show();
				m_cProgressDialog.setCancelable(true);
				m_cProgressDialog.setTitle("Loading... Please wait");
				m_cProgressDialog.setContentView(R.layout.progress);
				break;
			case END_PROGRESS:
				if(m_cProgressDialog!=null && m_cProgressDialog.isShowing()){
					m_cProgressDialog.dismiss();
				}
				break;
			}
			
		}
	};
	
	public interface onBookSelectedListener{
		public void onBookSelected(BookDetailModel book);
	}
	
	public void setOnBookSelectedListener(onBookSelectedListener listener){
		mBookSelectedListener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BookListView = inflater.inflate(R.layout.activity_main, container, false);
		mBookListView = (ListView) BookListView.findViewById(R.id.booklist);
		title	 = (TextView)BookListView.findViewById(R.id.title);
		mbtn_prev = (Button) BookListView.findViewById(R.id.prev);
		mbtn_next = (Button) BookListView.findViewById(R.id.next);
		mBookDetails = new ArrayList<BookDetailModel>();
		mBookAdapter = new BookAdapter(getActivity(),mBookDetails,MyVolley.getImageLoader());
		BookMap = new LinkedHashMap<Integer, BookDetailModel>();
		mBookListView.setAdapter(mBookAdapter);
		bookRequest = MyVolley.getRequestQueue();
		m_cHandler.sendEmptyMessage(START_PROGRESS);
		
		mBookListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				if(mBookSelectedListener!=null){
					BookDetailModel book = mBookDetails.get(position);
					if(book!=null)
					mBookSelectedListener.onBookSelected(book);
				}
			}
		});
		
		  mbtn_next.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					if(page_increment<=pageCount){
					page_increment++;
					loadPage(page_increment);
					CheckEnable();
					}
				}
			});
		    
		   mbtn_prev.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					if(page_increment>0){
					page_increment--;
					loadPage(page_increment);
					CheckEnable();
					}
				}
			});
		
		
		return BookListView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setRetainInstance(true);
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		ConnectServer();
	}
	
	
	@SuppressWarnings("deprecation")
	private void ConnectServer(){
		if(isNetAvailable()){
			BookHttpRequest();
		}else{
			m_cHandler.sendEmptyMessage(END_PROGRESS);
			AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
			ad.setCancelable(false);
			ad.setTitle("Alert");
			ad.setMessage("No Internet Connection.");
			ad.setButton(getActivity().getString(R.string.ok_text), new DialogInterface.OnClickListener() {

	    public void onClick(DialogInterface dialog, int which) {
	        dialog.dismiss();
	    	}
		});
			ad.show();
		}
	}
	
	public boolean isNetAvailable() {	
		ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			connectivity=null;
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void BookHttpRequest(){
		String url = "http://assignment.gae.golgek.mobi/api/v10/items";
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("count", Integer.toString(5));
		builder.appendQueryParameter("callback", "onGetItems");
		builder.appendQueryParameter("format", "json");
		bookItems = new GsonRequest<BookItems>(Request.Method.GET, builder.toString(), 
				BookItems.class, null, BookReqSuccessListener(), BookReqErrorListener());
		bookRequest.add(bookItems);
	}
	
	private void BookOffsetHttpRequest(){
		String url = "http://assignment.gae.golgek.mobi/api/v10/items";
		Uri.Builder builder = Uri.parse(url).buildUpon();
		builder.appendQueryParameter("offset", Integer.toString(5));
		builder.appendQueryParameter("callback", "onGetItems");
		builder.appendQueryParameter("format", "json");
		bookItems = new GsonRequest<BookItems>(Request.Method.GET, builder.toString(), 
				BookItems.class, null, BookReqSuccessListener(), BookReqErrorListener());
		bookRequest.add(bookItems);
	}
	
	private Response.Listener<BookDetailModel> BookDetailReqSuccessListener(){
		return new Response.Listener<BookDetailModel>() {

			@Override
			public void onResponse(BookDetailModel response) {
				try{
					ParseBookDetailsResponse(response);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
	}
	
	private Response.Listener<BookItems> BookReqSuccessListener(){
		return new Response.Listener<BookItems>() {

			@Override
			public void onResponse(BookItems response) {
				try{
					ParseBookItemsResponse(response);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
	}
	
	private Response.ErrorListener BookReqErrorListener(){
		return new Response.ErrorListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onErrorResponse(VolleyError response) {
				// TODO Auto-generated method stub
				m_cHandler.sendEmptyMessage(END_PROGRESS);
				AlertDialog ad = new AlertDialog.Builder(getActivity()).create();
				ad.setCancelable(false);
				ad.setTitle("Alert");
				ad.setMessage("Internet Connection is not stable");
				ad.setButton("Reload", new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        	ConnectServer();
		    	}
			});
				ad.setButton2("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				ad.show();

			}
		};
	}
	
	private void ParseBookItemsResponse(BookItems response) throws NullPointerException{
		VolleyApplication.mBookList.clear();
		VolleyApplication.mBookList = response.getBooks();
		if(VolleyApplication.mBookList!=null){
			Log.v("MainActivity", "succsess");
			for(int index = 0;index<VolleyApplication.mBookList.size();index++){
				
				String url = "http://assignment.gae.golgek.mobi/api/v10/items/"+ VolleyApplication.mBookList.get(index).getBookId().toString();
				Uri.Builder builder = Uri.parse(url).buildUpon();
				builder.appendQueryParameter("format", "json");
				bookDetails = new GsonRequest<BookDetailModel>(Request.Method.GET, builder.toString(), 
						BookDetailModel.class, null, BookDetailReqSuccessListener(), BookReqErrorListener());
				bookRequest.add(bookDetails);
			}
		}
	}
	
	private void ParseBookDetailsResponse(BookDetailModel response) throws NullPointerException{
		if(response!=null){
			BookMap.put(response.getId(), response);
			VolleyApplication.page_data.add(response);
			if(BookMap.size() == VolleyApplication.mBookList.size()){
				int TOTAL_LIST_ITEMS = VolleyApplication.mBookList.size();
				int val = TOTAL_LIST_ITEMS%NUM_ITEMS_PAGE;
				val = val==0?0:1;
				pageCount = TOTAL_LIST_ITEMS/NUM_ITEMS_PAGE+val;
				title.setText("Page "+(1)+" of "+pageCount);
			if(load_more==false){
				load_more = true;
				loadPage(0);
				BookOffsetHttpRequest();
			}
				m_cHandler.sendEmptyMessage(END_PROGRESS);
			}
		}
	}
	
	/**
	 * Method for enabling and disabling Buttons
	 */
	private void CheckEnable()
	{
		if(page_increment+1 == pageCount)
		{
			mbtn_next.setEnabled(false);
			mbtn_prev.setEnabled(true);
		}
		else if(page_increment == 0)
		{
			mbtn_prev.setEnabled(false);
			mbtn_next.setEnabled(true);
		}
		else
		{
			mbtn_prev.setEnabled(true);
			mbtn_next.setEnabled(true);
		}
	}
	
	/**
	 * Method for loading data in listview
	 * @param number
	 */
	private void loadPage(int number)
	{
		mBookDetails.clear();
		title.setText("Page "+(number+1)+" of "+pageCount);
		int start = number * NUM_ITEMS_PAGE;
		for(int i=start;i<(start)+NUM_ITEMS_PAGE;i++)
		{
			if(i<VolleyApplication.page_data.size())
			{
				mBookDetails.add(VolleyApplication.page_data.get(i));
			}
			else
			{
				break;
			}
		}
		mBookAdapter.notifyDataSetChanged();
	}

}
