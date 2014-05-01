package edu.purdue.cs307.scry.RottenTomatoes;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import edu.purdue.cs307.scry.TaskArrayAdapter;
import edu.purdue.cs307.scry.model.Task;

public class RottenTomatoe {
	public String type = "Blah";
	public final String rtAPI = "http://api.rottentomatoes.com/api/public/v1.0/movie_alias.json?apikey=xfggz76arpb4n8ukptd5x42f&type=imdb&id=";
	public final String imdbAPI = "http://www.omdbapi.com/?t=";
	public int rating = -1;
	public boolean valid;
	public boolean pending = false;
	AsyncHttpResponseHandler handler;
	
	public RottenTomatoe(String name, final TaskArrayAdapter adapter){
		handler = new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
				//Set Rotten Tomatoes Data
				String imdbID = null;
				if(statusCode == 404) rating = -1;
				String response = new String(responseBody);
				try {
					JSONObject json = new JSONObject(response);
					imdbID = json.getString("imdbID");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("IMDBID original: "  + imdbID);
				if(imdbID == null){
					return;
				}
				imdbID = imdbID.substring(2);
				System.out.println("IMDBID substring: "  + imdbID);
				AsyncHttpClient client2 = new AsyncHttpClient();
				client2.get(rtAPI + imdbID, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, org.apache.http.Header[] headers, byte[] responseBody){
						if(statusCode == 404) rating = -1;
						String response = new String(responseBody);
						try {
							JSONObject json = new JSONObject(response);
							json = json.getJSONObject("ratings");
							type = json.getString("critics_rating");
							System.out.println("Type give is: " + type);
							String myRating = json.getString("critics_score");
							rating = Integer.parseInt(myRating);
							System.out.println("The rating given is: " + rating);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						valid = true;
						pending = false;
						adapter.notifyDataSetChanged();
					}
					
					@Override
					public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, Throwable error){
						valid = false;
						pending = false;
						Log.wtf("Rotten Tomatoe", "HTTP Client Failure");
					}
				});
				
			}
			
			@Override
			public void onFailure(int statusCode, org.apache.http.Header[] headers, byte[] responseBody, Throwable error){
				valid = false;
				pending = false;
				Log.wtf("Rotten Tomatoe", "HTTP Client Failure");
			}
			
			
		};
		pending = true;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(imdbAPI + name, handler);
		
	}
	
	public int getRating(){
		return rating;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean isValid(){
		return valid;
	}
	
	public boolean getPending(){
		return pending;
	}
}
