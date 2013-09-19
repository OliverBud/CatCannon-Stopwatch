package com.example.tenthousand_hour_project;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;

public class Timer extends Activity {
	
	SQLiteDatabase qdb;
	long elapsed;
	String activity_name;
	String arctive_activity_name;
	long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);
		Button start_button = (Button) findViewById(R.id.start);
		Button stop_button = (Button) findViewById(R.id.stop);
		//Chronometer Chrono = (Chronometer) findViewById(R.id.chronometer);
		
		qdb = openDB();
		
		start_button.setOnClickListener(start_listener);
		stop_button.setOnClickListener(stop_listener);
		
		Cursor active_activity = qdb.rawQuery("SELECT activity_name FROM activity_table " +
				"WHERE active = 1", null);
		active_activity.moveToFirst();
		int Checking = active_activity.getCount();
		activity_name = active_activity.getString(0);

		
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        Cursor active_activity = qdb.rawQuery("SELECT activity_name FROM activity_table " +
				"WHERE active = 1", null);
		active_activity.moveToFirst();
		int Checking = active_activity.getCount();
		activity_name = active_activity.getString(0);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timer, menu);
		return true;
	}
	
	private SQLiteDatabase openDB(){
		Database db = new Database(this);
		SQLiteDatabase qdb = db.getWritableDatabase();
		return qdb;
  	
	}
	
	private OnClickListener start_listener = new OnClickListener() {
	    public void onClick(View v) {
	    	EditText timekeeper = (EditText) findViewById(R.id.chronometer);

			timekeeper.setText("0");
			time = System.currentTimeMillis();
	    	
	    }
	};
	
	private OnClickListener stop_listener = new OnClickListener() {
	    public void onClick(View v) {
	    	EditText timekeeper = (EditText) findViewById(R.id.chronometer);

			elapsed = System.currentTimeMillis() - time;
			timekeeper.setText(Long.toString(elapsed));
			
			Cursor timespot = null;
			timespot = qdb.rawQuery("SELECT activity_time FROM activity_table WHERE " +
					"activity_name = '" + activity_name + "'", null);
			timespot.moveToFirst();
			int check = timespot.getCount();
			
			long the_time = timespot.getLong(timespot.getColumnIndexOrThrow("activity_time"));
			ContentValues update_time = new ContentValues();
			update_time.put("activity_time", the_time + elapsed);
			String where = "activity_name=?";
			String[] whereArgs = {activity_name};
			qdb.update("activity_table", update_time, where, whereArgs);

	    }
	};
	

	
	
}
