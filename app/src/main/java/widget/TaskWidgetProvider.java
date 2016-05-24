package widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.MainActivity;

/**
 * Created by ben on 21.05.16.
 */
public class TaskWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "de.boe_dev.taskwidget.EXTRA_ITEM";
    public static final String CLICK_ACTION = "de.boe_dev.taskwidget.CLICK_ACTION";
    public static final String REFRESH = "de.boe_dev.taskwidget.REFRESH";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(CLICK_ACTION)) {
            String taskId = intent.getStringExtra(EXTRA_ITEM);
            Intent taskDetailIntent = new Intent(context, MainActivity.class);
            taskDetailIntent.putExtra(MainActivity.TASK_DETAIL, taskId);
            taskDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(taskDetailIntent);
        } else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, TaskWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(componentName), R.id.widget_list);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.v("WidgetProvider", "onUpdate");

        for (int appWidgetId : appWidgetIds) {
            Log.v("WidgetProvider", "appWidgetId " + appWidgetId);
            Intent intent = new Intent(context, TaskCollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_tasks);
//            Intent intentSync = new Intent(context, FetchService.class);
//            intentSync.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intentSync.putExtra(REFRESH, 0);
//            PendingIntent pendingSync = PendingIntent.getService(context, 0, intentSync, 0);

            rv.setRemoteAdapter(R.id.widget_list, intent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }
}
