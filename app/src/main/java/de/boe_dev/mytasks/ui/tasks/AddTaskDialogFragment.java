package de.boe_dev.mytasks.ui.tasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.HashMap;

import de.boe_dev.mytasks.R;
import model.Task;
import utils.Constants;

/**
 * Created by ben on 05.05.16.
 */
public class AddTaskDialogFragment extends DialogFragment {

    private EditText taskName,taskAddress;

    public static AddTaskDialogFragment newInstance() {
        AddTaskDialogFragment addTaskDialogFragment = new AddTaskDialogFragment();
        Bundle bundle = new Bundle();
        addTaskDialogFragment.setArguments(bundle);
        return addTaskDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_task, null);
        taskName = (EditText) rootView.findViewById(R.id.add_task_name);
        taskAddress = (EditText) rootView.findViewById(R.id.add_task_address);


        builder.setView(rootView).setPositiveButton(R.string.positiv_button_create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addTask();
            }
        });

        return builder.create();
    }

    public void addTask() {

        String userEnteredTaskName = taskName.getText().toString();
        String createdUser = "Anonymous User";

        if (!userEnteredTaskName.equals("")) {

            Firebase ref = new Firebase(Constants.FIREBASE_URL_TASKS);
            Firebase newListRef = ref.push();

            HashMap<String, Object> timestampCreated = new HashMap<>();
            timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

            Task task = new Task(userEnteredTaskName, createdUser, timestampCreated);

            newListRef.setValue(task);

            AddTaskDialogFragment.this.getDialog().cancel();

        }

    }
}
