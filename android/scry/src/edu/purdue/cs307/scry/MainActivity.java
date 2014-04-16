package edu.purdue.cs307.scry;

import edu.purdue.cs307.scry.dev.DummyDataCreator;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.SearchManager;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SearchView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends FragmentActivity implements
        TaskDatasourceActivity, ActionBar.TabListener {

    private TaskDataSource datasource;
    public static ViewPager viewPager;
    public static TabsPageAdapter mAdapter;

    public static FragmentManager fragmentManager;

    private ActionBar actionBar;

    // private static ArrayList<TabInfo> tabs = new ArrayList<TabInfo>();
    private String tab[] = new String[] { "Tasks", "Friends", "Map" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	fragmentManager = getSupportFragmentManager();

	datasource = new TaskDataSource(this.getApplicationContext());
	datasource.open();

	String userID = getSharedPreferences("pref_profile", 0).getString(
	        "userID", null);
	String userEmail = getSharedPreferences("pref_profile", 0).getString(
	        "email", null);
	String userName = getSharedPreferences("pref_profile", 0).getString(
	        "name", null);

	User currentUser = new User(userID, userName, userEmail);

	Log.wtf("NEW USER", currentUser.toString());

	HttpClientSetup client = new HttpClientSetup();
	client.addUser(currentUser);
	System.out.println("User added to server");

	// Initialization of tab management
	viewPager = (ViewPager) findViewById(R.id.pager);
	actionBar = getActionBar();
	mAdapter = new TabsPageAdapter(getSupportFragmentManager());

	viewPager.setAdapter(mAdapter);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	for (int i = 0; i < 3; i++) {
	    actionBar.addTab(actionBar.newTab().setText(tab[i])
		    .setTabListener(this));
	}

	// on swiping the viewpager make respective tab selected
	viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

	    @Override
	    public void onPageSelected(int position) {
		// on changing the page
		// make respected tab selected
		actionBar.setSelectedNavigationItem(position);
	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    }
	});
	handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
	handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    String query = intent.getStringExtra(SearchManager.QUERY);
	    Intent mapIntent = new Intent(MainActivity.this,
		    SearchActivity.class);

	    mapIntent.putExtra("query", query);
	    startActivity(mapIntent);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle item selection
	switch (item.getItemId()) {
	    case R.id.action_populate:
		Log.d("Options Item", "Adding random data...");
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
		    @Override
		    protected Void doInBackground(Void... params) {
			DummyDataCreator.populateDataStore(
			        MainActivity.this.getApplicationContext(),
			        datasource);
			return null;
		    }

		    @Override
		    protected void onPostExecute(Void v) {
			TaskListFragment frag = ((TaskListFragment) mAdapter
			        .getItem(0));
			frag.refreshData();
		    }
		};
		task.execute();
		return true;
	    case R.id.action_delete:
		datasource.purge();
		Log.d("Options Item", "Deleting...");
		TaskListFragment frag = ((TaskListFragment) mAdapter.getItem(0));
		frag.refreshData();
		return true;
	    case R.id.action_create:
		Intent i = new Intent(this, CreateTaskActivity.class);
		startActivity(i);
		return true;
	    default:
		return super.onOptionsItemSelected(item);
	}
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
	FragmentManager fm = getSupportFragmentManager();
	fm.beginTransaction()
	        .replace(R.id.fragment_pane, new TaskListFragment())
	        .addToBackStack("TaskListFragment").commit();
    }

    @Override
    public void onBackPressed() {
	FragmentManager fm = getSupportFragmentManager();
	if (fm.getBackStackEntryCount() > 2)
	    fm.popBackStack();
	else
	    super.onBackPressed();
    }

    // Chip Leinen original code
    /*
     * public static void NavigateTo(FragmentManager fm, Fragment frag) {
     * if (tabs.get(viewPager.getCurrentItem()).getFragmentManager() == null) {
     * tabs.get(viewPager.getCurrentItem()).setFragmentManager(fm);
     * }
     * FragmentTransaction ft =
     * tabs.get(viewPager.getCurrentItem()).getFragmentManager().beginTransaction();
     * ft.replace(R.id.mainlayout,frag, frag.getClass().toString() );
     * ft.addToBackStack(frag.getClass().toString());
     * ft.commitAllowingStateLoss();
     * 
     * }
     * 
     * //Chip Leinen original code
     * public class TabInfo {
     * private FragmentManager fm;
     * private String tabName;
     * 
     * public TabInfo(FragmentManager _fm, String _tabName) {
     * fm = _fm;
     * tabName = _tabName;
     * }
     * 
     * public void setFragmentManager(FragmentManager _fm) {
     * fm = _fm;
     * }
     * public FragmentManager getFragmentManager() {
     * return fm;
     * }
     * public void setTabname(String _tabName) {
     * tabName = _tabName;
     * }
     * public String getTabname() {
     * return tabName;
     * }
     * }
     */

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	// on tab selected
	// show respected fragment view
	viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
}