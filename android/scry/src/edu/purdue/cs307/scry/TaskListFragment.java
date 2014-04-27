package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.google.android.gms.internal.is;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class TaskListFragment extends ListFragment implements
        BackPressedFragment {

    TaskArrayAdapter adapter;
    boolean isSortedByCategories = false;
    Stack<ListAdapter> adapterStack = new Stack<ListAdapter>();
    MenuItem item;
    protected String viewingCategory;
    private boolean isShowingUnfinished = true;

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
    	return null;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	adapter = new TaskArrayAdapter(getActivity().getApplicationContext(),
	        R.layout.fragment_task_list, new ArrayList<Task>(), this);
	adapterStack.push(getListAdapter());
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
		            .getDataSource().getAllUnfinishedTasks());
	} else if (ada instanceof ArrayAdapter<?>) {
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
	item = menu.getItem(menu.size() - 1);
	if (item.getItemId() == R.id.action_sort) {
	    item.setTitle((isSortedByCategories) ? "Show all tasks"
		    : "Sort by Category");
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
		    adapterStack.pop();
		    adapterStack.push(getListAdapter());
		} else {
		    List<String> categories = ((TaskDatasourceActivity) getActivity())
			    .getDataSource().getCategories();
		    ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(
			    getActivity(), android.R.layout.simple_list_item_1,
			    categories);
		    getListView().setOnItemClickListener(
			    new AdapterView.OnItemClickListener() {

			        @Override
			        public void onItemClick(AdapterView<?> parent,
			                View view, int position, long id) {
				    TextView t = (TextView) view
				            .findViewById(android.R.id.text1);
				    viewingCategory = (String) t.getText();
				    List<Task> tasks = ((TaskDatasourceActivity) getActivity())
				            .getDataSource()
				            .getTasksInCategory(viewingCategory);
				    setListAdapter(new TaskArrayAdapter(
				            getActivity()
				                    .getApplicationContext(),
				            R.layout.fragment_task_list, tasks,
				            TaskListFragment.this));
				    adapterStack.push(getListAdapter());
			        }
			    });
		    setListAdapter(catAdapter);
		    item.setTitle("Show all tasks");
		    isSortedByCategories = true;
		    adapterStack.pop();
		    adapterStack.push(getListAdapter());
		}
		return true;
	    case R.id.action_completed:
		if (isShowingUnfinished) {
		    List<Task> completedTasks = ((TaskDatasourceActivity) getActivity())
			    .getDataSource().getAllCompletedTasks();
		    setListAdapter(new TaskArrayAdapter(getActivity()
			    .getApplicationContext(),
			    R.layout.fragment_task_list, completedTasks,
			    TaskListFragment.this));
		    adapterStack.push(getListAdapter());
		    item.setTitle("Show unfinished tasks");
		    isShowingUnfinished = false;
		} else {
		    setListAdapter(adapter);
		    item.setTitle("Show completed tasks");
		    isShowingUnfinished = true;
		    adapterStack.pop();
		    adapterStack.push(getListAdapter());
		}
		return true;

	}
	return false;
    }

    @Override
    public boolean onBackPressed() {
	Log.v("TaskListFragment", "Make this pop the adapter stack!!!!!!");
	adapterStack.pop();
	if (adapterStack.empty())
	    if (isSortedByCategories) {
		setListAdapter(adapter);
		adapterStack.push(adapter);
		isSortedByCategories = false;
		item.setTitle("Sort by Category");
		return true;
	    } else
		return false;
	else {
	    setListAdapter(adapterStack.peek());
	    return true;
	}
    }

}
