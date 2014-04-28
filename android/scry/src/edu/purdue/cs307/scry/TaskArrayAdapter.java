package edu.purdue.cs307.scry;

import java.util.List;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
// import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskArrayAdapter extends ArrayAdapter<Task> {

    private final Context context;
    private Fragment f;
    private View PreviousToolbar = null;
    
    public TaskArrayAdapter(Context context, int resource) {
	super(context, resource);
	this.context = context;
    }

    public TaskArrayAdapter(Context context, int resource,
	    int textViewResourceId) {
	super(context, resource, textViewResourceId);
	this.context = context;
    }

    public TaskArrayAdapter(Context context, int resource, Task[] objects) {
	super(context, resource, objects);
	this.context = context;
    }

    public TaskArrayAdapter(Context context, int resource,
	    int textViewResourceId, Task[] objects) {
	super(context, resource, textViewResourceId, objects);
	this.context = context;
    }

    public TaskArrayAdapter(Context context, int resource, List<Task> objects,
	    Fragment f) {
	super(context, resource, objects);
	this.context = context;
	this.f = f;
    }

    public TaskArrayAdapter(Context context, int resource,
	    int textViewResourceId, List<Task> objects) {
	super(context, resource, textViewResourceId, objects);
	this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	final Task t = getItem(position);
	LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View taskView = inflater
	        .inflate(R.layout.task_list_item, parent, false);
	
	
	taskView.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			View toolbar = v.findViewById(R.id.toolbar);
			
			boolean SameItemClicked = false;
			if ((PreviousToolbar==toolbar)) {
			PreviousToolbar = null;
			SameItemClicked = true;
			}
			if (PreviousToolbar!=null){
			ExpandAnimation tempExpandAni = new ExpandAnimation(PreviousToolbar, 500);
			PreviousToolbar.startAnimation(tempExpandAni);
			}

			// Creating the expand animation for the item
			ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);

			// Start the animation on the toolbar
			toolbar.startAnimation(expandAni);
			if (SameItemClicked) {
			PreviousToolbar = null;
			}
			else {
			PreviousToolbar = toolbar;
			}
           

            // Creating the expand animation for the item
           // ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);

            // Start the animation on the toolbar
          //  toolbar.startAnimation(expandAni);
		}
	});
	
	
	TextView task_name = (TextView) taskView.findViewById(R.id.task_name);
	/*
	task_name.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            View toolbar = v.findViewById(R.id.toolbar);

            // Creating the expand animation for the item
            ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);

            // Start the animation on the toolbar
            toolbar.startAnimation(expandAni);
			
		}
	});
	
	*/
	
	
	TextView task_category = (TextView) taskView
	        .findViewById(R.id.task_category);
	CheckBox completed = (CheckBox) taskView
	        .findViewById(R.id.task_completed);
	if (t.isComplete()) { 
	    completed.setChecked(true);
	} else {
	    completed.setChecked(false);
	}

	completed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    final boolean isChecked) {
		Log.v("Checked", "onCheckedChanged()");
		t.setComplete(isChecked);
		if (t.isComplete()) {
		    Handler handler = new Handler();
		    handler.postDelayed(new Runnable() {
			public void run() {
			    if (t.isComplete()) {
				Log.v("Checked", "run()");
				if (t.isComplete()) {
				    ((TaskDatasourceActivity) f.getActivity())
					    .getDataSource().deleteTask(t);
				    ((TaskDatasourceActivity) f.getActivity())
					    .getDataSource().commitTask(t);
				    TaskArrayAdapter.this.remove(t);
				    TaskArrayAdapter.this
					    .notifyDataSetChanged();
				}
			    }
			}
		    }, 1000);
		} 	
	    }
	});
	task_name.setText(t.toString());
	task_category.setText(t.getCategory());


	//ADDED BY CHRIS
//	View toolbar = convertView.findViewById(R.id.toolbar);
	//View toolbar = ((Activity)context).getWindow().getDecorView().findViewById(R.id.toolbar);
  //  ((LinearLayout.LayoutParams) toolbar.getLayoutParams()).bottomMargin = -50;
  //  toolbar.setVisibility(View.GONE);
	return taskView;
    }
}
