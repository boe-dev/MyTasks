package de.boe_dev.mytasks.ui.taskDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import de.boe_dev.mytasks.R;
import utils.Constants;

/**
 * Created by ben on 05.05.16.
 */
public class TaskDetailActivity extends AppCompatActivity {

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
    }

    public void showAddTaskOrMaterialDialog(View view) {
        DialogFragment dialog = AddTaskOrMaterialDialog.newInstance(mTaskId);
        dialog.show(this.getSupportFragmentManager(), "AddTaskOrMaterialDialog");
    }

}
