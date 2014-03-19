package edu.purdue.cs307.scry;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
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
				.addToBackStack("").commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void loginmothafucka(View v) {
		Intent intent = new Intent(this, SignInActivity.class);
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
		fm.beginTransaction().add(R.id.fragment_pane, new TaskListFragment())
				.addToBackStack("").commit();
	}

	public void pushTaskDetailsFragment() {
		FragmentManager fm = getFragmentManager();
		fm.beginTransaction()
				.add(R.id.fragment_pane, new TaskDetailsFragment())
				.addToBackStack("").commit();
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
