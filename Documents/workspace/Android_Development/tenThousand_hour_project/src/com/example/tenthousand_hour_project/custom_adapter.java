/*
 * custom adapter for the ListView
 */

package com.example.tenthousand_hour_project;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class custom_adapter extends ArrayAdapter<activity_summary>{

	Context context;
	int layoutResourceId;
	
	//here is the list of the data we will use to fill the list
	//stored as activity_summaries
	ArrayList<activity_summary> data;
	
	public custom_adapter(Context context, int layoutResourseID, ArrayList<activity_summary> data){
		super(context, layoutResourseID, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		custom_holder holder = null;
		
		if(row == null)
        {
			
			//call to custom row layout we are using
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.row_layout,null);
            
            holder = new custom_holder();
            holder.name_text = (TextView)row.findViewById(R.id.name_view);
            holder.time_text = (TextView)row.findViewById(R.id.time_view);
            
            row.setTag(holder);
        }
        else
        {
            holder = (custom_holder)row.getTag();
        }
        
         activity_summary summary = data.get(position);
        holder.name_text.setText(summary.name);
        holder.time_text.setText(summary.time);
		
		return row;
		
	}

	//holder class for the textViews we want to put info into
	static class custom_holder
	{
		TextView name_text;
		TextView time_text;
	}
	
}
