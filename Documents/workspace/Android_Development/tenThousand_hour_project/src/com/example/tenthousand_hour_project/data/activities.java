package com.example.tenthousand_hour_project.data;

import android.database.sqlite.SQLiteDatabase;

public class activities {
	  public static final String TABLE_ACTIVITIES = "activity_table";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_ACTIVITY_NAME = "activity_name";
	  public static final String COLUMN_ACTIVITY_TIME = "activity_time";
	  public static final String COLUMN_ACTIVITY_ID = "activity_id";
	  public static final String COLUMN_USER_ID = "user_id";
	  public static final String COLUMN_ACTIVE = "active";
	  
	  private static final String DATABASE_CREATE = "create table " 
		      + TABLE_ACTIVITIES
		      + "(" 
		      + COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_ACTIVE + " int," 
		      + COLUMN_ACTIVITY_NAME + " text not null,"
		      + COLUMN_ACTIVITY_TIME + " real," 
		      + COLUMN_ACTIVITY_ID + " int," 
		      + COLUMN_USER_ID+ " int" + ");";
	  
	  public static void onCreate(SQLiteDatabase database) {
		    database.execSQL(DATABASE_CREATE);
		  }
}
