package edu.purdue.cs307.scry.fragments;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.cs307.scry.FriendsArrayAdapter;
import edu.purdue.cs307.scry.R;
import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.model.Task;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class FriendsListFragment extends ListFragment{
	
	FriendsArrayAdapter adapter;
	TaskDataSource datasource;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    String objects[];
	    datasource = new TaskDataSource(getActivity().getApplicationContext());
		datasource.open();
		objects = datasource.temp_friends;
	    adapter = new FriendsArrayAdapter(getActivity().getApplicationContext(),
		        R.layout.fragment_friends_list, objects, this);
	    setListAdapter(adapter);
	}
}
