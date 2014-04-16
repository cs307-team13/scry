package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TaskDataSource {
    
    private SQLiteDatabase database;
    private TaskStoreDbHelper dbHelper;
    private String[] allColumns = { TaskStoreContract.TaskEntry._ID,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LAT,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LONG,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_DATE_CREATED,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_DATE_MODIFIED,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_EXPIRE_DATE,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CREATOR_ID,
	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY };

    public TaskDataSource(Context context) {
	dbHelper = new TaskStoreDbHelper(context);
    }

    public void open() throws SQLException {
	database = dbHelper.getWritableDatabase();
    }

    public void close() {
	dbHelper.close();
    }

    public Task createComment(String name, String category) {
	ContentValues values = new ContentValues();
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE, name);
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY,
	        category);

	long insertId = database.insert(TaskStoreContract.TaskEntry.TABLE_NAME,
	        null, values);

	Cursor cursor = database.query(TaskStoreContract.TaskEntry.TABLE_NAME,
	        allColumns, TaskStoreContract.TaskEntry._ID + " = " + insertId,
	        null, null, null, null);
	cursor.moveToFirst();
	Task newTask = cursorToTask(cursor);
	HttpClientSetup client = new HttpClientSetup();
	client.addTask(newTask);
	cursor.close();
	return newTask;
    }

    public long commitTask(Task t)
    {
	ContentValues values = new ContentValues();
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE, t.title);
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY, t.category);
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LAT, t.lat_location);
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LONG, t.long_location);
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CREATOR_ID, t.ownerId);
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_DATE_CREATED, t.getEntryDate());
	
	HttpClientSetup client = new HttpClientSetup();
	client.addTask(t);
	
	return database.insert(TaskStoreContract.TaskEntry.TABLE_NAME,
	        null, values);
    }

    public List<Task> getAllTasks() {
	List<Task> comments = new ArrayList<Task>();
	open();
	Cursor cursor = database.query(TaskStoreContract.TaskEntry.TABLE_NAME,
	        allColumns, null, null, null, null, null);

	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    Task comment = cursorToTask(cursor);
	    comments.add(comment);
	    cursor.moveToNext();
	}
	// make sure to close the cursor
	cursor.close();
	return comments;
    }

    public Cursor getAllTasksAsCursor() {

	Cursor cursor = database.query(TaskStoreContract.TaskEntry.TABLE_NAME,
	        allColumns, null, null, null, null, null);
	cursor.moveToFirst();

	return cursor;
    }

    public void purge() {
	Log.d("SQLite", "Purging all values fron database");
	database.delete(TaskStoreContract.TaskEntry.TABLE_NAME, null, null);
    }

    public List<String> getCategories() {
	// Cursor cursor = database.query(true, TaskStoreContract.TaskEntry.TABLE_NAME,
	// new String[] { TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY }, null,
	// null, null, null, TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY,
	// null);
	Cursor cursor = database
	        .rawQuery(
	                "SELECT DISTINCT "
	                        + TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY
	                        + " FROM "
	                        + TaskStoreContract.TaskEntry.TABLE_NAME + ";",
	                null);

	cursor.moveToFirst();
	List<String> list = new ArrayList<String>();

	while (!cursor.isAfterLast()) {
	    String cat = cursor.getString(cursor
		    .getColumnIndex(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY));
	    if(cat != null)
		list.add(cat); 
	    cursor.moveToNext();
	}

	return list;
    }

    public Task cursorToTask(Cursor cursor) {
	Task task = new Task();
	task.lat_location = cursor
	        .getDouble(cursor
	                .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LAT));
	task.long_location = cursor
	        .getDouble(cursor
	                .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LONG));
	task.title = cursor
	        .getString(cursor
	                .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE));
	task.category = cursor
	        .getString(cursor
	                .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY));

	return task;
    }

    public void deleteTask(Task task) {
	long id = task.getId();
	System.out.println("Task deleted with id: " + id);
	database.delete(TaskStoreContract.TaskEntry.TABLE_NAME,
	        TaskStoreContract.TaskEntry._ID + " = " + id, null);
    }

    public List<Task> getTasksInCategory(String category)
    {
	String selection = TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY
	        + " LIKE ?";
	String[] selectionArgs = new String[] { category };

	return query(selection, selectionArgs, null);
    }
    
    
    public List<Task> getTaskMatches(String query, String[] columns) {
	String selection = TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE
	        + " LIKE ?";
	String[] selectionArgs = new String[] { "%" + query + "%" };

	return query(selection, selectionArgs, columns);
    }

    private List<Task> query(String selection, String[] selectionArgs,
	    String[] columns) {

	Cursor cursor = database.query(true,
	        TaskStoreContract.TaskEntry.TABLE_NAME, allColumns,
	        selection, selectionArgs, null, null, null, null);

	if (cursor == null) {
	    return null;
	} else if (!cursor.moveToFirst()) {
	    cursor.close();
	    return null;
	}
	List<Task> tasks = new ArrayList<Task>(); 
	
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    Task comment = cursorToTask(cursor);
	    tasks.add(comment);
	    cursor.moveToNext();
	}
	
	
	return tasks;
    }
}
