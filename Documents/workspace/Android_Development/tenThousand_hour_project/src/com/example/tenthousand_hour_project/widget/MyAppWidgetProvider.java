package com.example.tenthousand_hour_project.widget;


import com.example.tenthousand_hour_project.R;
import com.example.tenthousand_hour_project.R.id;
import com.example.tenthousand_hour_project.R.layout;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;


public class MyAppWidgetProvider extends AppWidgetProvider{
    public static final String EXTRA_ITEM = "com.example.widget_tester.EXTRA_ITEM";
    public static final String TOAST_ACTION = "com.example.widget_tester.TOAST_ACTION";
    public static final String CUSTOM_ACTION = "com.example.widget_tester.CUSTOM_ACTION";
    

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
        	
        	Intent intent1 = new Intent(context, MyAppWidgetProvider.class);
            intent1.setAction(CUSTOM_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent1, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button

            // Here we setup the intent which points to the StackViewService which will
            // provide the views for this collection.
            Intent intent = new Intent(context, StackWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
           
            
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.app_widget_provider_layout);
            rv.setRemoteAdapter(appWidgetIds[i], R.id.stack_view, intent);
            rv.setOnClickPendingIntent(R.id.button1, pendingIntent);


            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            rv.setEmptyView(R.id.stack_view, R.id.empty_view);

            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.
            Intent toastIntent = new Intent(context, MyAppWidgetProvider.class);
            toastIntent.setAction(MyAppWidgetProvider.TOAST_ACTION);
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		 
		 if (CUSTOM_ACTION.equals(intent.getAction())){
			String msg = "mmmmm..... buttered toast";
			 Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			 
		 }
		if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
	}
	
	
	

}
