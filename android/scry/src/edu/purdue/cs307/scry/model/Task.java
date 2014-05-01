package edu.purdue.cs307.scry.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Task implements Parcelable, ClusterItem {

    public String title;
    public String category;
    public double lat_location = Double.NaN, long_location = Double.NaN;
    public String ownerId;
    private String entry_date;
    private String adj_date;
    private boolean complete;
    private long _id;
    private Date date;
    private String key;
    public float rating;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public Task() {
	date = new Date();
	entry_date = dateFormat.format(date);
	adj_date = dateFormat.format(date);
	key = UUID.randomUUID().toString();
    }

    @Deprecated
    public Task(String s, String cat, double lat, double lon, String email) {
	title = s;
	category = cat;
	lat_location = lat;
	long_location = lon;
	ownerId = email;
	complete = false;

	key = UUID.randomUUID().toString();
	
	date = new Date();
	entry_date = dateFormat.format(date);
	adj_date = dateFormat.format(date);
    }

    @Deprecated
    public Task(String s, String id) {
	title = s;
	ownerId = id;
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
	out.writeLong(_id);
	out.writeLong(date.getTime());
	out.writeString(key);
	out.writeDouble(rating);
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
	key = in.readString();
	rating = in.readInt();
    }

    @Override
    public int describeContents() {
	return 0;
    }

    public void setTask(String s) {
	this.title = s;
	this.date = new Date();
	this.adj_date = dateFormat.format(date);
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
	@Override
        public Task createFromParcel(Parcel in) {
	    return new Task(in);
	}

	@Override
        public Task[] newArray(int size) {
	    return new Task[size];
	}
    };

    public void setId(long id) {
	_id = id;
    }

    public long getId() {
	return _id;
    }

    public boolean isComplete() {
	return this.complete;
    }

    public void setComplete(boolean b) {
	this.complete = b;
    }
    
    public float getRating(){
    	return rating;
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

    public void setKey(String uuid) {
	this.key = uuid;
    }

    public String getKey() {
	return this.key;
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

    public LatLng getLocation() {
	if (lat_location != Double.NaN && long_location != Double.NaN) {
	    return new LatLng(lat_location, long_location);
	}
	return null;
    }

    @Override
    public LatLng getPosition() {
	return getLocation();
    }
}