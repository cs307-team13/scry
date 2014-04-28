package edu.purdue.cs307.scry.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import edu.purdue.cs307.scry.R;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.model.Task;

public class TaskEditFragment extends Fragment {

    public Task task1;
    private EditText text_title;
    private EditText text_category;
    private EditText text_lat;
    private EditText text_long;

    public static TaskEditFragment newInstance(Task t) {
	TaskEditFragment f = new TaskEditFragment();
	Bundle args = new Bundle();
	args.putParcelable("task", t);
	f.setArguments(args);
	return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle args = getArguments();
	task1 = args.getParcelable("task");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	final View v = inflater
	        .inflate(R.layout.fragment_edit_properties, null);

	text_title = (EditText) v.findViewById(R.id.edit_title);
	text_title.setText(task1.title);
	
	text_category = (EditText) v.findViewById(R.id.edit_category);
	text_category.setText(task1.getCategory());
	
	text_lat = (EditText) v.findViewById(R.id.edit_lat);
	text_lat.setText("" + task1.lat_location);

	text_long = (EditText) v.findViewById(R.id.edit_long);
	text_long.setText("" + task1.long_location);
	
	Button cancel = (Button) v.findViewById(R.id.cancel_button);
	Button save = (Button) v.findViewById(R.id.save_button);

	cancel.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
	    	getActivity().finish();
	    }
	});

	save.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		task1.setTask(text_title.getText().toString());
		task1.category = text_category.getText().toString();
		task1.lat_location = Double.parseDouble(text_lat.getText().toString());
		task1.long_location = Double.parseDouble(text_long.getText().toString());
		((TaskDatasourceActivity)getActivity()).getDataSource().deleteTask(task1);
		((TaskDatasourceActivity)getActivity()).getDataSource().commitTask(task1);
		getActivity().finish();
	    }
	});
	return v;
    }
}