package edu.purdue.cs307.scry;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CreateTaskFragment extends Fragment {
    public static String TAG = "CreateTaskFragment";
    private TaskDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	datasource = new TaskDataSource(this.getActivity()
	        .getApplicationContext());
	datasource.open();
    }



    @Override
    public void onResume() {
	datasource.open();
	super.onResume();
    }

    @Override
    public void onPause() {
	datasource.close();
	super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	final View v = inflater
	        .inflate(R.layout.create_task, null);

	Button b = (Button) v.findViewById(R.id.btn_create);

	b.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		final TextView txttitle = (TextView) v
		        .findViewById(R.id.etxt_title);
		final TextView txtcat = (TextView) v
		        .findViewById(R.id.etxt_category);

		final String title = txttitle.getText().toString();
		final String category = txtcat.getText().toString();

		AsyncTask<Void, Void, Void> saveData = new AsyncTask<Void, Void, Void>() {

		    @Override
		    protected Void doInBackground(Void... arg0) {
			Task t = datasource.createComment(title, category);
			
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
}
