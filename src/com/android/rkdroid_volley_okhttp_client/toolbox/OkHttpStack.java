package com.android.rkdroid_volley_okhttp_client.toolbox;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;

public class OkHttpStack extends HurlStack{
	private final OkHttpClient client;
	
	public OkHttpStack(){
		this(new OkHttpClient());
	}
	
	public OkHttpStack(OkHttpClient client){
		if(client==null){
			throw new NullPointerException("client must not be null");
		}
		this.client = client;
	}
	
	@Override
	protected HttpURLConnection createConnection(URL url) throws IOException {
		// TODO Auto-generated method stub
		return client.open(url);
	}

}
