package de.boe_dev.mytasks.ui.taskDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.firebase.client.Firebase;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.boe_dev.mytasks.R;
import model.SubTaskOrMaterial;
import utils.Constants;

/**
 * Created by ben on 05.05.16.
 */
public class TaskDetailActivity extends AppCompatActivity {

    private ListView mListView;
    private TaskDetailItemAdapter mTaskDetailItemAdapter;
    private Firebase mRef;
    private String mTaskId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        Intent intent = this.getIntent();
        mTaskId = intent.getStringExtra(Constants.KEY_LIST_ID);
        if (mTaskId == null) {
            finish();
            return;
        }

        mRef = new Firebase(Constants.FIREBASE_URL_TASKS).child(mTaskId);
        Firebase listItemsRfef = new Firebase(Constants.FIREBASE_URL_SUBTASKS).child(mTaskId);

        mTaskDetailItemAdapter = new TaskDetailItemAdapter(this, SubTaskOrMaterial.class, R.layout.item_materials, listItemsRfef, mTaskId);
        mListView = (ListView) findViewById(R.id.task_detail_list);
        mListView.setAdapter(mTaskDetailItemAdapter);

    }

    public void showAddTaskOrMaterialDialog(View view) {
        DialogFragment dialog = AddTaskOrMaterialDialog.newInstance(mTaskId);
        dialog.show(this.getSupportFragmentManager(), "AddTaskOrMaterialDialog");
    }

}
