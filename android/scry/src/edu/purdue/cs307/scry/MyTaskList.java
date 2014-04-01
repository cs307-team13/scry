package edu.purdue.cs307.scry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/* 
 * This is a random fill in class that I used for testing the tabs.
 * I tried to have this push the TaskListFragment but it didn't work correctly
 */

public class MyTaskList extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//((MainActivity) getActivity()).pushListFragment();
		View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);
		
		return rootView;
	}
}