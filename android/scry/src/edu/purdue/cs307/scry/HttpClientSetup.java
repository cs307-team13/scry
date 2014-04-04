package edu.purdue.cs307.scry;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.util.Log;

public class HttpClientSetup {
	private Context context;
	private String URL = "http://1-dot-scryserver.appspot.com/server";
	AsyncHttpClient client;

	public HttpClientSetup() {
		client = new AsyncHttpClient();
	}

	public void sendRequest() { //SAMPLE, DELETE LATER
		AsyncHttpClient client = new AsyncHttpClient();

		/*
		 * String time = java.text.DateFormat.getDateTimeInstance().format(
		 * Calendar.getInstance().getTime()); String userName = "John Smith";
		 * 
		 * Requester r = new Requester(userName + (int) (Math.random() * 1000),
		 * time, "219-555-1242", "Not Urgent", start_lat, start_long, end_lat,
		 * end_long);
		 */
		// Log.d("json", r.toJSON().toString());
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
			se = new StringEntity("{\"name\": \"value\"}"); // r.toJSON().toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RequestParams params = new RequestParams();
		params.put("key", "value");
		params.put("name", "Shanaenae");
		client.post("http://1-dot-scryserver.appspot.com/server", params, handler); // post(this.context,
							// "http://1-dot-scryserver.appspot.com/server", se,
		// "application/json", handler);
		Log.d("debug", client.toString());
	}

	public void addUser(User user) {
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
		RequestParams params = new RequestParams();
		params.put("Method", "addUser");
		params.put("Id", user.getEmail());
		params.put("Name", user.getName());
		this.client.post(URL, params, handler);
	}
	
	public void addTask(Task task){
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler();
		RequestParams params = new RequestParams();
		params.put("Method", "addTask");
		params.put("Owner", task.getOwner());
		params.put("Description", task.toString());
		params.put("Category", task.getCategory());
		params.put("Complete", task.isComplete());
		this.client.post(URL, params, handler);
	}
	
	public void editTask(Task task){
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler();
		RequestParams params = new RequestParams();
		params.put("Method", "editTask");
	}
}