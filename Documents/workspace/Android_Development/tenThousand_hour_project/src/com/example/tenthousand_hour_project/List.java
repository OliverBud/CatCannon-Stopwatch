package com.example.tenthousand_hour_project;

import java.util.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.EditText;
import android.widget.ListView;

public class List extends Activity {
	Button submit_name_button;
	EditText new_name_text;
	final ListView activity_list = (ListView) findViewById(R.id.listview);
	int active_id;
	SQLiteDatabase qdb;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		qdb = openDB();
		submit_name_button = (Button) findViewById(R.id.submit_name);
		new_name_text = (EditText) findViewById(R.id.new_name_box);
		submit_name_button.setOnClickListener(SubmitListener);
		
		Cursor idRecord = qdb.rawQuery("SELECT user_id from user_table Where active = '1'", null);
		idRecord.moveToFirst();
		active_id = idRecord.getInt(idRecord.getColumnIndexOrThrow("user_id"));
		
		
		
		Cursor individual_name_list = qdb.rawQuery("SELECT activity_name FROM activity_table WHERE user_id = '" + active_id + "'", null);
		Cursor individual_time_list = qdb.rawQuery("SELECT activity_time FROM activity_table WHERE user_id = '" + active_id + "'", null);
		
		int numRows = individual_name_list.getCount();
		String[] name_list = new String[numRows];
		int[] time_list = new int[numRows];
		
		individual_name_list.moveToFirst();
		individual_time_list.moveToFirst();
		
		for (int row = 0; row < numRows; row ++){
			name_list[row] = individual_name_list.getString(individual_name_list.getColumnIndexOrThrow("activity_name"));
			time_list[row] = individual_time_list.getInt(individual_time_list.getColumnIndexOrThrow("activity_time"));
			individual_name_list.moveToNext();
			individual_time_list.moveToNext();
		}
		
		activity_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name_list));
		
		
		Cursor nameRecord = qdb.rawQuery("SELECT userName from user_table Where active = '1'", null);
		nameRecord.moveToFirst();
		
		String display = nameRecord.getString(nameRecord.getColumnIndexOrThrow("userName"));
    	/*
		EditText display_text = (EditText) findViewById(R.id.display_text);
    	display_text.setText(display);
    	*/
		displayDB(qdb);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	private OnClickListener SubmitListener = new OnClickListener() {
	    public void onClick(View v) {
	    	String new_name = new_name_text.toString();
	    	
	    	ContentValues new_activity = new ContentValues();
	    	new_activity.put("user_id", active_id);
	    	new_activity.put("activity_time", 0);
	    	new_activity.put("activity_name", new_name);
	    	new_activity.put("active", 0);
	    	qdb.insert("activity_table", null, new_activity);
	    	
	    	displayDB(qdb);
	   	}
	};
	
	private OnItemClickListener list_listener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String item_name = (String) arg0.getItemAtPosition(0);			
			ContentValues update_active = new ContentValues();
			update_active.put("active", 1);
			String where = "activity_name=?";
			String[] whereArgs = new String[] {item_name};
			qdb.update("activity_table", update_active, where, whereArgs);
			switchActivity();
		}
		
		
	};
	
	
	
	private SQLiteDatabase openDB(){
		Database db = new Database(this);
		SQLiteDatabase qdb = db.getWritableDatabase();
		return qdb;
  	
	}
	
	public void switchActivity(){
		Intent intent = new Intent(this, Stopwatch.class);
		startActivity(intent);	    
	}
	
	public void displayDB(SQLiteDatabase qdb){
		Cursor recordSet = qdb.rawQuery("Select * FROM activity_table", null);
		int numRows = recordSet.getCount();
		recordSet.moveToFirst();
		EditText display_text = (EditText) findViewById(R.id.DB_display);
		display_text.setLines(numRows + 1);
		String aName;
		long aTime;
		int aUser_id;
		int aActive;
		String Text;
		Text = Integer.toString(numRows) + "\n";
		for (int i = 0; i < numRows; i ++){
			aName = recordSet.getString(recordSet.getColumnIndexOrThrow("activity_name"));
			aTime = recordSet.getLong(recordSet.getColumnIndexOrThrow("activity_time"));
			aUser_id = recordSet.getInt(recordSet.getColumnIndexOrThrow("user_id"));
			aActive = recordSet.getInt(recordSet.getColumnIndexOrThrow("active"));
			Text = Text + aName + " " + Long.toString(aTime) + " " + Integer.toString(aUser_id) + " " + Integer.toString(aActive) + "\n";	
			recordSet.moveToNext();
		}
		
		display_text.setText(Text);
	}
	
	

}
