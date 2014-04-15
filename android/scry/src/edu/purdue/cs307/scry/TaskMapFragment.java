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

public class TaskMapFragment extends Fragment {

    private static View view;
    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private static GoogleMap mMap;
    private static Double latitude, longitude;
    private TaskDataSource data;

    @Override
    public void onResume() {
	Log.d("Map", "onResume()");
	data = ((TaskDatasourceActivity) getActivity()).getDataSource();
	super.onResume();
	populateTasks(); 
    }

    private void populateTasks() {
	// TODO Auto-generated method stub
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
		    if (mMap != null)
		    {
			Log.d("Adding Tasks", "Task " + t.title + " added");
			mMap.addMarker(new MarkerOptions().position(
			        t.getLocation()).title(t.title));
		    }
		}
	    }

	};
	task.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	Log.d("Map", "onCreateView()");
	if (view == null) {
	    if (container == null) {
		return null;
	    }
	    view = (RelativeLayout) inflater.inflate(R.layout.fragment_map,
		    container, false);
	    // Passing harcoded values for latitude & longitude. Please change as
	    // per your
	    // need. This is just used to drop a Marker on the Map
	    latitude = 26.78;
	    longitude = 72.56;
	} else {
	    ((ViewGroup) view.getParent()).removeView(view);
	}
	return view;
    }

    /***** Sets up the map if it is possible to do so *****/
    public void setUpMapIfNeeded() {
	Log.d("Map", "setUpMapIfNeeded()");
	// Do a null check to confirm that we have not already instantiated the
	// map.
	if (mMap == null) {
	    // Try to obtain the map from the SupportMapFragment.
	    try {
		FragmentManager fm = this.getChildFragmentManager(); 
		Fragment f = fm.findFragmentById(R.id.location_map);
		mMap = ((SupportMapFragment) f).getMap();
	    } catch (RuntimeException re) {
		Log.e("Map", "Map not found!", re);
	    }
	    // mMap = ((MapFragment) MainActivity.fragmentManager
	    // .findFragmentById(R.id.location_map)).getMap();
	    // Check if we were successful in obtaining the map.
	    if (mMap != null)
		setUpMap();
	} else
	    setUpMap();
    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not
     * null.
     */
    private void setUpMap() {
	Log.d("Map", "-------------Setting up Map!!!!");
	// For showing a move to my loction button
	mMap.setMyLocationEnabled(true);
	// For dropping a marker at a point on the Map
	mMap.addMarker(new MarkerOptions()
	        .position(new LatLng(latitude, longitude)).title("My Home")
	        .snippet("Home Address"));
	// For zooming automatically to the Dropped PIN Location
	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
	        latitude, longitude), 12.0f));
	populateTasks();
    }
}