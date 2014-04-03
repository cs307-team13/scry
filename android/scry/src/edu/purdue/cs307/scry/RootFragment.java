package edu.purdue.cs307.scry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RootFragment extends Fragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		    Bundle savedInstanceState) {
		final View v = inflater
		        .inflate(R.layout.root_fragment, null);
		return v;
	}
}
