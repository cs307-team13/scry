package scryserver;

import java.io.IOException;
import java.util.Enumeration;

import edu.purdue.cs307.channel.api.*;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

//import com.google.appengine.api.datastore.Key;
//import com.google.appengine.api.datastore.KeyFactory;
//import edu.purdue.cs307.scry;

@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {
	
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("This is the server for the Scry Android application. Coming April 2014.");
		
		//String channelkey = "key"; //req.getParameter("c");  //RETURNING NULL
		//String token = channelService.createChannel(channelkey);
		
		
		// Send the client the 'token' + the 'channelKey' this way the client
		// can start using the new channel
		/*resp.setContentType("text/html");
		StringBuffer sb = new StringBuffer();
		sb.append("{\"channelkey\": \"");
		sb.append(channelkey);
		sb.append("\", \"token\": \"");
		sb.append(token);
		sb.append("\"}");
		resp.getWriter().write(sb.toString());*/
		
		
		
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
		
		/*createDemoUsers(resp);
		try {
			retrieveAndPrintDemoInfo(resp);
		} catch (EntityNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.err.println("Entity not found");
		}*/
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String method_call = req.getParameter("Method");
		switch(method_call){
		case "addUser":
			addUser(req);
			break;
		case "addTask":
			addTask(req);
			break;
		case "editTask":
			editTask(req);
			break;
		case "deleteTask":
			deleteTask(req);
			break;
		}
		/*String key = req.getParameter("key");
		String name = req.getParameter("name");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity test = new Entity("TestEntry", "test1");
		Key k = KeyFactory.createKey("TestEntry", "test1");
		test.setProperty("Name", name);
		test.setProperty("Key", key);
		datastore.put(test);*/
	}
	
	private static void addUser(HttpServletRequest req){
		Entity user = new Entity("User", req.getParameter("Id"));
		String name = req.getParameter("Name");
		String email = req.getParameter("Email");
		user.setProperty("Name", name);
		user.setProperty("Email", email);
		datastore.put(user);
	}
	
	private static void addTask(HttpServletRequest req){
		Entity task = new Entity("Task");
		task.setProperty("Description", req.getParameter("Description"));
		task.setProperty("Category", req.getParameter("Category"));
		task.setProperty("Owner", req.getParameter("Owner"));
		String comp = req.getParameter("Complete");
		boolean bool;
		if(comp.equals("true"))
			bool = true;
		else
			bool = false;
		task.setProperty("Complete", bool);
		Key key = task.getKey();
		datastore.put(task);
	}
	
	private static void editTask(HttpServletRequest req){
		//Key k = stringToKey(req.getParameter("Key"));
		//Entity task = datastore.get(k);
	}
	
	private static void deleteTask(HttpServletRequest req){
		
	}
	
	public static void createDemoUsers(HttpServletResponse resp) throws IOException{
		createAndStoreEmily(resp);
		//createAndStorePhil(resp);
		//createAndStoreJoeBob(resp);
	}
	public static void retrieveAndPrintDemoInfo(HttpServletResponse resp) throws IOException, EntityNotFoundException{
		resp.getWriter().println();
		retrieveAndPrintEmily(resp);
		//resp.getWriter().println();
		//retrieveAndPrintPhil(resp);
		//resp.getWriter().println();
		//retrieveAndPrintJoeBob(resp);
	}
	public static void createAndStoreEmily(HttpServletResponse resp) throws IOException{
		User emily = new User(2604538654L, "Emily Roberts", "emily839@professionals.org");
		//emily.addTask(new Task("Play with puppies!", emily.getEmail()));
		storeUser(emily);
		resp.getWriter().println("Emily is now stored in the database!");
	}
	public static void retrieveAndPrintEmily(HttpServletResponse resp) throws IOException, EntityNotFoundException{
		//User emily = getUserInfo(key);
		//Entity emily = getUserInfo(key);
		resp.getWriter().println("Emily's info:");
		//resp.getWriter().println(emily.toString());
	}
	/*public static void createAndStorePhil(HttpServletResponse resp) throws IOException {
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
	}*/
	
	public static void storeUser(User user){
		
		/*PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			pm.makePersistent(user);
		}
		finally{
			pm.close();
		}*/
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity info = new Entity("User", user.getEmail());
		Key k = KeyFactory.createKey("User", user.getEmail());
		user.setKey(k);
		//key = k;
		info.setProperty("Name", user.getName());
		info.setProperty("Email", user.getEmail());
		info.setProperty("Phone Number", user.getPhone());
		//info.setProperty("Tasks", user.getTaskList());
		datastore.put(info);
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
	
	public static Entity getUserInfo(Key key) throws EntityNotFoundException{
		/*User ret_user;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			ret_user = pm.getObjectById(User.class, email);
		}
		finally{
			pm.close();
		}
		return ret_user;*/
		
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity ent = datastore.get(key);
		System.err.println("Got key!");
		//User user = new User((long)ent.getProperty("Phone Number"), (String)ent.getProperty("Name"), (String)ent.getProperty("Email"));
		return ent;
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
