package edu.purdue.cs307.scry;

//import android.app.ListFragment;
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
    public static String TAG = "TaskListFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	TaskArrayAdapter adapter = new TaskArrayAdapter(getActivity().getApplicationContext(),
		        R.layout.fragment_task_list, ((TaskDatasourceActivity) getActivity()).getDataSource()
	            .getAllTasks(), this);
        setListAdapter(adapter);        
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	registerForContextMenu(getListView());
    }

}
