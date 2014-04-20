package edu.purdue.cs307.scry;


import java.util.ArrayList;

/*Persistence is what will enable the google app engine to write instances of
 * classes to the datastore, which is the database we will be using through the 
 * GAE (Google App Engine). Each class should be @PersistenceCapable and each data
 * member of the class should be @Persistent so they can all be stored in the datastore. 
 * We will use the user's email as the key to look up the user in the datastore so it 
 * will need the @PrimaryKey annotation.
 * 
 * These are important to the implementation of the server, so make sure when 
 * creating your classes that these details are included.
 * 
 * More information about the datastore, persistence, and JDO can be found here:
 * https://developers.google.com/appengine/docs/java/datastore/jdo/overview-dn2
 * 
 * Make sure you have the GAE plugin for eclipse installed so you have access to the
 * SDKs necessary.
 * 
 * For Eclipse 4.3: https://developers.google.com/eclipse/docs/install-eclipse-4.3
 * If not using 4.3, just change version number in URL to version you are using
 * */



public class User {
	
	private String name;
	
	private String userID;
	
	private ArrayList<Task> tasklist;
	
	private ArrayList<User> friendsList;
	
	private String email;
	
	
	public User(String ID, String nam, String em){
		userID = ID;
		name = nam;
		email = em;
		tasklist = new ArrayList<Task>();
		friendsList = new ArrayList<User>();
	}
	

	public String getUserID(){
		return this.userID;

	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public ArrayList<Task> getTaskList(){
		return this.tasklist;
	}
	
	public void setTaskList(ArrayList<Task> list){
		this.tasklist.clear();
		this.tasklist = list;
	}
	
	public ArrayList<User> getFriendsList(){
		return this.friendsList;
	}
	
	public void setFriendsList(ArrayList<User> list){
		this.friendsList.clear();
		this.friendsList = list;
	}
	
	public void addTask(Task task){
		this.getTaskList().add(task);
	}
	
	public void addFriend(User friend){
		this.getFriendsList().add(friend);
	}
	
	public String toString(){
		String info = "";
		info += "Name: " + this.getName();

		info += "\nUser ID: " + this.getUserID();

		info += "\nEmail: " + this.getEmail();
		return info;
	}
}
