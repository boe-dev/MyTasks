package de.boe_dev.mytasks.ui.tasks;

import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import data.TaskContract;
import de.boe_dev.mytasks.R;
import model.Task;
import utils.Constants;
import widget.TaskWidgetProvider;

/**
 * Created by ben on 05.05.16.
 */
public class AddTaskDialogFragment extends DialogFragment {

    private EditText taskName,taskAddress;
    private String mEncodedEmail;

    public static AddTaskDialogFragment newInstance(String encodedEmail) {
        AddTaskDialogFragment addTaskDialogFragment = new AddTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ENCODED_EMAIL, encodedEmail);
        addTaskDialogFragment.setArguments(bundle);
        return addTaskDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEncodedEmail = getArguments().getString(Constants.KEY_ENCODED_EMAIL);

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
        String createdUser = mEncodedEmail;

        if (!userEnteredTaskName.equals("")) {

            Firebase ref = new Firebase(Constants.FIREBASE_URL_TASKS);
            Firebase newListRef = ref.push();

            HashMap<String, Object> timestampCreated = new HashMap<>();
            timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = null;

            double latitude = 0.0;
            double longitude = 0.0;


            if (!taskAddress.equals("")) {
                try {
                    addresses = geocoder.getFromLocationName(taskAddress.getText().toString(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (addresses.size() > 0) {
                latitude = addresses.get(0).getLatitude();
                longitude = addresses.get(0).getLongitude();
            }

            Task task = new Task(userEnteredTaskName, createdUser, latitude, longitude, timestampCreated);

            newListRef.setValue(task);

            ContentValues values = new ContentValues();
            values.put("task_id", newListRef.getKey());
            values.put("name", userEnteredTaskName);
            getContext().getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, values);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
            ComponentName thisWidget = new ComponentName(getContext(), TaskWidgetProvider.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);

            Intent intent = new Intent(getContext(), TaskWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            getContext().sendBroadcast(intent);

            AddTaskDialogFragment.this.getDialog().cancel();

        }

    }
}
