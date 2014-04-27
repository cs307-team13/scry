package edu.purdue.cs307.scry;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.purdue.cs307.scry.model.Task;
import edu.purdue.cs307.scry.model.User;

public class HttpClientSetup {
	private String URL = "http://1-dot-scryserver.appspot.com/server";

	public void sendRequest() { // SAMPLE, DELETE LATER
		AsyncHttpClient client = new AsyncHttpClient();

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
		client.post("http://1-dot-scryserver.appspot.com/server", params,
				handler); // post(this.context,
		// "http://1-dot-scryserver.appspot.com/server", se,
		// "application/json", handler);
		Log.d("debug", client.toString());
	}

	public void addUser(User user) {
		AsyncHttpClient client = new AsyncHttpClient();
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
		params.put("Id", user.getUserID());
		params.put("Email", user.getEmail());
		params.put("Name", user.getName());
		client.post(URL, params, handler);
	}

	public void addTask(Task task) {
		AsyncHttpClient client = new AsyncHttpClient();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler();
		RequestParams params = new RequestParams();
		params.put("Method", "addTask");
		params.put("ID", task.getKey());
		params.put("Owner", task.getOwner());
		params.put("Description", task.toString());
		params.put("Category", task.getCategory());
		if(task.isComplete())
			params.put("Complete", "true");
		else
			params.put("Complete", "false");
		client.post(URL, params, handler);
	}

	public void editTask(Task task) {
		AsyncHttpClient client = new AsyncHttpClient();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler();
		RequestParams params = new RequestParams();
		params.put("Method", "editTask");
		params.put("ID", task.getKey());
		params.put("Owner", task.getOwner());
		params.put("Description", task.toString());
		params.put("Category", task.getCategory());
		if(task.isComplete())
			params.put("Complete", "true");
		else
			params.put("Complete", "false");
		client.post(URL, params, handler);
	}
	
	public void deleteTask(Task task){
		AsyncHttpClient client = new AsyncHttpClient();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler();
		RequestParams params = new RequestParams();
		params.put("Method", "deleteTask");
		params.put("ID", task.getKey());
		client.post(URL, params, handler);
	}
	
	public void getTaskByUser(String id){ //User user){
		AsyncHttpClient client = new AsyncHttpClient();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
				System.out.println("Response from server: " + responseBody);
			}
		};
		RequestParams params = new RequestParams();
		params.put("Method", "getTaskByUser");
		params.put("Owner", id); //user.getUserID());
		client.get(URL, params, handler);
		
	}
}
