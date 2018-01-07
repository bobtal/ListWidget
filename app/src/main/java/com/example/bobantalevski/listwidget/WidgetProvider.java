package com.example.bobantalevski.listwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider{

    public static final String KEY_ITEM = "com.example.bobantalevski.listwidget.KEY_ITEM";

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
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
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
