package edu.purdue.cs307.scry;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import edu.purdue.cs307.scry.fragments.FriendsListFragment;
import edu.purdue.cs307.scry.fragments.TaskListFragment;
import edu.purdue.cs307.scry.fragments.TaskMapFragment;


public class TabsPageAdapter extends FragmentPagerAdapter {
	
	ArrayList<Fragment> frags;
	
	public TabsPageAdapter(FragmentManager fm) {
		super(fm);
		frags = new ArrayList<Fragment>();
		frags.add(new TaskListFragment());
		frags.add(new FriendsListFragment());
		frags.add(new TaskMapFragment());
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
		if(index >= 0 && index <= 2)	
			return frags.get(index);
		/*switch (index) {
		case 0:
			// TODO: Change this
			// Task List fragment activity
			return frags.get(0);
		case 1:
			// Friends fragment activity
			return new FriendsFragment();
		case 2:
			// Maps fragment activity
			return new MapsFragment();
		}*/

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
