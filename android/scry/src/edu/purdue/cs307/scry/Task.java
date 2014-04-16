package edu.purdue.cs307.scry;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.text.SimpleDateFormat;
import com.google.maps.android.clustering.ClusterItem;

import com.google.android.gms.maps.model.LatLng;

public class Task implements Parcelable, ClusterItem {

    public String title;
    public String category;
    public double lat_location = Double.NaN, long_location = Double.NaN;
    public String ownerId;
    private String entry_date;
    private String adj_date;
    private boolean complete;
    private int _id;
    private Date date;
    private UUID key;

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

    public void setComplete(boolean b) {
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

    public void setKey(UUID key) {
	this.key = key;
    }

    public UUID getKey() {
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