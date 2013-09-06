package com.example.hello_world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class ListViewExampleActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_listviewexampleactivity);
    SQLiteDatabase qdb = openDB();

    final ListView listview = (ListView) findViewById(R.id.listview);
    
    
    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
        "Android", "iPhone", "WindowsMobile" };

    final ArrayList<String> list = new ArrayList<String>();
    ContentValues values1 = new ContentValues();
    for (int i = 0; i < values.length; ++i) {
    	values1.put("name", values[i]);
    	values1.put("num", i);
    }	
    	long newRowId;
    	newRowId = qdb.insert(
    	         "test_table",
    	         "name",
    	         values1);
    
    
    for (int i = 0; i < values.length; ++i) {
    	String[] stuff = new String[1];
    	stuff[0] = "name";
    	Cursor recordSet = qdb.query(false, "test_table", stuff, "num", stuff, "1", null, null, null, null);
    	String add = recordSet.getString(recordSet.getPosition());
    	list.add(add);
    }
    
    
    final StableArrayAdapter adapter = new StableArrayAdapter(this,
        android.R.layout.simple_list_item_1, list);
    listview.setAdapter(adapter);

    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, final View view,
          int position, long id) {
//        final String item = (String) parent.getItemAtPosition(position);
//        view.animate().setDuration(2000).alpha(0)
//            .withEndAction(new Runnable() {
//              @Override
//              public void run() {
//                list.remove(item);
//                adapter.notifyDataSetChanged();
//                view.setAlpha(1);
//              }
//            });
      }

	

    });
  }

  private class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
    }

    @Override
    public long getItemId(int position) {
      String item = getItem(position);
      return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

  }

  private SQLiteDatabase openDB(){
		Database db = new Database(this);
  	SQLiteDatabase qdb = db.getWritableDatabase();
  	return qdb;
  	
	}
  
} 