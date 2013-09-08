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
	SQLiteDatabase qdb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SubmitButton = (Button) findViewById(R.id.login_button);
		SubmitButton.setOnClickListener(SubmitListener);
		Database db = new Database(this);
		qdb = db.getWritableDatabase();		
		ContentValues resetActive = new ContentValues();
		resetActive.put("active", 0);
		qdb.update("user_table", resetActive, null, null);
		displayDB(qdb);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	private OnClickListener SubmitListener = new OnClickListener() {
	    public void onClick(View v) {
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
				qdb.insert("user_table", null, newUser);
				displayDB(qdb);
				//switchActivity();
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
	
//	private SQLiteDatabase openDB(SQLiteDatabase qdb){
//		Database db = new Database(this);
//		qdb = db.getWritableDatabase();
//		displayDB(qdb);
//		return qdb;
//  	
//	}
	
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
	
	public void displayDB(SQLiteDatabase qdb){
		Cursor recordSet = qdb.rawQuery("Select * FROM user_table", null);
		int numRows = recordSet.getCount();
		recordSet.moveToFirst();
		EditText display_text = (EditText) findViewById(R.id.DB_display);
		display_text.setLines(numRows + 1);
		String dName;
		String dPassword;
		int duser_id;
		int dactive;
		String Text;
		Text = Integer.toString(numRows) + "\n";
		for (int i = 0; i < numRows; i ++){
			dName = recordSet.getString(recordSet.getColumnIndexOrThrow("userName"));
			dPassword = recordSet.getString(recordSet.getColumnIndexOrThrow("password"));
			duser_id = recordSet.getInt(recordSet.getColumnIndexOrThrow("user_id"));
			dactive = recordSet.getInt(recordSet.getColumnIndexOrThrow("active"));
			Text = Text + dName + " " + dPassword + " " + Integer.toString(duser_id) + " " + Integer.toString(dactive) + "\n";	
			recordSet.moveToNext();
		}
		
		display_text.setText(Text);
	}
	

}
