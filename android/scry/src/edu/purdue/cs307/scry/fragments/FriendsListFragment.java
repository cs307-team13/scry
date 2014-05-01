package edu.purdue.cs307.scry.fragments;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.cs307.scry.FriendsArrayAdapter;
import edu.purdue.cs307.scry.HttpClientSetup;
import edu.purdue.cs307.scry.R;
import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.model.Task;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsListFragment extends ListFragment{
	
	FriendsArrayAdapter adapter;
	TaskDataSource datasource;
	String objects[];
	HttpClientSetup client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    datasource = new TaskDataSource(getActivity().getApplicationContext());
		datasource.open();
		
		client = new HttpClientSetup();
		
		objects = datasource.temp_friends;
		ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, objects);
	    /*adapter = new FriendsArrayAdapter(getActivity().getApplicationContext(),
		        R.layout.fragment_friends_list, objects, this);*/
	    setListAdapter(a);
	}
	
	@Override
	public void onListItemClick(ListView I, View v, int position, long id){
		String email = objects[position];
		Log.d("List item clicked", "Email " + email + " clicked!");
		client.getUserIDandTasks(email); //,new UserResult() {
			
		List<Task> task_list = client.getTaskListFromServer();
	}
}
