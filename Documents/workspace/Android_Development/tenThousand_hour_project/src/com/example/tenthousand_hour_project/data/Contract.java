package com.example.tenthousand_hour_project.data;

import android.net.Uri;

public class Contract {
	static final String AUTHORITY = "com.example.tenthousand_hour_project.provider";
	public static final Uri CONTENT_URI =  Uri.parse("content://" + AUTHORITY);
	
	public static final class users{
		public static final Uri CONTENT_URI =  Uri.withAppendedPath(Contract.CONTENT_URI,   "user_table");
		
		private users() {}

	}

	public static final class activities{
		 public static final Uri CONTENT_URI =  Uri.withAppendedPath(Contract.CONTENT_URI,   "activity_table");

		 private activities() {}
		 
	}

}
