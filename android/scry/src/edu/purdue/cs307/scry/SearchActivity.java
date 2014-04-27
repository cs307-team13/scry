package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.model.Task;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class SearchActivity extends FragmentActivity implements
        TaskDatasourceActivity {
    public static final String TAG = "SearchActivity";
    private TaskDataSource datasource;
    private static List<Task> results = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_search);
	Log.d(TAG, "onCreate()");
	datasource = new TaskDataSource(this.getApplicationContext());
	datasource.open();

	if (savedInstanceState == null) {
	    getSupportFragmentManager().beginTransaction()
		    .add(R.id.container, new PlaceholderFragment()).commit();
	}

	Intent i = getIntent();
	String query = i.getStringExtra("query");

	results = datasource.getTaskMatches(query, null);

	handleIntent(getIntent());
    }

    @Override
    public void onResume() {
	Log.d(TAG, "onResume()");
	datasource.open();
	super.onResume();
    }

    @Override
    public void onPause() {
	Log.d(TAG, "onPause()");
	datasource.close();
	results = null;
	super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	Log.d(TAG, "onCreateOptionsMenu()");

	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.search, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	Log.d(TAG, "onOptionsItemSelected()");

	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
	Log.d(TAG, "onNewIntent()");

	handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
	Log.d(TAG, "handleIntent()");

	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    String query = intent.getStringExtra(SearchManager.QUERY);
	    // use the query to search your data somehow

	    results = datasource.getTaskMatches(query, null);

	}
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends ListFragment {
	public static final String TAG = "PlaceholderFragment";

	public PlaceholderFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    Log.d(TAG, "onCreate()");

	    TaskArrayAdapter adapter = new TaskArrayAdapter(getActivity()
		    .getApplicationContext(), R.layout.fragment_task_list,
		    new ArrayList<Task>(), this);
	    setListAdapter(adapter);
	}

	@Override
	public void onStart() {
	    Log.d(TAG, "onStart()");

	    super.onStart();
	    refreshData();
	}

	public void refreshData() {
	    Log.d(TAG, "refreshData()");

	    ((TaskArrayAdapter) getListAdapter()).clear();
	    ((TaskArrayAdapter) getListAdapter()).addAll(results);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    Log.d(TAG, "onCreateView()");

	    View rootView = inflater.inflate(R.layout.fragment_task_list,
		    container, false);
	    return rootView;
	}
    }

    @Override
    public TaskDataSource getDataSource() {

	return datasource;
    }

}
