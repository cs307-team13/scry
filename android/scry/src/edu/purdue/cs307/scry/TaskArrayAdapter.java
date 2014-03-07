package edu.purdue.cs307.scry;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class TaskArrayAdapter extends ArrayAdapter<Task> {
	
	private final Context context;
	private Fragment frag = null;
	
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
	
	public TaskArrayAdapter(Context context, int resource, int textViewResourceId, Task[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}
	
	public TaskArrayAdapter(Context context, int resource, List<Task> objects,
			Fragment f) {
		super(context, resource, objects);
		this.context = context;
		this.frag = f;
	}
	
	public TaskArrayAdapter(Context context, int resource, int textViewResourceId, List<Task> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}
	
	@Override
	public View getView (int position, View convertView, ViewGroup parent){
		final Task t = getItem(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View taskView = inflater.inflate(R.layout.task_list_item, parent, false);
		TextView task_name = (TextView) taskView.findViewById(R.id.task_name);
		TextView task_category = (TextView) taskView.findViewById(R.id.task_category);
		CheckBox completed = (CheckBox) taskView.findViewById(R.id.task_completed);
		if(t.isComplete()){
			completed.setChecked(true);
		} else {
			completed.setChecked(false);
		}
		task_name.setText(t.toString());
		task_category.setText(t.getCategory());
		
		taskView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.wtf("This Sucks", "in on click");
				TaskDetailsFragment helper = new TaskDetailsFragment();
				helper.getDetails(t);
				((MainActivity) frag.getActivity()).pushTaskDetailsFragment();
			}
		});
		
		taskView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Log.wtf("Hello", "I long clicked!");
				return true; 
			}
		});
		return taskView;
	}


}
