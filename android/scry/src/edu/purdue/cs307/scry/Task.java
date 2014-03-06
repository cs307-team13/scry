package edu.purdue.cs307.scry;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

@PersistenceCapable
public class Task implements java.io.Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 2906776728160196901L;

    @Persistent
    public String title;

    @Persistent
    public String category;

    @Persistent
    public double lat_location, long_location;

    public String useremail;

    @Persistent
    private String entry_date;

    @Persistent
    private String adj_date;

    private Date date;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy", Locale.US);

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
    	useremail = email;

    	date = new Date();
    	entry_date = dateFormat.format(date);
		adj_date = dateFormat.format(date);
    }

    public Task(String s, String email) {
    	title = s;
    	useremail = email;

    	date = new Date();

    	entry_date = dateFormat.format(date);
    	adj_date = dateFormat.format(date);
    }

    public void setTask(String s) {
    	this.title = s;
    	this.date = new Date();
    	this.adj_date = dateFormat.format(date);
    }

    public String getOwnerEmail() {
    	return this.useremail;
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

    /*Use getDetails() for more information on a task*/
    public String getDetails(){
    	String description;
    	description = "Category: " + this.getCategory() + "\n";
    	description += "Entry Date: " + this.getEntryDate();
    	return description;
    }
    @Override
    public String toString() {
    	
    	return title;
    }
}