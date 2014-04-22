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
		Entity task = new Entity("Task", req.getParameter("ID"));
		task.setProperty("Description", req.getParameter("Description"));
		task.setProperty("Category", req.getParameter("Category"));
		//task.setProperty("Owner", req.getParameter("Owner"));
		/*String comp = req.getParameter("Complete");
		boolean bool;
		if(comp.equals("true"))
			bool = true;
		else
			bool = false;
		task.setProperty("Complete", bool);
		Key key = task.getKey();*/
		datastore.put(task);
	}
	
	private static void editTask(HttpServletRequest req){
		deleteTask(req);
		addTask(req);
	}
	
	private static void deleteTask(HttpServletRequest req){
		String id = req.getParameter("ID");
		Key key = KeyFactory.stringToKey(id);
		datastore.delete(key);
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
}
