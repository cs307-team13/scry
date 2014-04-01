package scryserver;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class User {
	@Persistent
	private long phone_number;
	
	@Persistent
	private String name;
	
	/*@Persistent
	private ArrayList<Task> tasklist;
	*/
	/*@Persistent
	private ArrayList<User> friendsList;
	*/
	@PrimaryKey
	@Persistent
	private String email;
	
	private Key key;
	
	
	public User(long num, String nam, String em){
		phone_number = num;
		name = nam;
		email = em;
		//tasklist = new ArrayList<Task>();
		//friendsList = new ArrayList<User>();
	}
	
	public void setKey(Key k){
		this.key = k;
	}
	
	public Key getKey(){
		return this.key;
	}
	
	public long getPhone(){
		return this.phone_number;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	/*public ArrayList<Task> getTaskList(){
		return this.tasklist;
	}
	
	public void addTask(Task task){
		this.tasklist.add(task);
	}
	
	public String taskListtoString(){
		String tasks = "Sample task";
		return tasks;
	}
	
	/*public void setTaskList(ArrayList<Task> list){
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
	
	
	
	public void addFriend(User friend){
		this.getFriendsList().add(friend);
	}*/
	
	public String toString(){
		String info = "";
		info += "Name: " + this.getName();
		info += "\nPhone: " + this.getPhone();
		info += "\nEmail: " + this.getEmail();
		//info += "\nTasks: " + this.taskListtoString();
		return info;
	}
	public static void main(){
	}
}
