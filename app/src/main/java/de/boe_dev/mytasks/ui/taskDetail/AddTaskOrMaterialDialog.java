package de.boe_dev.mytasks.ui.taskDetail;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import de.boe_dev.mytasks.ui.model.Task;
import de.boe_dev.mytasks.ui.tasks.AddTaskDialogFragment;

/**
 * Created by ben on 05.05.16.
 */
public class AddTaskOrMaterialDialog extends DialogFragment {

    public static AddTaskOrMaterialDialog newInstance(Task task, String listId) {

        AddTaskOrMaterialDialog addTaskOrMaterialDialog = new AddTaskOrMaterialDialog();
        Bundle bundle = new Bundle();
        addTaskOrMaterialDialog.setArguments(bundle);
        return addTaskOrMaterialDialog;
    }

}
