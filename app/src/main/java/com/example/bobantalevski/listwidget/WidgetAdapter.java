package com.example.bobantalevski.listwidget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    String[] list = {"Treehouse", "Android", "Java", "Kotlin", "Anko"};

    public WidgetAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_item);
        remoteViews.setTextViewText(R.id.textView, list[i]);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
