package edu.purdue.cs307.scry;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.entity.StringEntity;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import edu.purdue.cs307.scry.model.Task;
import edu.purdue.cs307.scry.model.User;

public class HttpClientSetup {
	private String URL = "http://1-dot-scryserver.appspot.com/server";
	private String tasks;
	private List<Task> task_list;
	
	public void sendRequest() { // SAMPLE, DELETE LATER
		AsyncHttpClient client = new AsyncHttpClient();

		StringEntity se = null;

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
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
	
	public String getTasks(){
		return this.tasks;
	}

	public void addUser(User user) {
		AsyncHttpClient client = new AsyncHttpClient();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
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
	
	public void getTaskByUser(String id){
		AsyncHttpClient client = new AsyncHttpClient();
		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] response){
				System.out.println("Response from server: " + response.toString());
				System.out.println("Status code: " + statusCode);
				for(Header h : headers){
					System.out.println("Header name: " + h.getName());
					System.out.println("Header Value: " + h.getValue());
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("Method", "getTaskByUser");
		params.put("Owner", id); //user.getUserID());
		client.get(URL, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] response){
				//String tasks;
				for(Header h : headers){
					//System.out.println("Header name: " + h.getName() + " Value: " + h.getValue());
					for(HeaderElement he : h.getElements()){
						System.out.println("Header Value: " + he.getValue() + " Header Name: " + he.getName());
						if(he.getName().startsWith("Message: ")){
							int beg = he.getName().indexOf(' ')+1;
							tasks = he.getName().substring(beg).replaceAll(" &", ",");
							tasks = tasks.replaceAll("[|]", ",");
						}
					}
				}
				System.out.println("Tasks from server: " + tasks);
				task_list = convertToList(tasks);
			}
		});
		
	}
	
	private List<Task> convertToList(String s){
		List<Task> list = new ArrayList<Task>();
		JSONObject json;
		//List<String> string_list = Arrays.asList(s.split) //split between individual json entries
		String splits[] = s.split("\\}, ");
		for(String entry : splits){
			entry = entry.replaceAll("[\\[\\]}]", "");
			entry = entry.concat("}");
			try {
				json = new JSONObject(entry);
				String description = json.getString("title");
				String cat = json.getString("category");
				Task t = new Task();
				t.setTask(description);
				t.category = cat;
				list.add(t);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	
		return list;
	}
}
