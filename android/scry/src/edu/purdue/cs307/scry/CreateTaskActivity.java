package edu.purdue.cs307.scry;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.fragments.CreateTaskFragment;

public class CreateTaskActivity extends FragmentActivity implements
TaskDatasourceActivity{

    private TaskDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.root_fragment);
	FragmentManager fm = getSupportFragmentManager();
	fm.beginTransaction()
	        .replace(R.id.mainlayout, new CreateTaskFragment())
	        .addToBackStack("CreateTaskFragment").commit();

	datasource = new TaskDataSource(this.getApplicationContext());
	datasource.open();
    }

    @Override
    public TaskDataSource getDataSource() {
	return datasource;
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
}
