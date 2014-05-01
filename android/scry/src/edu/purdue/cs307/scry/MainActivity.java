package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;
import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.dev.DummyDataCreator;
import edu.purdue.cs307.scry.fragments.BackPressedFragment;
import edu.purdue.cs307.scry.fragments.TaskListFragment;
import edu.purdue.cs307.scry.model.Task;
import edu.purdue.cs307.scry.model.User;

public class MainActivity extends FragmentActivity implements
		TaskDatasourceActivity, ActionBar.TabListener {

	private static final int EDIT_TASK = 123;
	private TaskDataSource datasource;
	public static ViewPager viewPager;
	public static TabsPageAdapter mAdapter;
	NfcAdapter mNfcAdapter;
	public static FragmentManager fragmentManager;
	
	private ActionBar actionBar;

	// private static ArrayList<TabInfo> tabs = new ArrayList<TabInfo>();
	private String tab[] = new String[] { "Tasks", "Friends", "Map" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragmentManager = getSupportFragmentManager();
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		if (mNfcAdapter == null) {
	        Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
	        finish();
	        return;
	    }

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
		setIntent(intent);
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
		case R.id.action_retrieve:
			Log.d("Options Item", "Retrieving tasks from server");
			AsyncTask<Void, Void, Void> task1 = new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					HttpClientSetup client = new HttpClientSetup();
					client.getTaskByUser(getSharedPreferences("pref_profile", 0)
							.getString("userID", null));
					try {
						Thread.sleep(2000); // Need to wait for getTaskByUser()
											// to complete execution
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					List<Task> task_list = client.getTaskListFromServer();
					for (Task t : task_list) {
						if (t == null)
							continue;
						t.ownerId = getSharedPreferences("pref_profile", 0)
								.getString("userId", null);
						datasource.commitTaskWithoutPush(t);
					}
					return null;
				}

				@Override
				protected void onPostExecute(Void v) {
					TaskListFragment frag = ((TaskListFragment) mAdapter
							.getItem(0));
					frag.refreshData();
				}
			};
			task1.execute();
			return true;
		case R.id.action_add_friend:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// Get the layout inflater
			LayoutInflater inflater = this.getLayoutInflater();

			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			builder.setView(inflater.inflate(R.layout.add_friend_popup, null))
					// Add action buttons
					.setPositiveButton("Add",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									System.out.println("Adding friend now!");

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {

								}
							});
			builder.create().show();
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
		if(getIntent().getAction()!=null)
			Log.v("Main", getIntent().getAction());
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
	        processIntent(getIntent());
	      }
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

	public void openEditForTask(Task t) {
		Intent i = new Intent(this, EditTaskActivity.class);
		i.putExtra("Task", t);
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
		Fragment f = mAdapter.getItem(viewPager.getCurrentItem());
		if (f instanceof BackPressedFragment) {
			boolean handled = ((BackPressedFragment) f).onBackPressed();
			if (handled)
				return;
			else
				super.onBackPressed();
		}
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
	/*
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		
		String text;
		Task t = (Task) getIntent().getExtras().getParcelable("Task");
		Log.wtf("NDEF MADE",t.toString());
		text = t.getOwner() + ", " + t.toString() + ", " + t.getCategory() + ", " + t.getLocation();
		NdefMessage msg = new NdefMessage(
				new NdefRecord[]{ NdefRecord.createMime("application/edu.purdue.cs307.scry.MainActivity",text.getBytes()),NdefRecord.createApplicationRecord("edu.purdue.cs307.scry")
				});
		
		return msg;
	}
	*/
	void processIntent(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage msg = (NdefMessage) rawMsgs[0];
        String s = new String(msg.getRecords()[0].getPayload());
        Log.wtf("MSG", "Processed");
        String[] tokens = s.split("[, ]");
 
        Task t = new Task();
        t.title = tokens[1];
        t.category = tokens[2];
        t.lat_location = Double.parseDouble(tokens[3]);
        t.long_location = Double.parseDouble(tokens[4]);
        t.ownerId = tokens[0];
        
        datasource.commitTask(t);
	}

}