package edu.purdue.cs307.scry;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

@PersistenceCapable
public class Task implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2906776728160196901L;

	@Persistent
	private String descrip;
	
	@Persistent
	private double lat_location, long_location;
	
	private String useremail;
	
	@Persistent
	private String entry_date;
	
	@Persistent
	private String adj_date;
	
	private Date date;
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy", Locale.US);
	
	public Task(String s, double lat, double lon, String email){
		descrip = s;
		lat_location = lat;
		long_location = lon;
		useremail = email;
		
		date = new Date();
		entry_date = dateFormat.format(date);
		adj_date = dateFormat.format(date);
	}
	
	public Task(String s, String email){
		descrip = s;
		useremail = email;
		
		date = new Date();
		
		entry_date = dateFormat.format(date);
		adj_date = dateFormat.format(date);
	}
	
	public void setTask(String s){
		this.descrip = s;
		this.date = new Date();
		this.adj_date = dateFormat.format(date);
	}

	public String getTaskDescription(){
		return this.descrip;
	}
	
	public String getOwnerEmail(){
		return this.useremail;
	}
	
	public String getEntryDate(){
		return this.entry_date;
	}
	
	public String getAdjustmentDate(){
		return this.adj_date;
	}
}
