/*
 * wrapper class for user activities to help facilitate 
 * the adapter populating the listView
 */

package com.example.tenthousand_hour_project;

public class activity_summary {

	public String name;
	public String time;
	
	public activity_summary(){
		super();
	}
	
	//pertinant info to the list
	public activity_summary(String name, String time){
		super();
		this.name = name;
		this.time = time;
	}
}
