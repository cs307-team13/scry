package edu.purdue.cs307.scry.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import edu.purdue.cs307.scry.HttpClientSetup;
import edu.purdue.cs307.scry.model.Task;

public class TaskDataSource {

    @Deprecated
    public String temp_friends[] = {"mhoward20158@gmail.com", "titanfan94@gmail.com"};
	
    private SQLiteDatabase taskdatabase;
    private SQLiteDatabase frienddatabase;
    
    private TaskStoreDbHelper taskdbHelper;
    private FriendStoreDbHelper frienddbHelper;
    
    public TaskDataSource(Context context) {
	taskdbHelper = new TaskStoreDbHelper(context);
	frienddbHelper = new FriendStoreDbHelper(context);
    }

    public void open() throws SQLException {
	taskdatabase = taskdbHelper.getWritableDatabase();
	frienddatabase = frienddbHelper.getWritableDatabase();
    }

    public void close() {
	taskdbHelper.close();
	frienddbHelper.close();
    }
    
    public long addFriend(String email)
    {
	ContentValues values = new ContentValues();
    	values.put(TaskStoreContract.FriendEntry.COLUMN_NAME_ENTRY_NAME, email);
    	
    	return frienddatabase.insert(TaskStoreContract.FriendEntry.TABLE_NAME, null,
    	        values);
    }
    
    public List<String> getFriends() {
	open();
	Cursor cursor = taskdatabase.query(TaskStoreContract.FriendEntry.TABLE_NAME,
		TaskStoreContract.FriendEntry.allColumns, null, null, null, null, null);

	List<String> friends = getAllFriendsFromCursor(cursor);
	return friends;
    }
    
    private List<String> getAllFriendsFromCursor(Cursor cursor) {
	List<String> friends = new ArrayList<String>();
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    String comment = cursorToFriend(cursor);
	    friends.add(comment);
	    cursor.moveToNext();
	}
	// make sure to close the cursor
	cursor.close();
	return friends;
    }

    private String cursorToFriend(Cursor cursor) {
	return cursor
	        .getString(cursor
	                .getColumnIndexOrThrow(TaskStoreContract.FriendEntry.COLUMN_NAME_ENTRY_NAME));
    }

    public long commitTask(Task t) {
	HttpClientSetup client = new HttpClientSetup();
	client.addTask(t);

	return commitTaskWithoutPush(t);
    }
    
    public long commitTaskWithoutPush(Task t) {
    	ContentValues values = new ContentValues();
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE, t.title);
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY,
    	        t.category);
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LAT,
    	        t.lat_location);
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LONG,
    	        t.long_location);
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CREATOR_ID,
    	        t.ownerId);
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_DATE_CREATED,
    	        t.getEntryDate());
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_COMPLETED,
    	        (t.isComplete()) ? 1 : 0);
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CREATOR_ID,
    	        t.getOwner());
    	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_UUID,
    	        t.getKey());

    	long id = taskdatabase.insert(TaskStoreContract.TaskEntry.TABLE_NAME, null,
    	        values);
    	t.setId(id);
    	return id;
    }

    public List<Task> getAllTasks() {
	open();
	Cursor cursor = taskdatabase.query(TaskStoreContract.TaskEntry.TABLE_NAME,
		TaskStoreContract.TaskEntry.allColumns, null, null, null, null, null);

	List<Task> comments = getAllTasksFromCursor(cursor);
	return comments;
    }

    public List<Task> getAllUnfinishedTasks() {
	open();
	Cursor cursor = taskdatabase.query(TaskStoreContract.TaskEntry.TABLE_NAME,
		TaskStoreContract.TaskEntry.allColumns,
	        TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_COMPLETED + "=?",
	        new String[] { "0" }, null, null, null);

	List<Task> comments = getAllTasksFromCursor(cursor);
	return comments;
    }

    public List<Task> getAllCompletedTasks() {
	open();
	Cursor cursor = taskdatabase.query(TaskStoreContract.TaskEntry.TABLE_NAME,
		TaskStoreContract.TaskEntry.allColumns,
	        TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_COMPLETED + "=?",
	        new String[] { "1" }, null, null, null);

	List<Task> comments = getAllTasksFromCursor(cursor);
	return comments;
    }

    private List<Task> getAllTasksFromCursor(Cursor cursor) {
	List<Task> comments = new ArrayList<Task>();
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

    public void purge() {
	Log.d("SQLite", "Purging all values from database");
	taskdatabase.delete(TaskStoreContract.TaskEntry.TABLE_NAME, null, null);
    }

    public List<String> getCategories() {
	// Cursor cursor = database.query(true, TaskStoreContract.TaskEntry.TABLE_NAME,
	// new String[] { TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY }, null,
	// null, null, null, TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY,
	// null);
	Cursor cursor = taskdatabase
	        .rawQuery(
	                "SELECT DISTINCT "
	                        + TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY
	                        + " FROM "
	                        + TaskStoreContract.TaskEntry.TABLE_NAME + ";",
	                null);

	cursor.moveToFirst();
	List<String> list = new ArrayList<String>();

	while (!cursor.isAfterLast()) {
	    String cat = cursor
		    .getString(cursor
		            .getColumnIndex(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY));
	    if (cat != null)
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
	task.setId(cursor.getLong(cursor
	        .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_ID)));

	task.ownerId = cursor
	        .getString(cursor
	                .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CREATOR_ID));
	
	task.setKey( cursor
	        .getString(cursor
	                .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CREATOR_ID)));

	task.setComplete(cursor.getInt(cursor
	        .getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_COMPLETED)) == 1);

	return task;
    }

    public void deleteTask(Task task) {
	long id = task.getId();
	System.out.println("Task deleted with id: " + id);
	taskdatabase.delete(TaskStoreContract.TaskEntry.TABLE_NAME,
	        BaseColumns._ID + " = " + id, null);
    }

    public List<Task> getTasksInCategory(String category) {
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

	Cursor cursor = taskdatabase.query(true,
	        TaskStoreContract.TaskEntry.TABLE_NAME, TaskStoreContract.TaskEntry.allColumns, selection,
	        selectionArgs, null, null, null, null);

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
