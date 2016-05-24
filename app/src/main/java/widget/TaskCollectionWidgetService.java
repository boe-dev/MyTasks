package widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import data.TaskContract;
import de.boe_dev.mytasks.R;
import model.Task;
import utils.Constants;

/**
 * Created by ben on 21.05.16.
 */
public class TaskCollectionWidgetService extends RemoteViewsService {

    int mWidgetId;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsFactory {

        private Context context;
        public ArrayList<Task> taskList;
        private Cursor cursor;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            Log.v("WidgetService", "onCreate()");
            if (cursor != null) {
                cursor.close();
            }
            cursor = getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDataSetChanged() {
            Log.v("WidgetService", "onDataSetChanged()");
            if (cursor != null) {
                cursor.close();
            }
            cursor = getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI, null, null, null, null);
        }

        @Override
        public void onDestroy() {
            taskList = null;
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Log.v("WidgetService", "getViewAt()");

            taskList = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Task task = new Task();
                    task.setListName(cursor.getString(2));
                    taskList.add(task);
                } while (cursor.moveToNext());
            }

            RemoteViews remote = new RemoteViews(context.getPackageName(), R.layout.widget_tasks_item);
            remote.setTextViewText(R.id.widget_item_text, taskList.get(position).getListName());
            return remote;
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
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
