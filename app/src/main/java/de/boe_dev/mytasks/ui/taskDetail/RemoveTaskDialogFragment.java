package de.boe_dev.mytasks.ui.taskDetail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;

import data.TaskContract;
import de.boe_dev.mytasks.R;
import utils.Constants;

/**
 * Created by benny on 23.05.16.
 */
public class RemoveTaskDialogFragment extends DialogFragment {

    final static String LOG_TAG = RemoveTaskDialogFragment.class.getSimpleName();
    private String mTaskId;

    public static RemoveTaskDialogFragment newInstance(String taskId) {
        RemoveTaskDialogFragment removeTaskDialogFragment = new RemoveTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_LIST_ID, taskId);
        removeTaskDialogFragment.setArguments(bundle);
        return removeTaskDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskId = getArguments().getString(Constants.KEY_LIST_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog)
                .setTitle(getActivity().getResources().getString(R.string.action_remove_task))
                .setMessage(getString(R.string.dialog_message_are_you_sure_remove_task))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeTask();
                /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    private void removeTask() {

        HashMap<String, Object> removeListData = new HashMap<String, Object>();

        removeListData.put("/" + Constants.FIREBASE_LOCATION_TASKS + "/" + mTaskId, null);
        removeListData.put("/" + Constants.FIREBASE_LOCATION_SUBTASKS + "/" + mTaskId, null);

        Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);
        firebaseRef.updateChildren(removeListData, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.e(LOG_TAG, firebaseError.getMessage());
                }
            }
        });

        getContext().getContentResolver().delete(TaskContract.TaskEntry.CONTENT_URI, "task_id = ?", new String[] { mTaskId });
        getContext().getContentResolver().delete(TaskContract.MaterialEntry.CONTENT_URI, "task_id = ?", new String[] { mTaskId });



    }

}
