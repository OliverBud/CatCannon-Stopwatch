package com.example.hello_world;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity{
	Button b1;
	Button b2;
	double time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		b1 = (Button) findViewById(R.id.Button_1);
		b1.setOnClickListener(myListener1);
		b2 = (Button) findViewById(R.id.Button_2);
		b2.setOnClickListener(myListener2);
		time = 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}

	
	private OnClickListener myListener1 = new OnClickListener() {
	    public void onClick(View v) {
	    	time = System.currentTimeMillis();
	    }
	};
	
	private OnClickListener myListener2 = new OnClickListener() {
	    public void onClick(View v) {
	    	time = System.currentTimeMillis() - time;
	    	EditText text = (EditText) findViewById(R.id.Hi);
	    	
	    	String time_string = String.valueOf(time);
	    	text.setText(time_string);
	    	switchActivity();
	    }
	};
	
	public void switchActivity(){
		Intent intent = new Intent(this, ListViewExampleActivity.class);
		startActivity(intent);	    
	}
	

}
