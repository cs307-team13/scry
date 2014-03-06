package scryserver;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;
//import edu.purdue.cs307.scry;

@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("This is the server for the Scry Android application. Coming April 2014.");
		//User dan = new User(558, "Dan", "dan@scrymail.com");
		//updateFriendsList(dan);
		//User user = getUserInfo("dan@scrymail.com");
		//resp.getWriter().println("User email: "+user.getEmail());
		//resp.getWriter().println(user.toString());
		//deleteUser(user);
		//storeUser(dan);
		//resp.getWriter().println("Dan has been uploaded.");
		//User temp = getUserInfo(dan.getEmail());
		//resp.getWriter().println("Dan's info:");
		//resp.getWriter().println(temp.toString());
		
		createDemoUsers(resp);
		retrieveAndPrintDemoInfo(resp);
	}
	
	public static void createDemoUsers(HttpServletResponse resp) throws IOException{
		createAndStoreEmily(resp);
		createAndStorePhil(resp);
		createAndStoreJoeBob(resp);
	}
	public static void retrieveAndPrintDemoInfo(HttpServletResponse resp) throws IOException{
		resp.getWriter().println();
		retrieveAndPrintEmily(resp);
		resp.getWriter().println();
		retrieveAndPrintPhil(resp);
		resp.getWriter().println();
		retrieveAndPrintJoeBob(resp);
	}
	public static void createAndStoreEmily(HttpServletResponse resp) throws IOException{
		User emily = new User(2604538654L, "Emily Roberts", "emily839@professionals.org");
		storeUser(emily);
		resp.getWriter().println("Emily is now stored in the database!");
	}
	public static void retrieveAndPrintEmily(HttpServletResponse resp) throws IOException{
		User emily = getUserInfo("emily839@professionals.org");
		resp.getWriter().println("Emily's info:");
		resp.getWriter().println(emily.toString());
	}
	public static void createAndStorePhil(HttpServletResponse resp) throws IOException {
		User phil = new User(1338675309L, "Phil Bilson", "phillyb@coolguys.net");
		storeUser(phil);
		resp.getWriter().println("Phil is now stored in the database!");
	}
	public static void retrieveAndPrintPhil(HttpServletResponse resp) throws IOException{
		User phil = getUserInfo("phillyb@coolguys.net");
		resp.getWriter().println("Phil's info:");
		resp.getWriter().println(phil.toString());
	}
	public static void createAndStoreJoeBob(HttpServletResponse resp) throws IOException {
		User joebob = new User (55585471234L, "Joe-Bob Bobson", "jbb@bobson.com");
		storeUser(joebob);
		resp.getWriter().println("Joe-Bob is now stored in the database");
	}
	public static void retrieveAndPrintJoeBob(HttpServletResponse resp) throws IOException{
		User joebob = getUserInfo("jbb@bobson.com");
		resp.getWriter().println("Joe-Bob's info:");
		resp.getWriter().println(joebob.toString());
	}
	
	public static void storeUser(User user){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(user);
		}
		finally{
			pm.close();
		}
	}
	
	public static void deleteUser(User user){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			User u = pm.getObjectById(User.class, user.getEmail());
			pm.deletePersistent(u);
		}
		finally{
			pm.close();
		}
	}
	
	public static User getUserInfo(String email){
		User ret_user;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			ret_user = pm.getObjectById(User.class, email);
		}
		finally{
			pm.close();
		}
		return ret_user;
	}
	
	/*public static void updateTaskList(User user){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			User u = pm.getObjectById(User.class, user.getEmail());
			u.setTaskList(user.getTaskList());
		}
		finally{
			pm.close();
		}
	}
	
	public static void updateFriendsList(User user){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			User u = pm.getObjectById(User.class, user.getEmail());
			u.setFriendsList(user.getFriendsList());
		}
		finally{
			pm.close();
		}
	}
	
	public static void addFriends(User user1, User user2){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			User ret_user1 = pm.getObjectById(User.class, user1.getEmail());
			User ret_user2 = pm.getObjectById(User.class, user2.getEmail());
			//SHOULD FRIENDS LIST BE MADE UP OF USERS, EMAILS, OR FRIENDS
			// * FRIENDS WOULD JUST HAVE EMAIL, NAME, AND TASKS
			ret_user1.addFriend(user2);
			ret_user2.addFriend(user1);
		}
		finally{
			pm.close();
		}
	}*/
}
