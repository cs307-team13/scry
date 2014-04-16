package edu.purdue.cs307.scry;

// import android.app.ListFragment;
import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TaskListFragment extends ListFragment {

    TaskArrayAdapter adapter;
    boolean isSortedByCategories = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	adapter = new TaskArrayAdapter(getActivity().getApplicationContext(),
	        R.layout.fragment_task_list, new ArrayList<Task>(), this);
	setListAdapter(adapter);
	setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
	super.onStart();
	refreshData();
    }

    public void refreshData() {
	ListAdapter ada = getListAdapter();
	if (ada instanceof TaskArrayAdapter) {
	    ((TaskArrayAdapter) getListAdapter()).clear();
	    ((TaskArrayAdapter) getListAdapter())
		    .addAll(((TaskDatasourceActivity) getActivity())
		            .getDataSource().getAllTasks());
	} else if (ada instanceof ArrayAdapter<?>)
	{
	    ((ArrayAdapter<String>) ada).clear(); 
	    List<String> categories = ((TaskDatasourceActivity) getActivity())
		    .getDataSource().getCategories();
	    ((ArrayAdapter<String>) ada).addAll(categories);
	}
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	registerForContextMenu(getListView());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	inflater.inflate(R.menu.list, menu);
	MenuItem item = menu.getItem(menu.size()-1);
	if(item.getItemId() == R.id.action_sort)
	{
	    item.setTitle((isSortedByCategories) ? "Show all tasks" : "Sort by Category" );
	}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	    case R.id.action_sort:
		if (isSortedByCategories) {
		    setListAdapter(adapter);
		    item.setTitle("Sort by Category");
		    isSortedByCategories = false;
		} else {
		    List<String> categories = ((TaskDatasourceActivity) getActivity())
			    .getDataSource().getCategories();
		    ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(
			    getActivity(), android.R.layout.simple_list_item_1,
			    categories);
		    setListAdapter(catAdapter);
		    item.setTitle("Show all tasks");
		    isSortedByCategories = true;
		}
		return true;
	}
	return false;
    }

}
