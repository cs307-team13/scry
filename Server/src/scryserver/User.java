package scryserver;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class User {
	@Persistent
	private int phone_number;
	
	@Persistent
	private String name;
	
	@Persistent
	private ArrayList<Task> tasklist;
	
	@Persistent
	private ArrayList<User> friendsList;
	
	@PrimaryKey
	@Persistent
	private String email;
	
	
	public User(int num, String nam, String em){
		phone_number = num;
		name = nam;
		email = em;
		tasklist = new ArrayList<Task>();
		friendsList = new ArrayList<User>();
	}
	
	public int getPhone(){
		return this.phone_number;
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
		info += "\nPhone: " + this.getPhone();
		info += "\nEmail: " + this.getEmail();
		return info;
	}
	public static void main(){
	}
}