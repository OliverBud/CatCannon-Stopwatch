package com.example.tenthousand_hour_project;

import com.example.tenthousand_hour_project.data.Contract;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Add_Item extends Activity {

	int active_id;
	int activity_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.d("add_item", "in activity");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		 Bundle extras = getIntent().getExtras();
		 //active_id = extras.getInt("active");
		 //activity_id = extras.getInt("activity_id");
		
		Button submit_button = (Button) findViewById(R.id.new_name_button);
		
		
		submit_button.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	EditText new_name_text = (EditText) findViewById(R.id.new_name_box);
		    	String new_name = new_name_text.getText().toString();
		    	ContentValues add_activity = new ContentValues();
		    	add_activity.put("activity_name", new_name);
		    	add_activity.put("activity_time", 0);
		    	add_activity.put("user_id", active_id);
		    	add_activity.put("active", 0);
		    	add_activity.put("activity_id", activity_id);    	
		    	Uri newActivity = getContentResolver().insert(Contract.activities.CONTENT_URI, add_activity);
		    	finish();
		    }
		});
		    

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_item, menu);
		return true;
	}

}
