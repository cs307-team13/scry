package scryserver;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import edu.purdue.cs307.scry.User;

@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("This is the server for the Scry Android application. Coming April 2014.");
		User dan = new User(558, "Dan", "dan@scrymail.com");
		//updateFriendsList(dan);
		//User user = getUserInfo("dan@scrymail.com");
		//resp.getWriter().println("User email: "+user.getEmail());
		//resp.getWriter().println(user.toString());
		//deleteUser(user);
		storeUser(dan);
		resp.getWriter().println("Dan has been uploaded.");
		User temp = getUserInfo(dan.getEmail());
		resp.getWriter().println("Dan's info:");
		resp.getWriter().println(temp.toString());
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
	
	public User getUserInfo(String email){
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
	/*
	public static void updateTaskList(User user){
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
			 * FRIENDS WOULD JUST HAVE EMAIL, NAME, AND TASKS
			ret_user1.getFriendsList().add(user2);
			ret_user2.getFriendsList().add(user1);
		}
		finally{
			pm.close();
		}
	}*/
}
