package de.boe_dev.mytasks.ui.taskDetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.util.HashMap;

import de.boe_dev.mytasks.R;

import utils.Constants;

/**
 * Created by benny on 24.05.16.
 */
public class EditTaskItemNameDialog extends DialogFragment {

    private EditText subTaskName;
    private String mTaskId, mItemName, mItemId;

    public static EditTaskItemNameDialog newInstance(String itemName, String itemId, String taskId) {
        EditTaskItemNameDialog editItemNameDialog = new EditTaskItemNameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_TASK_ID, taskId);
        bundle.putString(Constants.KEY_TASK_ITEM_NAME, itemName);
        bundle.putString(Constants.KEY_TASK_ITEM_ID, itemId);
        editItemNameDialog.setArguments(bundle);
        return editItemNameDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskId = getArguments().getString(Constants.KEY_TASK_ID);
        mItemName = getArguments().getString(Constants.KEY_TASK_ITEM_NAME);
        mItemId = getArguments().getString(Constants.KEY_TASK_ITEM_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_sub_task_or_material, null);
        rootView.findViewById(R.id.sub_task_or_material_spinner).setVisibility(View.GONE);
        subTaskName = (EditText) rootView.findViewById(R.id.sub_task_or_material_desc);
        subTaskName.setText(mItemName);
        subTaskName.setSelection(mItemName.length());

        dialog.setView(rootView).setPositiveButton(R.string.edit_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editSubTask();
                EditTaskItemNameDialog.this.getDialog().cancel();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditTaskItemNameDialog.this.getDialog().cancel();
            }
        });
        return dialog.create();
    }

    private void editSubTask() {
        String nameInput = subTaskName.getText().toString();
        if (!nameInput.equals("") && !nameInput.equals(subTaskName)) {
            Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);
            HashMap<String, Object> updateItem = new HashMap<>();
            updateItem.put("/" + Constants.FIREBASE_LOCATION_SUBTASKS + "/" + mTaskId + "/" + mItemId + "/" + Constants.FIREBASE_PROPERTY_NAME, nameInput);
            firebaseRef.updateChildren(updateItem);
        }
    }

}
