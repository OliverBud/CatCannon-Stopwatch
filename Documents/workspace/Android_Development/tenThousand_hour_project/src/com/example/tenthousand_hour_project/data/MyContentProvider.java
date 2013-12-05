package com.example.tenthousand_hour_project.data;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class MyContentProvider extends ContentProvider{

	private Database helper;
	private static final int USER_TABLE = 10;
	private static final int USER_TABLE_ID = 20;

	private static final int ACTIVITY_TABLE = 15;
	private static final int ACTIVITY_TABLE_ID = 25;
	
	//private static final String AUTHORITY = "com.example.tenthousand_hour_project.provider";
	//private static final String USER_PATH = "user_table";
	//private static final String ACTIVITY_PATH = "activity_table";
	//public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

	
/*	
	 public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
		      + "/user_table";
	 public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
		      + "/user_table";
*/	 
	 private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
	    sURIMatcher.addURI(Contract.AUTHORITY, users.TABLE_USERS, USER_TABLE);
	    sURIMatcher.addURI(Contract.AUTHORITY, users.TABLE_USERS + "/#", USER_TABLE_ID);
	    
	    sURIMatcher.addURI(Contract.AUTHORITY, activities.TABLE_ACTIVITIES, ACTIVITY_TABLE);
	    sURIMatcher.addURI(Contract.AUTHORITY, activities.TABLE_ACTIVITIES + "/#", ACTIVITY_TABLE_ID);
	  }
	
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowsDeleted = 0;
		switch(uriType){
			case USER_TABLE:
				db.delete(users.TABLE_USERS, where, whereArgs);
				break;
			case USER_TABLE_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(where)) {
			        rowsDeleted = db.delete(users.TABLE_USERS,
			        		users.COLUMN_ID + "=" + id, 
			            null);
			      } else {
			        rowsDeleted = db.delete(users.TABLE_USERS,
			        		users.COLUMN_ID + "=" + id 
			            + " and " + where,
			            whereArgs);
			      }
				break;
			case ACTIVITY_TABLE:
				db.delete(activities.TABLE_ACTIVITIES, where, whereArgs);
				break;
			case ACTIVITY_TABLE_ID:
				String id1 = uri.getLastPathSegment();
				if (TextUtils.isEmpty(where)) {
			        rowsDeleted = db.delete(activities.TABLE_ACTIVITIES,
			        	activities.COLUMN_ID + "=" + id1, 
			            null);
			      } else {
			        rowsDeleted = db.delete(activities.TABLE_ACTIVITIES,
			        	activities.COLUMN_ID + "=" + id1 
			            + " and " + where,
			            whereArgs);
			      }
				break;
			default:
			      throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = helper.getWritableDatabase();
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = helper.getWritableDatabase();
		long id = 0;
		Uri PATH;
		switch(uriType){
			case USER_TABLE:
				id = db.insert(users.TABLE_USERS, null, values);
				PATH = Contract.users.CONTENT_URI;
				break;
			case ACTIVITY_TABLE:
				id = db.insert(activities.TABLE_ACTIVITIES, null, values);
				PATH = Contract.activities.CONTENT_URI;
				break;
			default:
			      throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return ContentUris.withAppendedId(PATH, id);
	}

	@Override
	public boolean onCreate() {
		helper = new Database(getContext());
		Log.d("cp", "content Provider created");
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String where,
		      String[] whereArgs, String sortOrder) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = helper.getWritableDatabase();
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		
		switch(uriType){
		case USER_TABLE:
			qb.setTables(users.TABLE_USERS);
			break;
		case USER_TABLE_ID:
			qb.setTables(users.TABLE_USERS);
			String id = uri.getLastPathSegment();
			qb.appendWhere(users.COLUMN_ID + "=" + id);
			break;
		case ACTIVITY_TABLE:
			qb.setTables(activities.TABLE_ACTIVITIES);
			break;
		case ACTIVITY_TABLE_ID:
			qb.setTables(activities.TABLE_ACTIVITIES);
			String id1 = uri.getLastPathSegment();
			qb.appendWhere(activities.COLUMN_ID + "=" + id1);
			break;
		default:
		      throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		Cursor cursor = qb.query(db, projection, where,
		        whereArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		Log.d("cp", "updating");
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = helper.getWritableDatabase();
		int rowsUpdated = 0;
		switch(uriType){
			case USER_TABLE:
				db.update(users.TABLE_USERS, values, where, whereArgs);
				break;
			case USER_TABLE_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(where)){
					rowsUpdated = db.update(users.TABLE_USERS,
							values, 
							users.COLUMN_ID + "=" + id, 
							null);
					
				} else {
					rowsUpdated = db.update(users.TABLE_USERS, 
							values, 
							users.COLUMN_ID + "=" + id + "and" + where, 
							whereArgs);
					
				}
				break;
			case ACTIVITY_TABLE:
				db.update(activities.TABLE_ACTIVITIES, values, where, whereArgs);
				break;
			case ACTIVITY_TABLE_ID:
				String id1 = uri.getLastPathSegment();
				if (TextUtils.isEmpty(where)){
					rowsUpdated = db.update(activities.TABLE_ACTIVITIES,
							values, 
							activities.COLUMN_ID + "=" + id1, 
							null);
					
				} else {
					rowsUpdated = db.update(activities.TABLE_ACTIVITIES, 
							values, 
							activities.COLUMN_ID + "=" + id1 + "and" + where, 
							whereArgs);
					
				}
				break;
			default:
				Log.d("cp", "default");
			      throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
