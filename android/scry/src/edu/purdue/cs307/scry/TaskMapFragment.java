package edu.purdue.cs307.scry;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskMapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
	    Bundle savedInstanceState) {

	View v = inflater.inflate(R.layout.fragment_map, null);
	
	return v; 
    }
}
