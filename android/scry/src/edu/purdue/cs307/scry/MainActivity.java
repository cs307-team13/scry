package edu.purdue.cs307.scry;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

public class MainActivity extends FragmentActivity implements TaskDatasourceActivity, 
	ActionBar.TabListener {

    private TaskDataSource datasource;
    private ViewPager viewPager;
	private TabsPageAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Tasks", "Friends", "Map" };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	datasource = new TaskDataSource(this
	        .getApplicationContext());
	datasource.open();
	
	/*
	 * I commented out the original opening to the create task screen
	 * since this will now be opened up by a button
	 */

	//FragmentManager fm = getFragmentManager();
	//fm.beginTransaction()
	//        .add(R.id.fragment_pane, new CreateTaskFragment()).addToBackStack("CreateTaskFragment").commit();
	
	//Initialization of tab management
	viewPager = (ViewPager) findViewById(R.id.pager);
	actionBar = getActionBar();
	mAdapter = new TabsPageAdapter(getSupportFragmentManager());
	
	viewPager.setAdapter(mAdapter);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	
	// Adding Tabs
	for (String tab_name : tabs) {
		actionBar.addTab(actionBar.newTab().setText(tab_name)
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
	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.main, menu);
	return true;
    }
    
    public void loginmothafucka(View v){
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
	        .replace(R.id.fragment_pane, new TaskListFragment()).addToBackStack("TaskListFragment").commit();
    }
    
    public void pushTaskDetailsFragment(Task t) {
    	FragmentManager fm = getFragmentManager();
    	fm.beginTransaction()
    	        .replace(R.id.fragment_pane, TaskDetailsFragment.newInstance(t)).addToBackStack("TaskDetailsFragment").commit();
    }
    
    @Override 
    public void onBackPressed()
    {
	FragmentManager fm = getFragmentManager();
	if(fm.getBackStackEntryCount() > 2)
	    fm.popBackStack();
	else 
	    super.onBackPressed();
    }
    
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