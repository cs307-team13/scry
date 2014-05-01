package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;

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
	
	public String id;
	
	public String tasks;
	public ArrayList<Task> task_list;
	
	
	public ArrayList<Task> getTaskListFromServer(){
		return this.task_list;
	}
	
	public void clearClientSetupTaskList(){
		this.task_list.clear();
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
	
	public void getUserIDandTasks(String email){
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("Method", "getUserId");
		params.put("Email", email); 
		System.out.println("In getUserID " + email);
		client.get(URL, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] response){
				System.out.println("In onSuccess");
				for(Header h : headers){
					System.out.println("Header name: " + h.getName() + " Value: " + h.getValue());
					for(HeaderElement he : h.getElements()){
						if(he.getName().startsWith("User ID: ")){
							int beg = he.getName().indexOf(' ')+5;
							id = he.getName().substring(beg);
							System.out.println("Retrieved user id: " + id);
						}
					}
				}
				getTaskByUser(id);
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error){
				System.out.println("Retrieval failed");
			}
		});
	}
	
	public void getTaskByUser(String id){
		Log.d("HttpClientSetup", "Getting tasks for user " + id);
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("Method", "getTaskByUser");
		params.put("Owner", id); 
		client.get(URL, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] response){
				for(Header h : headers){
					System.out.println("Header name: " + h.getName() + " Value: " + h.getValue());
					for(HeaderElement he : h.getElements()){
						//System.out.println("Header Value: " + he.getValue() + " Header Name: " + he.getName());
						if(he.getName().startsWith("Message: ")){
							int beg = he.getName().indexOf(' ')+1;
							tasks = he.getName().substring(beg).replaceAll(" &", ",");
							tasks = tasks.replaceAll("[|]", ",");
						}
					}
				}
				System.out.println("Tasks from server: " + tasks);
				task_list = convertToList(tasks);
				System.out.println("Task_List: " + task_list.toString());
			}
		});
		
	}
	
	private ArrayList<Task> convertToList(String s){
		ArrayList<Task> list = new ArrayList<Task>();
		JSONObject json;
		String splits[] = s.split("\\}, "); //Split at end of each JSON entry
		for(String entry : splits){
			entry = entry.replaceAll("[\\[\\]}]", ""); //Delete square brackets for conversion to JSONObject
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
