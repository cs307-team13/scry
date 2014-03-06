package edu.purdue.cs307.scry;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class TaskListFragment extends ListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	
	setListAdapter(new ArrayAdapter<Task>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,
	        ((TaskDatasourceActivity) getActivity()).getDataSource()
	                .getAllTasks()));
    }

}
