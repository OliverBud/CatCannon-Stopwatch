package com.example.tenthousand_hour_project;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.view.Menu;

public class List extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		SQLiteDatabase qdb = openDB();
		Cursor nameRecord = qdb.rawQuery("SELECT userName from user_table Where active = '1'", null);
		nameRecord.moveToFirst();
		String display = nameRecord.getString(nameRecord.getColumnIndexOrThrow("userName"));
		
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
