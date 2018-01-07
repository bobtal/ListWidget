package com.example.bobantalevski.listwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WidgetProvider extends AppWidgetProvider{

    public static final String KEY_ITEM = "com.example.bobantalevski.listwidget.KEY_ITEM";
    public static final String TOAST_ACTION = "com.example.bobantalevski.listwidget.TOAST_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(TOAST_ACTION)) {
            String listItem = intent.getStringExtra(KEY_ITEM);
            Toast.makeText(context, listItem, Toast.LENGTH_SHORT).show();

            // With this line we are calling onUpdate ourselves whenever there's a toast action
            // so we get a random color change again whenever there's a click on a list item
//            onUpdate(context, AppWidgetManager.getInstance(context), null);
            // We are sending the context, the instance of AppWidgetManager, and since we are not
            // actually using appWidgetIds in onUpdate, we are passing null as the third parameter

            // Or instead of the line above, we could set the action of the passed intent
            // to the update action which will be passed on to super.onReceive and it will
            // trigger the onUpdate method
//            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            // or
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        int[] realAppWidgetIds = AppWidgetManager.getInstance(context).
                getAppWidgetIds(new ComponentName(context, WidgetProvider.class));

        for (int id : realAppWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

            Intent serviceIntent = new Intent(context, WidgetService.class);
            remoteViews.setRemoteAdapter(R.id.listView, serviceIntent);

            // get a random color
            int r = (int)(Math.random() * 0xff);
            int g = (int)(Math.random() * 0xff);
            int b = (int)(Math.random() * 0xff);
            int color = (0xff << 24) + (r << 16) + (g << 8) + b;
            remoteViews.setInt(R.id.frameLayout, "setBackgroundColor", color);

            Intent intent = new Intent(context, WidgetProvider.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Showing a toast by removing the above line and adding the line below
            // but we don't change the color anymore? So manually call onUpdate in onReceive
            intent.setAction(TOAST_ACTION);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, realAppWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
//            remoteViews.setOnClickPendingIntent(R.id.frameLayout, pendingIntent);
            // this sets up a click listener on the frame layout, which we can't click anymore
            // when we added the list to the widget, as we are clicking on list items

            // Instead, in order to keep the "change color on click" functionality,
            // we need to add a click listener to each list item like below
            remoteViews.setPendingIntentTemplate(R.id.listView, pendingIntent);

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }
}
