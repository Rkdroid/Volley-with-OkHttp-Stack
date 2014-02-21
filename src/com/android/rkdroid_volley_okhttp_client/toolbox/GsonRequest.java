package com.android.rkdroid_volley_okhttp_client.toolbox;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonRequest<T> extends Request<T> {
	
	/** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE =
        String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Listener<T> mListener;

    private final String mRequestBody;
    
    private Gson mGson;
    private Class<T> mJavaClass;


	public GsonRequest(int method, String url, Class<T> cls, String requestBody, Listener<T> listener, 
			ErrorListener errorListener) {
		super(method, url, errorListener);
		mGson = new Gson();
        mJavaClass = cls;
        mListener = listener;
        mRequestBody = requestBody;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
	
	private Map<String, String> headers = new HashMap<String, String>();
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		return headers;
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String getFunction = "onGetItems";
			String jsonString = "";
            String server_JSONResponse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if(server_JSONResponse.startsWith("{"))
            {
            	jsonString  = server_JSONResponse;
            }else if(server_JSONResponse.startsWith(getFunction)){
            	server_JSONResponse = server_JSONResponse.substring(getFunction.length()+1);
            	server_JSONResponse = server_JSONResponse.substring(0, server_JSONResponse.length()-2);
            	jsonString = "{\"books\":"+server_JSONResponse+"}";
            }
            else{
            	jsonString = "{\"books\":"+server_JSONResponse+"}";
            }
    		T parsedGSON = mGson.fromJson(jsonString, mJavaClass);
            return Response.success(parsedGSON,
                    HttpHeaderParser.parseCacheHeaders(response));
            
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException je) {
            return Response.error(new ParseError(je));
        }
	}
	
	@Override
	public String getBodyContentType() {
		// TODO Auto-generated method stub
		return PROTOCOL_CONTENT_TYPE;
	}
	
	@Override
	public byte[] getBody() throws AuthFailureError {
       try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
	}

}
