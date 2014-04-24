package scryserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.*;
import com.google.appengine.api.datastore.PreparedQuery;

//import edu.purdue.cs307.scry;

@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {
	
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("This is the server for the Scry Android application. Coming April 2014.");
		
		String method_call = req.getParameter("Method");
		switch(method_call){
		case "getTasksByUser":
			getTasksByUser(req);
			break;
		case "getUserInfo":
			getUserInfo(req);
		}
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
		task.setProperty("Owner", req.getParameter("Owner"));
		task.setProperty("Complete", req.getParameter("Complete"));
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
	
	private static List<Entity> getTasksByUser(HttpServletRequest req){
		Filter ownerFilter = new FilterPredicate("Owner", FilterOperator.EQUAL, req.getParameter("Owner"));
		Query q = new Query("Task").setFilter(ownerFilter);
		PreparedQuery pq = datastore.prepare(q);
		//send list to client
		return pq.asList(FetchOptions.Builder.withLimit(1024));
	}
	
	private static List<Task> convertEntitiesToTasks(List<Entity> entities){ //Needs to go in client end, send Tasks as response to client
		List<Task> tasks = new ArrayList<Task>();
		for(Entity e : entities){
			//Task temp = generateTask(e);
			//tasks.add(temp);
		}
		return tasks;
	}
	
	private static Entity getUserInfo(HttpServletRequest req) {
		Filter filter = new FilterPredicate("Email", FilterOperator.EQUAL, req.getParameter("Email"));
		Query q = new Query("User").setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}
}
