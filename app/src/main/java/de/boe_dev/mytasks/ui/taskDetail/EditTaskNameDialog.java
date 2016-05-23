package de.boe_dev.mytasks.ui.taskDetail;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import data.TaskContract;
import de.boe_dev.mytasks.R;
import model.Task;
import utils.Constants;
import widget.TaskWidgetProvider;

/**
 * Created by benny on 23.05.16.
 */
public class EditTaskNameDialog extends DialogFragment {

    final static String LOG_TAG = EditTaskNameDialog.class.getSimpleName();
    private EditText taskName,taskAddress;
    private String mTaskId, mTaskName;

    public static EditTaskNameDialog newInstance(Task task, String taskId) {
        EditTaskNameDialog editTaskNameDialog = new EditTaskNameDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_TASK_ID, taskId);
        bundle.putString(Constants.KEY_TASK_NAME, task.getListName());
        editTaskNameDialog.setArguments(bundle);
        return editTaskNameDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskId = getArguments().getString(Constants.KEY_TASK_ID);
        mTaskName = getArguments().getString(Constants.KEY_TASK_NAME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_task, null);
        taskName = (EditText) rootView.findViewById(R.id.add_task_name);
        taskName.setText(mTaskName);
        taskName.setSelection(mTaskName.length());
        taskAddress = (EditText) rootView.findViewById(R.id.add_task_address);

        dialog.setView(rootView).setPositiveButton(R.string.edit_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTask();
                EditTaskNameDialog.this.getDialog().cancel();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditTaskNameDialog.this.getDialog().cancel();
            }
        });


        return dialog.create();
    }

    private void editTask() {
        final String inputTaskName = taskName.getText().toString();
        final String inputAddress = taskAddress.getText().toString();
        if (!inputTaskName.equals("") || !inputAddress.equals("")) {
            if (mTaskId != null && mTaskName != null) {
                Firebase taskRef = new Firebase(Constants.FIREBASE_URL_TASKS).child(mTaskId);
                HashMap<String, Object> updateProperties = new HashMap<>();
                if(!inputTaskName.equals("") && !inputTaskName.equals(mTaskName)) {
                    updateProperties.put(Constants.FIREBASE_PROPERTY_LISTNAME, inputTaskName);
                    ContentValues values = new ContentValues();
                    values.put(TaskContract.TaskEntry.COLUMN_NAME, inputTaskName);
                    getContext().getContentResolver().update(TaskContract.TaskEntry.CONTENT_URI, values, "task_id = ?", new String[] { mTaskId });

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
                    ComponentName thisWidget = new ComponentName(getContext(), TaskWidgetProvider.class);
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                    Intent intent = new Intent(getContext(), TaskWidgetProvider.class);
                    intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                    getContext().sendBroadcast(intent);
                }
                if (!inputAddress.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    List<Address> addresses = null;
                    double latitude = 0.0;
                    double longitude = 0.0;
                    try {
                        addresses = geocoder.getFromLocationName(inputAddress, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses.size() > 0) {
                        latitude = addresses.get(0).getLatitude();
                        longitude = addresses.get(0).getLongitude();
                    }
                    updateProperties.put(Constants.FIREBASE_PROPERTY_LATITUDE, latitude);
                    updateProperties.put(Constants.FIREBASE_PROPERTY_LONGITUDE, longitude);
                }
                taskRef.updateChildren(updateProperties);
            }
        }

    }

}
