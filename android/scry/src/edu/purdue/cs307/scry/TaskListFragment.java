package edu.purdue.cs307.scry;

//import android.app.ListFragment;
import java.util.ArrayList;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class TaskListFragment extends ListFragment {
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
	
    	TaskArrayAdapter adapter = new TaskArrayAdapter(getActivity().getApplicationContext(),
		        R.layout.fragment_task_list, new ArrayList<Task>(), this);
        setListAdapter(adapter);        
    }
    
    @Override
    public void onStart()
    {
	super.onStart();
	refreshData();
    }
    
    public void refreshData()
    {
	((TaskArrayAdapter) getListAdapter()).clear();
	((TaskArrayAdapter) getListAdapter()).addAll(((TaskDatasourceActivity) getActivity()).getDataSource()
	            .getAllTasks());
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	registerForContextMenu(getListView());
    }

}
