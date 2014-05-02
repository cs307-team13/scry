package edu.purdue.cs307.scry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.purdue.cs307.scry.RottenTomatoes.RottenTomatoe;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.fragments.TaskListFragment;
import edu.purdue.cs307.scry.model.Task;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
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
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.widget.RatingBar;

import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskArrayAdapter extends ArrayAdapter<Task> {

	private View PreviousToolbar = null;
	private final Context context;
	private Fragment f;
	private HashMap<String, RottenTomatoe> rt = new HashMap<String, RottenTomatoe>();
	public final String errorMessage = "Rotten Tomatoes does not recognize this movie. "
			+ "Please put the task in the form: 'watch <Movie Title>'";
	public boolean inFriends;

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
			ListFragment f, boolean inFriends) {
		super(context, resource, objects);
		this.context = context;
		this.f = f;
		this.inFriends = inFriends;
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
		View taskView;
		if(!inFriends){
			taskView = inflater
					.inflate(R.layout.task_list_item, parent, false);
			checkbox(t, taskView);
		} else {
			taskView = inflater
					.inflate(R.layout.friend_task_item, parent, false);
			addbox(t, taskView);
		}
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
		
		//Set the date
		TextView task_lat = (TextView) taskView.findViewById(R.id.toolbar_lat);
		TextView task_long = (TextView) taskView.findViewById(R.id.toolbar_long);
		TextView task_date = (TextView) taskView.findViewById(R.id.toolbar_date);
		
		DecimalFormat df = new DecimalFormat("#.##");
		task_date.setText(t.getEntryDate());
		if(t.getLat() == 0.0)
			task_lat.setText("");
		else
			task_lat.setText("Lat " + df.format(t.getLat()) + " ");
		if(t.getLong() == 0.0)
			task_long.setText("");
		else
			task_long.setText("Lat " + df.format(t.getLong()) + " ");
				
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

		if (t.getCategory().equals("Movie/Television")

				|| t.getCategory().equals("Movie")) {
			RottenTomatoe movie;
			String movieName = t.toString();
			movieName = movieName.substring(6);
			if (rt.get(movieName) != null) {
				movie = rt.get(movieName);

			} else {
				movie = new RottenTomatoe(movieName, this);
			}

			rt.put(movieName, movie);
			
			TextView rt_describe = (TextView) taskView
					.findViewById(R.id.rt_describe);
			TextView rt_rating = (TextView) taskView
					.findViewById(R.id.rt_rating);
			ImageView rt_image = (ImageView) taskView
					.findViewById(R.id.rt_rotten);
			if (movie.type.equals("Fresh")) {
				rt_image = (ImageView) taskView.findViewById(R.id.rt_fresh);
				rt_rating.setVisibility(View.VISIBLE);
				rt_rating.setText("" + movie.getRating() + "%");
			} else if (movie.type.equals("Certified Fresh")) {
				rt_image = (ImageView) taskView
						.findViewById(R.id.rt_certified_fresh);
				rt_rating.setVisibility(View.VISIBLE);
				rt_rating.setText("" + movie.getRating() + "%");
			} else if (movie.type.equals("Rotten")) {
				rt_image = (ImageView) taskView.findViewById(R.id.rt_rotten);
				rt_rating.setVisibility(View.VISIBLE);
				rt_rating.setText("" + movie.getRating() + "%");
			} else {
				System.out.println("Not Valid");
				rt_image = (ImageView) taskView.findViewById(R.id.rt_error);
				rt_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(f
								.getActivity());
						builder.setMessage(errorMessage)
								.setTitle("Movie Error")
								.setPositiveButton("Ok",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// TODO: Close message
											}
										})
								.setNegativeButton("Edit Task",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												// User cancelled the dialog
												// TODO: Go to edit task screen
												((MainActivity) f.getActivity())
														.openEditForTask(t);
											}
										});
						// Create the AlertDialog object and return it
						builder.create().show();

					}
				});
			}
			rt_describe.setVisibility(View.VISIBLE);
			rt_image.setVisibility(View.VISIBLE);
		}

		task_name.setText(t.toString());
		task_category.setText(t.getCategory());

		taskView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {


				((MainActivity) f.getActivity()).openEditForTask(t);
				return true;
			}
		});

		taskView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				View toolbar = v.findViewById(R.id.toolbar);

				boolean SameItemClicked = false;
				if ((PreviousToolbar == toolbar)) {
					PreviousToolbar = null;
					SameItemClicked = true;
				}
				if (PreviousToolbar != null) {
					ExpandAnimation tempExpandAni = new ExpandAnimation(
							PreviousToolbar, 500);
					PreviousToolbar.startAnimation(tempExpandAni);
				}

				// Creating the expand animation for the item
				ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);

				// Start the animation on the toolbar
				toolbar.startAnimation(expandAni);
				if (SameItemClicked) {
					PreviousToolbar = null;
				} else {
					PreviousToolbar = toolbar;
				}

				Log.wtf("This Sucks", "in on click");
			}
			});
		//});

		return taskView;
	}

	private void checkbox(final Task t, View taskView) {
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
				if(t.isComplete()){
					//Do nothing
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(f.getActivity());
					// Get the layout inflater
					LayoutInflater inflater = f.getActivity().getLayoutInflater();

					// Inflate and set the layout for the dialog
					// Pass null as the parent view because its going in the dialog
					// layout
					final View myView = inflater.inflate(R.layout.rating, null);
					builder.setView(myView)
							.setTitle("Rate Task")
							// Add action buttons
							.setPositiveButton("Rate",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog,
												int id) {
												RatingBar rate = (RatingBar) myView.findViewById(R.id.rating);
												float rating2 = rate.getRating();
												t.rating = rating2;
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {
												//Do Nothing
										}
									});
					builder.create().show();
				}
				t.setComplete(isChecked);
				if (t.isComplete()) {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (t.isComplete()) {
								Log.v("Checked", "run()");
								//if (t.isComplete()) {
									((TaskDatasourceActivity) f.getActivity())
											.getDataSource().deleteTask(t);
									((TaskDatasourceActivity) f.getActivity())
											.getDataSource().commitTask(t);
									TaskArrayAdapter.this.remove(t);
									TaskArrayAdapter.this
											.notifyDataSetChanged();
								//}
							}
						}
					}, 1000);
				} else {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (!t.isComplete()) {
								Log.v("Checked", "run()");
								//if (t.isComplete()) {
									((TaskDatasourceActivity) f.getActivity())
											.getDataSource().deleteTask(t);
									((TaskDatasourceActivity) f.getActivity())
											.getDataSource().commitTask(t);
									TaskArrayAdapter.this.remove(t);
									TabsPageAdapter tabs = ((MainActivity) f.getActivity()).getTabsPageAdapter();
									((TaskListFragment) tabs.getItem(0)).refreshData();
								//}
							}
						}
					}, 1000);
				}
			}

		});
	}
	
	private void addbox(final Task t, View taskView) {
		ImageView add = (ImageView) taskView
				.findViewById(R.id.add_task);
		
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.v("Clicked", "addbox()");
				((TaskDatasourceActivity) f.getActivity())
				.getDataSource().commitTask(t);
				TabsPageAdapter tabs = ((MainActivity) f.getActivity()).getTabsPageAdapter();
				((TaskListFragment) tabs.getItem(0)).refreshData();
			}
		});
	}

}
