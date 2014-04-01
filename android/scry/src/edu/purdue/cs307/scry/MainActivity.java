package edu.purdue.cs307.scry;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity implements TaskDatasourceActivity {

    private TaskDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	datasource = new TaskDataSource(this.getApplicationContext());
	datasource.open();

	FragmentManager fm = getFragmentManager();
	fm.beginTransaction().add(R.id.fragment_pane, new CreateTaskFragment())
	        .addToBackStack("CreateTaskFragment").commit();

	handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            datasource.open();
            Cursor c = datasource.getWordMatches(query, null);
            while(!c.isAfterLast())
            {
        	Log.d("Search", c.getString(c.getColumnCount()-1));
        	c.moveToNext();
            }
            datasource.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);

	// Associate searchable configuration with the SearchView
	SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	SearchView searchView = (SearchView) menu.findItem(R.id.search)
	        .getActionView();
	searchView.setSearchableInfo(searchManager
	        .getSearchableInfo(getComponentName()));

	return true;
    }

    public void loginmothafucka(View v) {
	Intent intent = new Intent(this, SigninActivity.class);
	startActivity(intent);
    }

    @Override
    public void onResume() {
	datasource.open();
	super.onResume();
    }

    @Override
    public void onPause() {
	datasource.close();
	super.onPause();
    }

    @Override
    public TaskDataSource getDataSource() {
	return datasource;
    }

    public void pushListFragment() {
	FragmentManager fm = getFragmentManager();
	fm.beginTransaction()
	        .replace(R.id.fragment_pane, new TaskListFragment())
	        .addToBackStack("TaskListFragment").commit();
    }

    public void pushTaskDetailsFragment(Task t) {
	FragmentManager fm = getFragmentManager();
	fm.beginTransaction()
	        .replace(R.id.fragment_pane, TaskDetailsFragment.newInstance(t))
	        .addToBackStack("TaskDetailsFragment").commit();
    }
    
    public void pushMapFragment() {
	FragmentManager fm = getFragmentManager();
	fm.beginTransaction()
	        .replace(R.id.fragment_pane, new TaskMapFragment())
	        .addToBackStack("TaskMapFragment").commit();
    }
    
    
    @Override
    public void onBackPressed() {
	FragmentManager fm = getFragmentManager();
	if (fm.getBackStackEntryCount() > 2)
	    fm.popBackStack();
	else
	    super.onBackPressed();
    }
}