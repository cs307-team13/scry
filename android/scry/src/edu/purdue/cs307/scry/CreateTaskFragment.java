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
import android.widget.LinearLayout;
import android.widget.TextView;

public class CreateTaskFragment extends Fragment {
    public static String TAG = "CreateTaskFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	final View v = inflater.inflate(R.layout.create_task, null);

	Button b = (Button) v.findViewById(R.id.btn_create);
	Button expand = (Button) v.findViewById(R.id.btn_more);

	final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
	        this.getActivity(),
	        android.R.layout.simple_dropdown_item_1line,
	        ((TaskDatasourceActivity) getActivity()).getDataSource()
	                .getCategories());
	AutoCompleteTextView textView = (AutoCompleteTextView) v
	        .findViewById(R.id.etxt_category);
	textView.setAdapter(adapter);

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

	b.setOnClickListener(new OnClickListener() {

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
			Task t = ((TaskDatasourceActivity) getActivity())
			        .getDataSource()
			        .createComment(arg0[0], arg0[1]);
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

	return v;
    }
}
