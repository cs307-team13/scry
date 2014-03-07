package edu.purdue.cs307.scry;

import android.app.ListFragment;
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
		        0, ((TaskDatasourceActivity) getActivity()).getDataSource()
	            .getAllTasks());
        setListAdapter(adapter);        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	registerForContextMenu(getListView());
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.item_detail) {
    		menu.setHeaderTitle("Task");
    		menu.add(Menu.NONE, 1, 1, "Set as Complete");
    		menu.add(Menu.NONE, 2, 2, "Remove Task");
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    int menuItemIndex = item.getItemId();
		if(menuItemIndex == 1){ //Set as complete selected
			Log.i("onContextItemSelected", "Selected complete");
			//TODO: implement task completion
		}
		if(menuItemIndex == 2){ //Remove task selected
			Log.i("onContextItemSelected", "Selected remove");
			//TODO: implement task removal
		}
    	return true;
    }
    	
    	
    	
    	
    	
    	
    	
    	
    /*super.onCreate(savedInstanceState);
	Log.i("onContextItemSelected", "Going to this place still");
	setListAdapter(new ArrayAdapter<Task>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1,
	        ((TaskDatasourceActivity) getActivity()).getDataSource()
	                .getAllTasks()));
    }*/

}
