package com.example.tenthousand_hour_project;

import java.util.*;  

import com.example.tenthousand_hour_project.data.Contract;
import com.example.tenthousand_hour_project.data.Database;

//import org.achartengine.model.TimeSeries;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
	
	//SQLiteDatabase qdb;
	ArrayList<String> list;
	ArrayList<activity_summary> real_list;
	custom_adapter custom;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		//qdb = openDB();
		Log.d("cp", "ListStart");
		//Button submit_button = (Button) findViewById(R.id.new_name_button);
		//submit_button.setOnClickListener(SubmitListener);
		
	
		//Load active profile id.
		//Cursor idRecord = qdb.rawQuery("SELECT user_id from user_table Where active = '1'", null);
		String[] projection = {"user_id"};
		String where = "active = ?";
		String[] whereArgs = {"1"};
		Cursor idRecord = getContentResolver().query(Contract.users.CONTENT_URI,
				projection, where, whereArgs, null);
		
		idRecord.moveToFirst();
		active_id = idRecord.getInt(idRecord.getColumnIndexOrThrow("user_id"));
		
		//set click handler for list
		final ListView activity_list = (ListView) findViewById(R.id.listview);
		activity_list.setOnItemClickListener(list_listener);
		
		//load activities that this user owns
		String[] activities_projection = {"activity_name", "activity_time"};
		String activities_where = "user_id = ?";
		String[] activities_whereArgs = {Integer.toString(active_id)};
		
		Cursor activities_cursor = getContentResolver().query(Contract.activities.CONTENT_URI, 
				activities_projection, 
				activities_where, 
				activities_whereArgs, 
				null);
		//Cursor individual_name_list = qdb.rawQuery("SELECT activity_name FROM activity_table WHERE user_id = '" + active_id + "'", null);
		//Cursor individual_time_list = qdb.rawQuery("SELECT activity_time FROM activity_table WHERE user_id = '" + active_id + "'", null);
		
		int numRows = activities_cursor.getCount();
		String[] name_list = new String[numRows];
		int[] time_list = new int[numRows];
		
		activities_cursor.moveToFirst();
		
		//put user activities into wrapper object
		activity_summary summary_data[] = new activity_summary[numRows];
		
		//populate activity collection
		for (int row = 0; row < numRows; row ++){
			name_list[row] = activities_cursor.getString(activities_cursor.getColumnIndexOrThrow("activity_name"));
			time_list[row] = activities_cursor.getInt(activities_cursor.getColumnIndexOrThrow("activity_time"));
			activities_cursor.moveToNext();
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        
        case R.id.action_logout:
            Log.d("button", "i pressed a logout button");
            NavUtils.navigateUpFromSameTask(this);
            return true;
        case R.id.action_add_item:
            Log.d("button", "i pressed a plus sign");
            
            Intent intent = new Intent(this, Add_Item.class);
            intent.putExtra("active", active_id);
            intent.putExtra("activty_id", real_list.size());           
    		startActivityForResult(intent, 0);	
    		
    		
    		
        	return true;
        default:
            return super.onOptionsItemSelected(item);
    }
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
			
			int rowsUpdated1 = getContentResolver().update(Contract.activities.CONTENT_URI,
					resetActive, 
					null,
					null);
			//qdb.update("activity_table", resetActive, null, null);
			
			//set user choice activity to active
			ContentValues active_activity = new ContentValues();
			active_activity.put("active", 1);
			String where = "activity_name=?";
			String[] whereArgs= {activity_name};
			int rowsUpdated = getContentResolver().update(Contract.activities.CONTENT_URI, 
					active_activity, 
					where, 
					whereArgs);
			//qdb.update("activity_table", active_activity, where, whereArgs);

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
	    	add_activity.put("activity_id", real_list.size());

	    	
	    	Uri newActivity = getContentResolver().insert(Contract.activities.CONTENT_URI, add_activity);
	    	//qdb.insert("activity_table", null, add_activity);
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
		startActivityForResult(intent, 0);	    
	}
	
	

}
