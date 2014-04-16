package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class TaskMapFragment extends SupportMapFragment implements
        ClusterManager.OnClusterClickListener<Task>,
        ClusterManager.OnClusterInfoWindowClickListener<Task>,
        ClusterManager.OnClusterItemClickListener<Task>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Task> {

    private static View view;
    /**
     * Note that this may be null if the Google Play services APK is not
     * available.
     */

    private TaskDataSource data;
    protected ClusterManager<Task> mClusterManager;

    @Override
    public void onResume() {
	super.onResume();
	Log.d("Map", "onResume()");
	data = ((TaskDatasourceActivity) getActivity()).getDataSource();
	populateTasks();

	// For zooming automatically to the Dropped PIN Location
	getMap().animateCamera(
	        CameraUpdateFactory.newLatLngZoom(new LatLng(40.426853,
	                -86.923538), 12.0f));
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

		mClusterManager = new ClusterManager<Task>(
		        TaskMapFragment.this.getActivity(), getMap());
		getMap().setOnCameraChangeListener(mClusterManager);

		mClusterManager.addItems(locTasks);
		mClusterManager.setRenderer(new TaskRenderer());
		getMap().setOnCameraChangeListener(mClusterManager);
	        getMap().setOnMarkerClickListener(mClusterManager);
	        getMap().setOnInfoWindowClickListener(mClusterManager);
		mClusterManager.setOnClusterClickListener(TaskMapFragment.this);
	        mClusterManager.setOnClusterInfoWindowClickListener(TaskMapFragment.this);
	        mClusterManager.setOnClusterItemClickListener(TaskMapFragment.this);
	        mClusterManager.setOnClusterItemInfoWindowClickListener(TaskMapFragment.this);
		/*
	         * getMap().addMarker(
	         * new MarkerOptions().position(t.getLocation())
	         * .title(t.title));
	         */
	        mClusterManager.cluster();

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

    @Override
    public void onClusterItemInfoWindowClick(Task item) {
	// TODO Auto-generated method stub
	Toast.makeText(this.getActivity(), "onClusterItemInfoWindowClick()", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onClusterItemClick(Task item) {
	// TODO Auto-generated method stub
	Toast.makeText(this.getActivity(), "onClusterItemClick()", Toast.LENGTH_SHORT).show();

	return false;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Task> cluster) {
	// TODO Auto-generated method stub
	Toast.makeText(this.getActivity(), "onClusterInfoWindowClick()", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onClusterClick(Cluster<Task> cluster) {
	// TODO Auto-generated method stub
	Toast.makeText(this.getActivity(), "onClusterClick()", Toast.LENGTH_SHORT).show();

	return false;
    }
    
    private class TaskRenderer extends DefaultClusterRenderer<Task> {

        public TaskRenderer() {
            super(TaskMapFragment.this.getActivity().getApplicationContext(), getMap(), mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Task task, MarkerOptions markerOptions) {
            markerOptions.title(task.title);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<Task> cluster) {
            // Always render clusters.
            return cluster.getSize() > 4;
        }
    }
    
    
}