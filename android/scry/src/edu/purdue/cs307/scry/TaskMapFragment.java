package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class TaskMapFragment extends SupportMapFragment {

    private static View view;
    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private TaskDataSource data;

    @Override
    public void onResume() {
	super.onResume();
	Log.d("Map", "onResume()");
	data = ((TaskDatasourceActivity) getActivity()).getDataSource();
	populateTasks();

	// For zooming automatically to the Dropped PIN Location
	getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
		40.426853, -86.923538), 12.0f));
    }

    private void populateTasks() {
	// TODO Auto-generated method stub
	getMap().clear();
	Log.v("Map", "Populating map");
	AsyncTask<Void, Integer, List<Task>> task = new AsyncTask<Void, Integer, List<Task>>() {

	    @Override
	    protected List<Task> doInBackground(Void... params) {
		Log.d("Map", "doInBackground()");
		List<Task> allTasks = data.getAllTasks();
		ArrayList<Task> locTasks = new ArrayList<Task>();
		for (Task t : allTasks) {
		    if (t.getLocation() != null)
			locTasks.add(t);
		}
		Log.d("Map", "List of tasks: " + locTasks.size() + "tasks.");
		return locTasks;
	    }

	    @Override
	    public void onPostExecute(List<Task> locTasks) {
		Log.d("Map", "onPostExecute()");
		for (Task t : locTasks) {

		    Log.d("Adding Tasks", "Task " + t.title + " added");
		    getMap().addMarker(
			    new MarkerOptions().position(t.getLocation())
			            .title(t.title));

		}
	    }

	};
	task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	view = super.onCreateView(inflater, container, savedInstanceState);
	Log.d("Map", "onCreateView()");

	return view;
    }
}