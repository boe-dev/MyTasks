package de.boe_dev.mytasks.ui.taskDetail;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

import data.TaskContract;
import data.TaskDbHelper;
import data.TaskProvider;
import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.materials.MaterialsFragment;
import model.SubTaskOrMaterial;
import utils.Constants;

/**
 * Created by ben on 05.05.16.
 */
public class AddTaskOrMaterialDialog extends DialogFragment {
    
    private String mListId;
    private int mResource;
    private Spinner spinner;
    private EditText nameText;

    public static AddTaskOrMaterialDialog newInstance(String listId) {

        AddTaskOrMaterialDialog addTaskOrMaterialDialog = new AddTaskOrMaterialDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_LIST_ID, listId);
        bundle.putInt(Constants.KEY_LAYOUT_RESOURCE, R.layout.dialog_add_sub_task_or_material);
        addTaskOrMaterialDialog.setArguments(bundle);
        return addTaskOrMaterialDialog;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListId = getArguments().getString(Constants.KEY_LIST_ID);
        mResource = getArguments().getInt(Constants.KEY_LAYOUT_RESOURCE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(mResource, null);
        spinner = (Spinner) rootView.findViewById(R.id.sub_task_or_material_spinner);
        nameText = (EditText) rootView.findViewById(R.id.sub_task_or_material_desc);

        builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.positiv_button_add_item, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addSubTaskOrMaterial();
                        AddTaskOrMaterialDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton(R.string.negative_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        AddTaskOrMaterialDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void addSubTaskOrMaterial() {
        if(!nameText.equals("")) {

            Firebase firebaseRef = new Firebase(Constants.FIREBASE_URL);
            Firebase itemsRef = new Firebase(Constants.FIREBASE_URL_SUBTASKS).child(mListId);

            HashMap<String, Object> updatedIteimToAddMap = new HashMap<>();

            Firebase newRef = itemsRef.push();
            String itemId = newRef.getKey();

            SubTaskOrMaterial subTaskOrMaterial = new SubTaskOrMaterial(nameText.getText().toString(), spinner.getSelectedItemPosition(), false);
            HashMap<String, Object> itemToAdd =
                    (HashMap<String, Object>) new ObjectMapper().convertValue(subTaskOrMaterial, Map.class);

            updatedIteimToAddMap.put("/" + Constants.FIREBASE_LOCATION_SUBTASKS + "/" +
                    mListId + "/" + itemId, itemToAdd);

            firebaseRef.updateChildren(updatedIteimToAddMap);

            if (spinner.getSelectedItemPosition() == 1) {
                ContentValues values = new ContentValues();
                values.put("task_id", mListId);
                values.put("name", nameText.getText().toString());
                values.put("checked", 0);
                getContext().getContentResolver().insert(TaskContract.MaterialEntry.CONTENT_URI, values);
            }

            AddTaskOrMaterialDialog.this.getDialog().cancel();
        }
    }

}
