package com.example.tenthousand_hour_project.data;

import android.database.sqlite.SQLiteDatabase;

public class users {
	  public static final String TABLE_USERS = "user_table";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_USER_NAME = "user_name";
	  public static final String COLUMN_PASSWORD = "password";
	  public static final String COLUMN_USER_ID = "user_id";
	  public static final String COLUMN_ACTIVE = "active";
	  
	  private static final String DATABASE_CREATE = "create table " 
		      + TABLE_USERS
		      + "(" 
		      + COLUMN_ID + " integer primary key autoincrement, " 
		      + COLUMN_ACTIVE + " int," 
		      + COLUMN_USER_NAME + " text not null,"
		      + COLUMN_PASSWORD + " text not null," 
		      + COLUMN_USER_ID+ " int" + ");";
	  
	  public static void onCreate(SQLiteDatabase database) {
		    database.execSQL(DATABASE_CREATE);
		  }

}
