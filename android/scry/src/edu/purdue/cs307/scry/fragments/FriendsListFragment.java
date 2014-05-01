package edu.purdue.cs307.scry.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsListFragment extends ListFragment implements
	BackPressedFragment {
	
	FriendsArrayAdapter adapter;
	TaskDataSource datasource;
	Stack<ListAdapter> adapterStack = new Stack<ListAdapter>();
	String objects[];
	HttpClientSetup client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    datasource = new TaskDataSource(getActivity().getApplicationContext());
		datasource.open();
		
		client = new HttpClientSetup();
		
		objects = datasource.temp_friends;
		//ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, objects);
	    adapter = new FriendsArrayAdapter(getActivity().getApplicationContext(),
		        R.layout.fragment_friends_list, datasource.getFriendsEmails(), this); //, client);
	    setListAdapter(adapter);
	    adapterStack.push(adapter);
	}

	
	public void addAdapter(ListAdapter adapter){
		setListAdapter(adapter);
	    adapterStack.push(adapter);
	}
	
	@Override
	public boolean onBackPressed() {
		if(adapterStack.size() != 1){
			adapterStack.pop();
			setListAdapter(adapterStack.peek());
			return true;
		} else {
			return false;
		}
	}
}
