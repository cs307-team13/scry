package edu.purdue.cs307.scry;

import com.google.android.gms.maps.model.LatLng;

import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateTaskFragment extends Fragment {
    public static String TAG = "CreateTaskFragment";
    EditText location;
    Button expand, confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.create_task, null);

	confirm = (Button) v.findViewById(R.id.btn_create);
	expand = (Button) v.findViewById(R.id.btn_more);

	final ArrayAdapter<String> adapter = setAutoComplete(v);

	assignExpandToggle(v);

	location = (EditText) v.findViewById(R.id.etxt_location);
	fillLocationIfNecessary();

	assignConfirmClickListener(v, adapter);

	return v;
    }

    private ArrayAdapter<String> setAutoComplete(final View v) {
	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	        this.getActivity(),
	        android.R.layout.simple_dropdown_item_1line,
	        ((TaskDatasourceActivity) getActivity()).getDataSource()
	                .getCategories());
	final AutoCompleteTextView textView = (AutoCompleteTextView) v
	        .findViewById(R.id.etxt_category);
	textView.setAdapter(adapter);
	
	return adapter;
    }

    private void assignExpandToggle(final View v) {
	expand.setOnClickListener(new OnClickListener() {
	    boolean areVisible = false;

	    @Override
	    public void onClick(View view) {

		LinearLayout extras = (LinearLayout) v
		        .findViewById(R.id.extras);
		for (int i = 0; i < extras.getChildCount(); i++) {
		    extras.getChildAt(i).setVisibility(
			    (areVisible) ? View.GONE : View.VISIBLE);
		}
		areVisible = (areVisible) ? false : true;
	    }

	});
    }

    private void assignConfirmClickListener(final View v,
	    final ArrayAdapter<String> adapter) {
	confirm.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		final EditText txttitle = (EditText) v
		        .findViewById(R.id.etxt_title);
		final EditText txtcat = (EditText) v
		        .findViewById(R.id.etxt_category);

		final String title = txttitle.getText().toString();
		String category = txtcat.getText().toString();
		if (category.equals("")) {
		    Parse p = new Parse();
		    category = p.parse(title);
		}

		AsyncTask<String, Void, Void> saveData = new AsyncTask<String, Void, Void>() {

		    @Override
		    protected Void doInBackground(String... arg0) {
			Task task = new Task();
			task.title = arg0[0]; 
			task.category = arg0[1]; 
			task.ownerId = getActivity().getSharedPreferences("pref_profile", 0).getString(
			        "userID", "");
			
			String loc = location.getText().toString();
			try {
			    String[] latlng = loc.split(", ");
			    task.lat_location = Double.parseDouble(latlng[0]);
			    task.long_location = Double.parseDouble(latlng[1]);
			} catch (RuntimeException re) {
			    Log.e(TAG, "Location could not be parsed.", re);
			}

			((TaskDatasourceActivity) getActivity())
			        .getDataSource().commitTask(task);

			adapter.add(arg0[1]);
			return null;
		    }

		    @Override
		    protected void onPostExecute(Void o) {
			txttitle.setText("");
			txtcat.setText("");
			getActivity().finish();
		    }

		};
		saveData.execute(title, category);
	    }

	});
    }

    private void fillLocationIfNecessary() {
	Bundle extras = getActivity().getIntent().getExtras();
	if (extras != null) {
	    LatLng loc = extras.getParcelable("location");
	    if (loc != null) {
		location.setText(loc.latitude + ", " + loc.longitude);
		expand.performClick();
	    }
	}

    }
}
