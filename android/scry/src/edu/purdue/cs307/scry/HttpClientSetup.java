package edu.purdue.cs307.scry;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.util.Log;

public class HttpClientSetup {
	private Context context;
	
	public HttpClientSetup(Context c){
		this.context = c;
	}
	
	public void sendRequest() {
		AsyncHttpClient client = new AsyncHttpClient();

		/*String time = java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		String userName = "John Smith";

		Requester r = new Requester(userName + (int) (Math.random() * 1000),
				time, "219-555-1242", "Not Urgent", start_lat, start_long,
				end_lat, end_long);*/
		//Log.d("json", r.toJSON().toString());
		StringEntity se = null;

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			public void onSuccess(String suc) {
				Log.d("response", suc);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				System.out.println("Connection to server failed");
				Log.d("failure", Integer.toString(statusCode));
			}
		};

		try {
			se = new StringEntity("{\"name\": \"value\"}"); //r.toJSON().toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.post(this.context, "http://1-dot-scryserver.appspot.com/server", se,
				"application/json", handler);
		Log.d("debug", client.toString());
	}
}
