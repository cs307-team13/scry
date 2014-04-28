package edu.purdue.cs307.scry.data;
import android.provider.BaseColumns;


public final class TaskStoreContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public TaskStoreContract() {}

    /* Inner class that defines the table contents */
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task_store";
        public static final String COLUMN_NAME_ENTRY_ID = "_id";
        public static final String COLUMN_NAME_ENTRY_TITLE = "title";
        public static final String COLUMN_NAME_ENTRY_LOC_LAT = "lat";
        public static final String COLUMN_NAME_ENTRY_LOC_LONG = "long";
        public static final String COLUMN_NAME_ENTRY_DATE_CREATED = "date_created";
        public static final String COLUMN_NAME_ENTRY_DATE_MODIFIED = "date_mod";
        public static final String COLUMN_NAME_ENTRY_EXPIRE_DATE = "date_expire";
        //If the task is only relevant for a period of time. This is not age of task. 
        public static final String COLUMN_NAME_ENTRY_CREATOR_ID = "creator_id"; 
        //this could be changed for a different organizational scheme...
        public static final String COLUMN_NAME_ENTRY_CATEGORY = "category";
        public static final String COLUMN_NAME_ENTRY_UUID = "uuid";
        public static final String COLUMN_NAME_ENTRY_COMPLETED = "completed";

        
        
        private static final String TEXT_TYPE = " TEXT";
    	private static final String REAL_TYPE = " REAL";
    	private static final String INTEGER_TYPE = " INTEGER";
    	private static final String COMMA_SEP = ",";
    	public static final String SQL_CREATE_ENTRIES =
    	    "CREATE TABLE " + TaskStoreContract.TaskEntry.TABLE_NAME + " (" +
    	    TaskStoreContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_TITLE + TEXT_TYPE + COMMA_SEP + 
    	    // Any other options for the CREATE command
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LAT + REAL_TYPE + COMMA_SEP +
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_LOC_LONG + REAL_TYPE + COMMA_SEP +
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_DATE_CREATED + TEXT_TYPE + COMMA_SEP +
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_DATE_MODIFIED + TEXT_TYPE + COMMA_SEP +
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_EXPIRE_DATE + TEXT_TYPE + COMMA_SEP +
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CREATOR_ID + TEXT_TYPE + COMMA_SEP +
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_CATEGORY + TEXT_TYPE + COMMA_SEP + 
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_UUID + TEXT_TYPE + COMMA_SEP + 
    	    TaskStoreContract.TaskEntry.COLUMN_NAME_ENTRY_COMPLETED + INTEGER_TYPE + 
    	    " )";
    	
    	public static final String SQL_DELETE_ENTRIES =
    		    "DROP TABLE IF EXISTS " + TaskStoreContract.TaskEntry.TABLE_NAME;
    		
    		
    }
}
