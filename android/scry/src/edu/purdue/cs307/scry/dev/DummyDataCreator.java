package edu.purdue.cs307.scry.dev;

import java.util.Random;

import android.content.Context;
import edu.purdue.cs307.scry.data.TaskDataSource;
import edu.purdue.cs307.scry.model.Task;

public class DummyDataCreator {

    public static void populateDataStore(Context c, TaskDataSource data)
    {
	String[] names = {"Watch Movie", "Play Softball", "Kick ass", "Have fun", "Sports!", "Play games", 
		"Run!", "Visit Mount Rushmore", "Call mom", "Climb Mt. Everest", "Walk the Great Wall", "HI MOM!!!"};
	String[] categories = {"Movies", "Games", "Recreation", "Being Awesome", "Productivity", "Fun", "Swimming", "YOU SUCK!!"};
	for (int i = 0; i < 32; i++)
	{
	    Random r = new Random();
	    double randLat = 40.407641 + (40.462256 - 40.407641) * r.nextDouble();
	    double randLon = -86.894784 + (-86.962076 + 86.894784) * r.nextDouble();
	    
	    String name = names[r.nextInt(names.length-1)];
	    String cat = names[r.nextInt(categories.length-1)];
	    Task t = new Task();
	    t.lat_location = randLat;
	    t.long_location = randLon;
	    t.setTask(name);
	    t.category = cat;
	    data.commitTask(t);
	}
    }
}
