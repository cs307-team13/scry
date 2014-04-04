package edu.purdue.cs307.scry;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class CreateTaskActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.root_fragment);
		FragmentManager fm = getSupportFragmentManager();
		fm.beginTransaction()
				.replace(R.id.mainlayout, new CreateTaskFragment())
				.addToBackStack("CreateTaskFragment").commit();

	}
}
