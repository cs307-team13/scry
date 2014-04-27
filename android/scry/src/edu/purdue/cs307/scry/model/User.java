package edu.purdue.cs307.scry.model;


import java.util.ArrayList;


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
