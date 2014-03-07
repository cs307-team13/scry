package edu.purdue.cs307.scry;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TaskDetailsFragment extends Fragment {

	public String title;
	public String category;
	public double lat_location, long_location;
	public String useremail;
	public String entry_date;
	public String adj_date;
	public Task task1;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		    Bundle savedInstanceState) {
		final View v = inflater
		        .inflate(R.layout.fragment_item_properties, null);

		TextView text_title = (TextView)v.findViewById(R.id.title);
		text_title.setText(title);
		TextView text_category = (TextView)v.findViewById(R.id.category);
		text_category.setText(category);
		
		Button remove = (Button) v.findViewById(R.id.remove);
		Button edit = (Button) v.findViewById(R.id.edit);
		Button complete = (Button) v.findViewById(R.id.completed);
	
		remove.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				Log.wtf("What's Up?", "Remove Clicked");
			}
		});
		
		edit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Log.wtf("What's Up?", "Edit Clicked");
			}
		});
		
		complete.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				task1.setComplete();
			}
		});
		return v;
	}
		

	
	public void getDetails(Task t){
		Log.wtf("This Sucks", "in get details");
		title = t.toString();
		category = t.getCategory();
		task1 = t;
		
	}
}
