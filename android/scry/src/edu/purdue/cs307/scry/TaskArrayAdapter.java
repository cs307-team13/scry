package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.purdue.cs307.scry.RottenTomatoes.RottenTomatoe;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.model.Task;
import android.os.AsyncTask;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.model.Task;

public class TaskArrayAdapter extends ArrayAdapter<Task> {

	private final Context context;
	private Fragment f;
	private HashMap<String, RottenTomatoe> rt = new HashMap<String, RottenTomatoe>();

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
		TextView task_name = (TextView) taskView.findViewById(R.id.task_name);
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
						@Override
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

		if (t.getCategory().equals("Movie/Television")) {
			RottenTomatoe movie;
			if (rt.get(t.toString()) != null) {
				movie = rt.get(t.toString());

			} else {
				movie = new RottenTomatoe(t, this);
			}
			// if (movie.isValid()) {
			// Set picture to type
			// Set rating to rating
			rt.put(t.toString(), movie);
			TextView rt_describe = (TextView) taskView
					.findViewById(R.id.rt_describe);
			TextView rt_rating = (TextView) taskView
					.findViewById(R.id.rt_rating);
			ImageView rt_image = (ImageView) taskView
					.findViewById(R.id.rt_rotten);
			if (movie.type.equals("Fresh")) {
				rt_image = (ImageView) taskView.findViewById(R.id.rt_fresh);
			}
			if (movie.type.equals("Certified Fresh")) {
				rt_image = (ImageView) taskView
						.findViewById(R.id.rt_certified_fresh);
			}
			if (movie.type.equals("Rotten")) {
				rt_image = (ImageView) taskView.findViewById(R.id.rt_rotten);
			}
			rt_describe.setVisibility(View.VISIBLE);
			rt_image.setVisibility(View.VISIBLE);
			rt_rating.setVisibility(View.VISIBLE);
			rt_rating.setText("" + movie.getRating() + "%");
			// }
		}

		task_name.setText(t.toString());
		task_category.setText(t.getCategory());

		taskView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Log.wtf("Hello", "I long clicked!");
				return true;
			}
		});

		taskView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.wtf("This Sucks", "in on click");
				((MainActivity) f.getActivity()).openEditForTask(t);
			}
		});

		return taskView;
	}

}
