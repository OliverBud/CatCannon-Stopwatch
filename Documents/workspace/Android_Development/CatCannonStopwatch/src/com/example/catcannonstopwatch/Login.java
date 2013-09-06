package com.example.catcannonstopwatch;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	
	Button submit_button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	
		submit_button = (Button) findViewById(R.id.submit_button);
		submit_button.setOnClickListener(submit_handler);
		openDB();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private OnClickListener submit_handler = new OnClickListener() {
	    public void onClick(View v) {
	    	EditText text = (EditText) findViewById(R.id.text_box);
	    	String login_text;
	    	login_text = text.getText().toString();
	    	
	    }
	};
	
	private void openDB(){
		Database db = new Database(this);
    	SQLiteDatabase qdb = db.getWritableDatabase();
    
    	qdb.execSQL("INSERT INTO test_table (col_1, col_2) VALUES ('herp derp, 46)");
    	
	}
	
	

}
