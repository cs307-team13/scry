package edu.purdue.cs307.scry;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskDataSource {
    private SQLiteDatabase database;
    private TaskStoreDbHelper dbHelper;
    private String[] allColumns = {
	    TaskStoreContract.TaskEntry._ID,
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
	values.put(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY, category);
	
	long insertId = database.insert(TaskStoreContract.TaskEntry.TABLE_NAME, null,
	        values);
	
	Cursor cursor = database.query(TaskStoreContract.TaskEntry.TABLE_NAME,
	        allColumns, TaskStoreContract.TaskEntry._ID + " = " + insertId, null,
	        null, null, null);
	cursor.moveToFirst();
	Task newTask = cursorToTask(cursor);
	cursor.close();
	return newTask;
    }

    public List<Task> getAllComments() {
	List<Task> comments = new ArrayList<Task>();

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
    
    private Task cursorToTask(Cursor cursor)
    {
	Task task = new Task(); 
	task.lat_location = cursor.getDouble(cursor.getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LAT));
	task.long_location = cursor.getDouble(cursor.getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LONG));
	task.title = cursor.getString(cursor.getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE));
	task.category = cursor.getString(cursor.getColumnIndexOrThrow(TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY));
	
	return task;
    }
}
