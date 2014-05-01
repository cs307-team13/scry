package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.purdue.cs307.scry.RottenTomatoes.RottenTomatoe;
import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.data.TaskDatasourceActivity;
import edu.purdue.cs307.scry.fragments.FriendsListFragment;
import edu.purdue.cs307.scry.model.Task;

public class FriendsArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private ListFragment f;
	public List<String> objects;

	public FriendsArrayAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
	}

	public FriendsArrayAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		this.context = context;
	}

	public FriendsArrayAdapter(Context context, int resource, String[] objects) {
		super(context, resource, objects);
		this.context = context;
	}

	public FriendsArrayAdapter(Context context, int resource,
			int textViewResourceId, String[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}

	public FriendsArrayAdapter(Context context, int resource, List<String> objects, //String[] objects,
			ListFragment f){
		super(context, resource, objects);
		this.context = context;
		this.f = f;
		this.objects = objects;
	}

	public FriendsArrayAdapter(Context context, int resource,
			int textViewResourceId, List<String> objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	final String email = objects.get(position);
    	LayoutInflater inflater = (LayoutInflater) context
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View myView = inflater.inflate(R.layout.friends_list_item, parent, false);
    	TextView textView = (TextView) myView.findViewById(R.id.friend_name);
    	myView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				HttpClientSetup client = new HttpClientSetup();
				TaskArrayAdapter adapter = new TaskArrayAdapter(f.getActivity().getApplicationContext(),
						R.layout.fragment_task_list, new ArrayList<Task>(), (ListFragment) f, true);
				((FriendsListFragment) f).addAdapter(adapter);
				TaskDataSource datasource = ((TaskDatasourceActivity) f.getActivity()).getDataSource();
				Log.d("FriendsArrayAdapter", "Getting for email: " + email);
				List<Task> t = client.getUserIDandTasks(email, adapter);
				//adapter.addAll(client.getTaskListFromServer());//datasource.getTaskForUser(null));
			}
    	});
    	textView.setText(email);
    	return myView; 
    }
	
}
