
package edu.purdue.cs307.scry;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class Task implements Parcelable {

    public String title;
    public String category;
    public double lat_location, long_location;
    public String ownerId;
    private String entry_date;
    private String adj_date;
    private boolean complete;
    private int _id;
    private Date date;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public Task() {
	date = new Date();
	entry_date = dateFormat.format(date);
	adj_date = dateFormat.format(date);
    }

    public Task(String s, String cat, double lat, double lon, String email) {
	title = s;
	category = cat;
	lat_location = lat;
	long_location = lon;
	ownerId = email;
	complete = false;

	date = new Date();
	entry_date = dateFormat.format(date);
	adj_date = dateFormat.format(date);
    }

    public Task(String s, String id) {
	title = s;
	ownerId = id;
	category = parse(s);
	date = new Date();
	entry_date = dateFormat.format(date);
	adj_date = dateFormat.format(date);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	out.writeString(title);
	out.writeString(category);
	out.writeDouble(lat_location);
	out.writeDouble(long_location);
	out.writeString(ownerId);
	out.writeString(entry_date);
	out.writeString(adj_date);
	out.writeByte((byte) ((complete) ? 1 : 0));
	out.writeInt(_id);
	out.writeLong(date.getTime());
    }

    private Task(Parcel in) {
	title = in.readString();
	category = in.readString();
	lat_location = in.readDouble();
	long_location = in.readDouble();
	ownerId = in.readString();
	entry_date = in.readString();
	adj_date = in.readString();
	complete = (in.readByte() == 1);
	_id = in.readInt();
	date = new Date(in.readLong());
    }

    @Override
    public int describeContents() {
	return 0;
    }

    public void setTask(String s) {
	this.title = s;
	this.category = parse(s);
	this.date = new Date();
	this.adj_date = dateFormat.format(date);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
	public Task createFromParcel(Parcel in) {
	    return new Task(in);
	}

	public Task[] newArray(int size) {
	    return new Task[size];
	}
    };

    public void setId(int id) {
	_id = id;
    }

    public int getId() {
	return _id;
    }

    public boolean isComplete() {
	return this.complete;
    }

    public void setComplete(boolean b ) {
	this.complete = b;
    }

    public String getOwner() {
	return this.ownerId;
    }

    public String getEntryDate() {
	return this.entry_date;
    }

    public String getCategory() {
	return this.category;
    }

    public String getAdjustmentDate() {
	return this.adj_date;
    }

    /* Use getDetails() for more information on a task */
    public String getDetails() {
	String description;
	description = "Category: " + this.getCategory() + "\n";
	description += "Entry Date: " + this.getEntryDate();
	return description;
    }

    @Override
    public String toString() {

	return title;
    }

public String parse(String s) {
		
		String[] tokens = s.split("[ ]+");
		String ret = null;
		for(int i = 0; i < tokens.length; i++){
			String current = tokens[i];
			Verbs currentVerb;
			if(contains(current)){
				currentVerb = Verbs.valueOf(current.toLowerCase());
			}else
				continue;
			
			switch (currentVerb) {
				
				case go:
					if(tokens[i+1].equals("to"))
						ret = "Visit";
					break;
				case golf:
				case play:
				case swim:
					ret = "Games/Sports";
					break;
				case watch:
					ret = "Movies/Television";
					break;
				case listen:
					ret = "Music";
					break;
				case read:
					ret = "Books";
					break;
				case hang:
					if(tokens[i+1].equals("out"))
						ret = "Social";
					break;
				case party:
					ret = "Social";
					break;
				case call:
				case text:
					ret = "Communication";
					break;
				case sell:
				case buy:
					ret = "Shopping";
					break;
				case cook:
				case eat:
					ret = "Food";
					break;
				default:
					ret = "Other";
					break;
			}
			if(!ret.equals("Other"))
				break;
		}
		return ret;
	}

	public enum Verbs{
		go,
		golf,
		play,
		swim,
		watch,
		listen,
		read,
		hang,
		party,
		call,
		text,
		sell,
		buy,
		cook,
		eat
	}
	
	public static boolean contains(String test) {

	    for (Verbs v : Verbs.values()) {
	        if (v.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}
}