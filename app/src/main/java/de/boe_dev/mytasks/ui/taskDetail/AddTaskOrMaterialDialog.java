package de.boe_dev.mytasks.ui.taskDetail;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import de.boe_dev.mytasks.ui.model.Task;
import de.boe_dev.mytasks.ui.tasks.AddTaskDialogFragment;

/**
 * Created by ben on 05.05.16.
 */
public class AddTaskOrMaterialDialog extends DialogFragment {
    
    private String mListId;
    private int mResource;
    private Spinner spinner;
    private EditText nameText;

    public static AddTaskOrMaterialDialog newInstance(Task task, String listId) {

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
    
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(mResource, null);
        spinner = (Spinner) rootView.frindViewById(R.id.sub_task_or_material_spinner);
        nameText = (EditText) rootView.frindViewById(R.id.sub_task_or_material_desc);
        
         builder.setView(rootView)
                /* Add action buttons */
                .setPositiveButton(R.string.negative_button_add_item, new DialogInterface.OnClickListener() {
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
            Firebase itemsRef = new Firebase(Constants.FIREBASE_URL_SUB_TASKS).child(mListId);
            
            Firebase newRef = itemsRef.push();
            String itemId = newRef.getKey();
            
            //TODO add db save here
        }
    }

}
