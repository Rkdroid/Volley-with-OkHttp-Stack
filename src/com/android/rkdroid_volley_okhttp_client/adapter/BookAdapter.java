package com.android.rkdroid_volley_okhttp_client.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.rkdroid_volley_okhttp_client.R;
import com.android.rkdroid_volley_okhttp_client.model.BookDetailModel;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class BookAdapter extends BaseAdapter{
	
	private List<BookDetailModel> mBookDetails;
	private LayoutInflater mInflater;
	ImageLoader mImageLoader;
	
	public BookAdapter(Context context,List<BookDetailModel> mBookDetails,ImageLoader imageloader){
		mImageLoader = imageloader;
		mInflater = LayoutInflater.from(context);
		this.mBookDetails = mBookDetails;
	}
	
	public void setBookDetails(List<BookDetailModel> mBookDetails){
		//this.mBookDetails = mBookDetails;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBookDetails.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mBookDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	class ViewHolder {
        TextView title;
        NetworkImageView image;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		   ViewHolder holder;
           if (convertView == null) {
               convertView = mInflater.inflate(R.layout.book_list_item, null);
               holder = new ViewHolder();
               holder.image = (NetworkImageView) convertView.findViewById(R.id.image);
               holder.title = (TextView) convertView.findViewById(R.id.title);
               convertView.setTag(holder);
           } else {
               holder = (ViewHolder) convertView.getTag();
           }
           
           holder.title.setText(mBookDetails.get(position).getTitle());
           holder.image.setImageUrl(mBookDetails.get(position).getImage(),mImageLoader);
           return convertView;
	}

}
