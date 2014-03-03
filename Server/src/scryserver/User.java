package scryserver;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

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

@PersistenceCapable
public class User {
	@Persistent
	private int phone_number;
	
	@Persistent
	private String name;
	
	@PrimaryKey
	@Persistent
	private String email;
	
	
	public User(int num, String nam, String em){
		phone_number = num;
		name = nam;
		email = em;
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
