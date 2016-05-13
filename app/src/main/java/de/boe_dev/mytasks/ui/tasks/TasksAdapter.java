package de.boe_dev.mytasks.ui.tasks;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.boe_dev.mytasks.R;
import model.Task;

/**
 * Created by ben on 05.05.16.
 */
public class TasksAdapter extends FirebaseListAdapter<Task> {

    @BindView(R.id.item_task_list_name) TextView taskNameText;
    @BindView(R.id.item_task_created_by_user) TextView createdUserText;

    public TasksAdapter(Activity activity, Class<Task> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        ButterKnife.bind(activity);
    }

    @Override
    protected void populateView(View v, Task list) {
        super.populateView(v, list);

        taskNameText = (TextView) v.findViewById(R.id.item_task_list_name);
        createdUserText = (TextView) v.findViewById(R.id.item_task_created_by_user);

        taskNameText.setText(list.getListName());
        createdUserText.setText(list.getCreatedUser());
    }
}
