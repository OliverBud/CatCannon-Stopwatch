package com.example.tenthousand_hour_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class Database extends SQLiteOpenHelper {
	 
    final static int DB_VERSION = 1;
    final static String DB_NAME = "StopWatch_database";
    Context context;
     
    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        // Store the context for later use
        this.context = context;
        
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		arg0.execSQL("CREATE TABLE user_table(userName TEXT, password TEXT, user_id int, active int)");
		arg0.execSQL("CREATE TABLE activity_table(user_id int, activity_name TEXT, activity_time real)");
		ContentValues dummyContent = new ContentValues();
		dummyContent.put("userName", "dummy");
		dummyContent.put("password", "pass");
		dummyContent.put("user_id", 0);
		dummyContent.put("active", 0);
		arg0.insert("user_table", null, dummyContent);

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		arg0.execSQL("DROP TABLE IF EXISTS user_table");
		arg0.execSQL("DROP TABLE IF EXISTS activity_table");
		

		onCreate(arg0);
	}
	
}
