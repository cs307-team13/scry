package edu.purdue.cs307.scry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import edu.purdue.cs307.scry.MyTaskList;
import edu.purdue.cs307.scry.FriendsFragment;
import edu.purdue.cs307.scry.MapsFragment;


public class TabsPageAdapter extends FragmentPagerAdapter {

	public TabsPageAdapter(FragmentManager fm) {
		super(fm);
	}

	/*
	 * This function returns fragments which is why calling the new
	 * TaskListFragment doesn't work because it is a List Fragment and not
	 * just a regular fragment. Either the TaskListFragment needs to change or 
	 * we need to find some other way to call it through the function. 
	 * 
	 * The Friends fragment and the Map fragment are simple place holders
	 * that will get changed later into actually calling the map and listing the
	 * friends list. 
	 */
	
	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// TODO: Change this
			// Task List fragment activity
			return new MyTaskList();
		case 1:
			// Friends fragment activity
			return new FriendsFragment();
		case 2:
			// Maps fragment activity
			return new MapsFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
