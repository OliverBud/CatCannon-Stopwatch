/*
 * This activity will always be called from the login activity
 * displays a list of the user's activities with their total time
 * and a button + textView to add new activities
 */

package com.example.tenthousand_hour_project;

import java.util.*;

//import org.achartengine.model.TimeSeries;

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

public class List1 extends Activity {
	
	int active_id;
	
	SQLiteDatabase qdb;
	ArrayList<String> list;
	ArrayList<activity_summary> real_list;
	custom_adapter custom;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		qdb = openDB();
		
		Button submit_button = (Button) findViewById(R.id.new_name_button);
		submit_button.setOnClickListener(SubmitListener);
		
	
		//Load active profile id.
		Cursor idRecord = qdb.rawQuery("SELECT user_id from user_table Where active = '1'", null);
		idRecord.moveToFirst();
		active_id = idRecord.getInt(idRecord.getColumnIndexOrThrow("user_id"));
		
		//set click handler for list
		final ListView activity_list = (ListView) findViewById(R.id.listview);
		activity_list.setOnItemClickListener(list_listener);
		
		//load activities that this user owns
		Cursor individual_name_list = qdb.rawQuery("SELECT activity_name FROM activity_table WHERE user_id = '" + active_id + "'", null);
		Cursor individual_time_list = qdb.rawQuery("SELECT activity_time FROM activity_table WHERE user_id = '" + active_id + "'", null);
		
		int numRows = individual_name_list.getCount();
		String[] name_list = new String[numRows];
		int[] time_list = new int[numRows];
		
		individual_name_list.moveToFirst();
		individual_time_list.moveToFirst();
		
		//put user activities into wrapper object
		activity_summary summary_data[] = new activity_summary[numRows];
		
		//populate activity collection
		for (int row = 0; row < numRows; row ++){
			name_list[row] = individual_name_list.getString(individual_name_list.getColumnIndexOrThrow("activity_name"));
			time_list[row] = individual_time_list.getInt(individual_time_list.getColumnIndexOrThrow("activity_time"));
			individual_name_list.moveToNext();
			individual_time_list.moveToNext();
			summary_data[row] = new activity_summary(name_list[row], Integer.toString(time_list[row]));
		
		
		}
		
		//make activity collection into ArrayList
		real_list = new ArrayList<activity_summary>();
		real_list.addAll(Arrays.asList(summary_data));
		
		//instantiate new custom adapter to fill the  custom ListVew
		custom = new custom_adapter(this, R.layout.row_layout, real_list);
		activity_list.setAdapter(custom);
		
		
		
		

	}
	
	
	@Override
    protected void onResume() {
        super.onResume();

		onCreate(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	//Click handler for ListView
	private OnItemClickListener list_listener = new OnItemClickListener(){

		//given the position on the list of the user click, 
		//extrapolate the activity being referred to
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			ListView act_list = (ListView) findViewById(R.id.listview);
			activity_summary activity_summary = (activity_summary) act_list.getItemAtPosition(position);
			String activity_name = activity_summary.name;
			
			//set all activities to inactive
			ContentValues resetActive = new ContentValues();
			resetActive.put("active", 0);
			qdb.update("activity_table", resetActive, null, null);
			
			//set user choice activity to active
			ContentValues active_activity = new ContentValues();
			active_activity.put("active", 1);
			String where = "activity_name=?";
			String[] whereArgs= {activity_name};
			qdb.update("activity_table", active_activity, where, whereArgs);

			//load timer activity
			switchActivity();
			
			
		}
		
	};
	
	//Click handler for new activity
	private OnClickListener SubmitListener = new OnClickListener() {
	    public void onClick(View v) {
	    	EditText new_name_text = (EditText) findViewById(R.id.new_name_box);
	    	String new_name = new_name_text.getText().toString();
	    	ContentValues add_activity = new ContentValues();
	    	add_activity.put("activity_name", new_name);
	    	add_activity.put("activity_time", 0);
	    	add_activity.put("user_id", active_id);
	    	add_activity.put("active", 0);
	    	qdb.insert("activity_table", null, add_activity);
	    	list.add(new_name);
	    	real_list.add(new activity_summary(new_name, Integer.toString(0)));
	    	custom.notifyDataSetChanged();
	    
	    }
	};
	
	
	
	private SQLiteDatabase openDB(){
		Database db = new Database(this);
		SQLiteDatabase qdb = db.getWritableDatabase();
		return qdb;
  	
	}
	
	public void switchActivity(){
		Intent intent = new Intent(this, Timer1.class);
		startActivity(intent);	    
	}
	
	

}
