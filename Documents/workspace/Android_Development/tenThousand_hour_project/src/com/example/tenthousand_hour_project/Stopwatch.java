package com.example.tenthousand_hour_project;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Chronometer;

public class Stopwatch extends Activity {

	long elapsed;
	String activity_name;
	String arctive_activity_name;
	SQLiteDatabase qdb;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stopwatch);
		
		Button start_button = (Button) findViewById(R.id.start_button);
		Button stop_button = (Button) findViewById(R.id.stop_button);
		TextView small_text = (TextView) findViewById(R.id.small_text);
		Chronometer timekeeper = (Chronometer) findViewById(R.id.timekeeper);
		start_button.setOnClickListener(start_listener);
		stop_button.setOnClickListener(stop_listener);
		
		Database db = new Database(this);
		qdb = db.getWritableDatabase();
		
		Cursor active_activity = qdb.rawQuery("SELECT activity_name FROM activity_table " +
				"WHERE active = 1", null);
		activity_name = active_activity.getString(active_activity.getColumnIndexOrThrow(
				"activity_name"));
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stopwatch, menu);
		return true;
	}

	private OnClickListener start_listener = new OnClickListener() {
		public void onClick(View v) {
			Chronometer timekeeper = (Chronometer) findViewById(R.id.timekeeper);

			timekeeper.setBase(0);
			timekeeper.start();
		}
	};
	
	private OnClickListener stop_listener = new OnClickListener() {
		public void onClick(View v) {
			Chronometer timekeeper = (Chronometer) findViewById(R.id.timekeeper);

			timekeeper.stop();
			elapsed = timekeeper.getBase();
			
			Cursor timespot = null;
			timespot = qdb.rawQuery("SELECT activity_time FROM activity_table WHERE " +
					"activity_name = '" + activity_name + "'", null);
			timespot.moveToFirst();
			
			long the_time = timespot.getLong(timespot.getColumnIndexOrThrow("activity_time"));
			ContentValues update_time = new ContentValues();
			update_time.put("activity_time", the_time + elapsed);
		}
	};
	
	
}
