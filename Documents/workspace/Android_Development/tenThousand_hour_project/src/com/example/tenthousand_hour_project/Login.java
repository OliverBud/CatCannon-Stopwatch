/*
 * 10,000 Hour Stopwatch
 * 
 * This activity is loaded upon application creation. validates user name/password and loads
 * the appropriate profile in the database
 * 
 */

package com.example.tenthousand_hour_project;

import android.os.Bundle;   
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

 
public class Login extends FragmentActivity implements LoginDialogFragment.NoticeDialogListener, LoginPasswordDialogFragment.NoticeDialogListener{

	Button SubmitButton;
	SQLiteDatabase qdb;
	int mStackLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		//Set Handler to listen for the user info submit button
		SubmitButton = (Button) findViewById(R.id.login_button);
		SubmitButton.setOnClickListener(SubmitListener);
		
		//Initialize database
		Database db = new Database(this);
		qdb = db.getWritableDatabase();		
		
		//set all profiles to inactive. will set the one that logs in to active
		ContentValues resetActive = new ContentValues();
		resetActive.put("active", 0);
		qdb.update("user_table", resetActive, null, null);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	//Handler Definion
	private OnClickListener SubmitListener = new OnClickListener() {
	    public void onClick(View v) {
	    	EditText name_text = (EditText) findViewById(R.id.name_box);
			EditText password_text = (EditText) findViewById(R.id.password_box);
			String name = name_text.getText().toString();
			String password = password_text.getText().toString();
		

			//Validate Information
			Cursor nameQuery = qdb.rawQuery("SELECT password FROM user_table WHERE userName = '" + name + "'", null);
			if (nameQuery.getCount() < 1){
				
				//if user name invalid, respond with this Dialogue
				showNameNoticeDialog();
				
			}
			else{
				
				nameQuery.moveToFirst();
				String realPass = nameQuery.getString(nameQuery.getColumnIndexOrThrow("password"));
				if (realPass.equals(password)){
				
					//if password is valid, set profile activee
					ContentValues userContent = new ContentValues();
					userContent.put("active", 1);
					String where = "userName=?";
					String[] whereArgs = new String[] {name};
					qdb.update("user_table", userContent, where, whereArgs);
					
					//move to activity displaying profile information
					switchActivity();
				}
				else{
					
					//if password is invalid, show this dialogue
					showPasswordNoticeDialog();
				}
			
			}
			
	    }
	};

	public void switchActivity(){
		
		//will always switch to List activity from Login
		Intent intent = new Intent(this, List1.class);
		startActivity(intent);	    
	}
	
	//Helper function to get a new user id when creating new profile
	public int findNewId(SQLiteDatabase qdb){
		Cursor idQuery = qdb.rawQuery("SELECT user_id FROM user_table", null);
		idQuery.moveToLast();
		int lastIdInt = idQuery.getInt(idQuery.getColumnIndex("user_id"));
		return lastIdInt + 1;
	}
	
	//for debugging purposes
	public void displayDB(SQLiteDatabase qdb){
		Cursor recordSet = qdb.rawQuery("Select * FROM user_table", null);
		int numRows = recordSet.getCount();
		recordSet.moveToFirst();
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
		
		//display_text.setText(Text);
	}
	
	
	//Dialogue for incorrect user name
	@SuppressLint("NewApi")
	public void showNameNoticeDialog() {
		
		
		mStackLevel ++;
		android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
	    android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Create and show the dialog.
	    DialogFragment newFragment = LoginDialogFragment.newInstance();
	    newFragment.show(ft, "dialog");
		
	}
	
	//dialog for password invalid
	public void showPasswordNoticeDialog() {
		
		
		mStackLevel ++;
		android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
	    android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Create and show the dialog.
	    DialogFragment newFragment = LoginPasswordDialogFragment.newInstance();
	    newFragment.show(ft, "dialog");
		
	}

	
	//function to implementing interface for user name invalid dialog
	//called when button for user creation clicked
	
    // "The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface"
    @Override
    public void onDialogNamePositiveClick(DialogFragment dialog) {
    	EditText name_text = (EditText) findViewById(R.id.name_box);
		EditText password_text = (EditText) findViewById(R.id.password_box);
		String name = name_text.getText().toString();
		String password = password_text.getText().toString();
    	
    	ContentValues newUser = new ContentValues();
		newUser.put("userName", name);
		newUser.put("password", password);
		newUser.put("active", 1);
		int newId = findNewId(qdb);
		newUser.put("user_id", newId);
		qdb.insert("user_table", null, newUser);   
		switchActivity();
    }

 	 //function to implementing interface for user name invalid dialog
  	//called when button for cancel/reset clicked
    @Override
    public void onDialogNameNegativeClick(DialogFragment dialog) {
    	EditText name_text = (EditText) findViewById(R.id.name_box);
		EditText password_text = (EditText) findViewById(R.id.password_box);
		name_text.setText("");
		password_text.setText("");
		
    }
    
    //function to implementing interface for password invalid dialog
  	//called when button for aknowledgment clicked
    @Override
    public void onDialogPasswordOkClick(DialogFragment dialog) {
    	EditText name_text = (EditText) findViewById(R.id.name_box);
		EditText password_text = (EditText) findViewById(R.id.password_box);
		name_text.setText("");
		password_text.setText("");
    }

	

}
