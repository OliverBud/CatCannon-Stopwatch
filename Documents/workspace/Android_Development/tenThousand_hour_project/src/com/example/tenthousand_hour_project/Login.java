package com.example.tenthousand_hour_project;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {

	Button SubmitButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SubmitButton = (Button) findViewById(R.id.login_button);
		SubmitButton.setOnClickListener(SubmitListener);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private OnClickListener SubmitListener = new OnClickListener() {
	    public void onClick(View v) {
	    	SQLiteDatabase qdb = openDB();
	    	EditText name_text = (EditText) findViewById(R.id.name_box);
			EditText password_text = (EditText) findViewById(R.id.password_box);
			String name = name_text.getText().toString();
			String password = password_text.getText().toString();

			Cursor nameQuery = qdb.rawQuery("SELECT password FROM user_table WHERE userName = '" + name + "'", null);
			if (nameQuery.getCount() < 1){
				ContentValues newUser = new ContentValues();
				newUser.put("userName", name);
				newUser.put("password", password);
				newUser.put("active", 1);
				int newId = findNewId(qdb);
				newUser.put("user_id", newId);
				long newRowId;
				newRowId = qdb.insert("user_table", null, newUser);
				switchActivity();
			}
			else{
				if (nameQuery.toString().equals(password)){
					ContentValues userContent = new ContentValues();
					userContent.put("active", 1);
					long rowId;
					rowId = qdb.update("user_table", userContent, "user_name=" + name, null);
					switchActivity();
				}
				else{
					switchActivity();
				}
			
			}
			
	    }
	};
	
	private SQLiteDatabase openDB(){
		Database db = new Database(this);
		SQLiteDatabase qdb = db.getWritableDatabase();
		return qdb;
  	
	}
	
	public void switchActivity(){
		Intent intent = new Intent(this, List.class);
		startActivity(intent);	    
	}
	
	public int findNewId(SQLiteDatabase qdb){
		Cursor idQuery = qdb.rawQuery("SELECT user_id FROM user_table", null);
		idQuery.moveToLast();
		int lastIdInt = idQuery.getInt(idQuery.getColumnIndex("user_id"));
		return lastIdInt + 1;
	}
	

}
