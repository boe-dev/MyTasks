package widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

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

        public ListRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            taskList = new ArrayList<>();


            for (int i = 0; i < 5; i++) {
                Task task = new Task();
                task.setListName("name " + i);
                taskList.add(task);
            }

            Firebase firebase = new Firebase(Constants.FIREBASE_URL_TASKS);
            firebase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Task user = userSnapshot.getValue(Task.class);
                        Log.v("test", "" + user.getListName());
                        if (user != null) {
                            taskList.add(user);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            taskList = null;
        }

        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
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
