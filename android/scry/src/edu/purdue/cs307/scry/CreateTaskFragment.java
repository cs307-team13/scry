package edu.purdue.cs307.scry;

import java.util.List;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateTaskFragment extends Fragment {
    public static String TAG = "CreateTaskFragment";
    public static String creator = null;
    public static String name = null;
    public static int lat = 0;
    public static int longi = 0;
    EditText ET_name;
    EditText ET_creator;
    EditText ET_lat;
    EditText ET_longi;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
   	
	View v = inflater.inflate(R.layout.fragment_createtask, container, false);
    	return v;
    }
	/*
		ET_name = (EditText) v.findViewById(R.id.etxt_title);
	    ET_creator = (EditText) v.findViewById(R.id.etxt_owner);
		ET_lat = (EditText) v.findViewById(R.id.etxt_lat);
		ET_longi = (EditText) v.findViewById(R.id.etxt_long);
		
		Button save = (Button) v.findViewById(R.id.btn_saveTask);
		
		save.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				//HAVE TO DO ERROR CHECKING STILL
				creator = ET_creator.getText().toString();
				name = ET_name.getText().toString();
				lat = Integer.valueOf(ET_lat.getText().toString());
				longi = Integer.valueOf(ET_longi.getText().toString());
				((TaskDatasourceActivity) getActivity()).getDataSource().createComment(name, creator);
				
		    	ListView tasklist = (ListView)v.findViewById(R.id.item_detail);
		    	ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(getActivity().getApplicationContext(),
				        android.R.layout.simple_list_item_1, ((TaskDatasourceActivity) getActivity()).getDataSource()
			            .getAllTasks());
		        tasklist.setAdapter(adapter);
		        adapter.notifyDataSetChanged();        
			}
		});
		return v;
    }

	Button b = (Button) v.findViewById(R.id.btn_create);
	Button list = (Button) v.findViewById(R.id.btn_openlist);
	Button categories = (Button) v.findViewById(R.id.test);

	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	        this.getActivity(),
	        android.R.layout.simple_dropdown_item_1line,
	        ((TaskDatasourceActivity) getActivity()).getDataSource()
	                .getCategories());
	AutoCompleteTextView textView = (AutoCompleteTextView) v
	        .findViewById(R.id.etxt_category);
	textView.setAdapter(adapter);

	categories.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		((TaskDatasourceActivity) getActivity()).getDataSource()
		        .purge();
	    }

	});

	list.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		InputMethodManager inputManager = (InputMethodManager) CreateTaskFragment.this
		        .getActivity().getSystemService(
		                Context.INPUT_METHOD_SERVICE);
		View view = CreateTaskFragment.this.getActivity()
		        .getCurrentFocus();
		if (view != null) {
		    inputManager.hideSoftInputFromWindow(view.getWindowToken(),
			    InputMethodManager.HIDE_NOT_ALWAYS);
		}
		((MainActivity) getActivity()).pushListFragment();
	    }

	});
	
	
	b.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		final EditText txttitle = (EditText) v
		        .findViewById(R.id.etxt_title);
		final EditText txtcat = (EditText) v
		        .findViewById(R.id.etxt_category);

		final String title = txttitle.getText().toString();
		final String category = txtcat.getText().toString();

		AsyncTask<Void, Void, Void> saveData = new AsyncTask<Void, Void, Void>() {

		    @Override	
		    protected Void doInBackground(Void... arg0) {
			Task t = ((TaskDatasourceActivity) getActivity())
			        .getDataSource().createComment(title, category);
			adapter.add(category);
			return null;
		    }

		    @Override
		    protected void onPostExecute(Void o) {
			txttitle.setText("");
			txtcat.setText("");
		    }

		};
		saveData.execute();

	    }

	});

	return v; 
    } 
    */
}
