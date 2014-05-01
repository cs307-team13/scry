package scryserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.*;

import org.mortbay.log.Log;

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
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

//import edu.purdue.cs307.scry;

@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(ServerServlet.class.getName());
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		//resp.setContentType("text/plain");
		//resp.getWriter().println("This is the server for the Scry Android application. Coming April 2014.");
		String method_call;
		if( (method_call = req.getParameter("Method")) != null){
			switch(method_call){
			case "getTaskByUser":
				getTasksByUser(req, resp);
				break;
			case "getUserInfo":
				getUserInfo(req, resp);
				break;
			case "getUserId":
				getUserID(req, resp);
				break;
			}
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
	
	private static String getUserID(HttpServletRequest req, HttpServletResponse resp){
		Filter f = new FilterPredicate("Email", FilterOperator.EQUAL, req.getParameter("Email"));
		Query q = new Query("User").setFilter(f);
		PreparedQuery pq = datastore.prepare(q);
		Entity result = pq.asSingleEntity();
		String id = (String) result.getKey().toString();
		int start = id.indexOf("\"");
		id = id.substring(start);
		id = id.replaceAll("[\")]", "");
		log.info("Sending user id: " + id.toString());
		resp.setHeader("User-id", "User ID: " + id);
		resp.setContentLength(id.length()+1000);
		resp.setContentType("text/plain");
		try {
			resp.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return id;
	}
	
	private static String getTasksByUser(HttpServletRequest req, HttpServletResponse resp){
		Filter ownerFilter = new FilterPredicate("Owner", FilterOperator.EQUAL, req.getParameter("Owner"));
		log.info("Getting tasks for user: " + req.getParameter("Owner"));
		Query q = new Query("Task").setFilter(ownerFilter);
		PreparedQuery pq = datastore.prepare(q);
		//send list to client
		try {
			List<JSONObject> json_list = convertTaskEntitiesToJSON(pq.asList(FetchOptions.Builder.withLimit(1024)));
			//String s = manualConversion(pq.asList(FetchOptions.Builder.withLimit(1024))).toString(); //json_list.toString();
			String s = json_list.toString();
			String s1 = s.replaceAll(",", "|");
			resp.setHeader("Message", "Message: " + s1);
			resp.setContentType("text/plain");
			resp.setContentLength(json_list.toString().length() + 1000);
			//resp.getWriter().write("Hello application"); //json_list.toString());
			//resp.getOutputStream().write(json_list.toString().getBytes());
			//resp.getOutputStream().flush();
			resp.getOutputStream().close();
			log.info("Sending to client: " + s1);
		} catch (IOException e) {
			System.out.println("Error sending tasks by user");
		}
		return pq.asList(FetchOptions.Builder.withLimit(1024)).toString();
	}
	
	private static List<Task> convertEntitiesToTasks(List<Entity> entities){ //Needs to go in client end, send Tasks as response to client
		List<Task> tasks = new ArrayList<Task>();
		for(Entity e : entities){
			//Task temp = generateTask(e);
			//tasks.add(temp);
		}
		return tasks;
	}
	
	private static List<String> manualConversion(List<Entity> entities){
		List<String> tasks = new ArrayList<String>();
		for(Entity e : entities){
			String cat = (String) e.getProperty("Category");
			String complete = (String) e.getProperty("Complete");
			String description = (String) e.getProperty("Description");
			String owner = (String) e.getProperty("Owner");
			String key = e.getKey().toString();
			String t = "(Description : " + description + "| Category : " + cat + ")";
			tasks.add(t);
		}
		return tasks;
	}
	
	private static List<JSONObject> convertTaskEntitiesToJSON(List<Entity> entities){
		List<JSONObject> json = new ArrayList<JSONObject>();
		for(Entity e : entities){
			String cat = (String) e.getProperty("Category");
			String complete = (String) e.getProperty("Complete");
			String description = (String) e.getProperty("Description");
			String owner = (String) e.getProperty("Owner");
			String key = e.getKey().toString();
			JSONObject j = new JSONObject();
			try {
				j.put("category", cat);
				//j.put("complete", complete);
				j.put("title", description);
				//j.put("ownerId", owner);
				//j.put("key", key);
			} catch (JSONException e1) {
			}
			json.add(j);
		}
		return json;
	}
	
	private static Entity getUserInfo(HttpServletRequest req, HttpServletResponse resp) {
		Filter filter = new FilterPredicate("Email", FilterOperator.EQUAL, req.getParameter("Email"));
		Query q = new Query("User").setFilter(filter);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}
}
