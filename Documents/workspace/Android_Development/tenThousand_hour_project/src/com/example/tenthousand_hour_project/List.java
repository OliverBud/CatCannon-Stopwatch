package com.example.tenthousand_hour_project;

import java.util.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;


public class List extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		SQLiteDatabase qdb = openDB();
		
		
		Cursor idRecord = qdb.rawQuery("SELECT user_id from user_table Where active = '1'", null);
		int active_id = idRecord.getInt(idRecord.getColumnIndexOrThrow("user_id"));
		
		final ListView listview = (ListView) findViewById(R.id.listview);
		Cursor individual_list = qdb.rawQuery("SELECT (activity_name, activity_time) FROM activity_table WHERE user_id = '" + active_id + "'", null);
		int numRows = individual_list.getCount();
		String[] name_list = new String[numRows];
		int[] time_list = new int[numRows];
		individual_list.moveToFirst();
		for (int row = 0; row < numRows; row ++){
			name_list[row] = individual_list.getString(individual_list.getColumnIndexOrThrow("activity_name"));
			time_list[row] = individual_list.getInt(individual_list.getColumnIndexOrThrow("activity_time"));
		}
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name_list));
		
		
		
		Cursor nameRecord = qdb.rawQuery("SELECT userName from user_table Where active = '1'", null);
		nameRecord.moveToFirst();
		
		String display = nameRecord.getString(nameRecord.getColumnIndexOrThrow("userName"));
    	EditText display_text = (EditText) findViewById(R.id.display_text);
    	display_text.setText(display);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	
	
	
	
	private SQLiteDatabase openDB(){
		Database db = new Database(this);
		SQLiteDatabase qdb = db.getWritableDatabase();
		return qdb;
  	
	}
	
	public void switchActivity(){
		Intent intent = new Intent(this, List.class);
		startActivity(intent);	    
	}
	
	

}
