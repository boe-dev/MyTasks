package widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.MainActivity;

/**
 * Created by ben on 21.05.16.
 */
public class TaskWidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_ITEM = "de.boe_dev.taskwidget.EXTRA_ITEM";
    public static final String CLICK_ACTION = "de.boe_dev.taskwidget.CLICK_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(CLICK_ACTION)) {
            String taskId = intent.getStringExtra(EXTRA_ITEM);
            Intent taskDetailIntent = new Intent(context, MainActivity.class);
            taskDetailIntent.putExtra(MainActivity.TASK_DETAIL, taskId);
            taskDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(taskDetailIntent);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, TaskCollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_tasks);
            rv.setRemoteAdapter(R.id.widget_list, intent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);

        }
    }
}
