package de.boe_dev.mytasks.ui.taskDetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.boe_dev.mytasks.R;
import model.SubTaskOrMaterial;
import model.Task;
import utils.Constants;

/**
 * Created by ben on 19.05.16.
 */
public class TaskDetailFragment extends Fragment implements OnMapReadyCallback {

    ListView mListView;
    FloatingActionsMenu mFabMemu;

    private SupportMapFragment mapFragment;
    private UiSettings uiSettings;
    private FragmentManager fm;
    private TaskDetailItemAdapter mTaskDetailItemAdapter;
    private ValueEventListener mTaskEventListener;
    private Firebase mRef;
    private Task mTask;
    private String mTaskId;
    private double latitude, longitude = 0.0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Constants.KEY_TASK_ID)) {
            mTaskId = getArguments().getString(Constants.KEY_TASK_ID);
        }
        fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_detail_task, container, false);
        initViews(rootView);

        mRef = new Firebase(Constants.FIREBASE_URL_TASKS).child(mTaskId);
        mTaskEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mTask = dataSnapshot.getValue(Task.class);
                if (mTask == null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    return;
                }
                latitude = mTask.getLatitude();
                longitude = mTask.getLongitude();
                if (latitude != 0.0 && longitude != 0.0) {
                    mapFragment.getMapAsync(TaskDetailFragment.this);
                } else {
                    mapFragment.getView().setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase listItemsRef = new Firebase(Constants.FIREBASE_URL_SUBTASKS).child(mTaskId);
        mTaskDetailItemAdapter = new TaskDetailItemAdapter(getActivity(), SubTaskOrMaterial.class, R.layout.item_materials, listItemsRef, mTaskId);
        mListView.setAdapter(mTaskDetailItemAdapter);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTaskDetailItemAdapter.cleanup();
        mRef.removeEventListener(mTaskEventListener);
    }

    public void showAddTaskOrMaterialDialog() {
        DialogFragment dialog = AddTaskOrMaterialDialog.newInstance(mTaskId);
        dialog.show(this.getFragmentManager(), getContext().getResources().getString(R.string.add_task_or_material_dialog));
    }

    private void showRemoveTaskDialog() {
        DialogFragment dialogFragment = RemoveTaskDialogFragment.newInstance(mTaskId);
        dialogFragment.show(getFragmentManager(), "RemoveTaskDialogFragment");
    }

    private void showEditTaskDialog() {
        DialogFragment dialogFragment = EditTaskNameDialog.newInstance(mTask, mTaskId);
        dialogFragment.show(this.getFragmentManager(), "EditTaskNameDialog");
    }

    private void showEditListItemNameDialog(String itemName, String itemId) {
        DialogFragment dialogFragment = EditTaskItemNameDialog.newInstance(itemName, itemId, mTaskId);
        dialogFragment.show(this.getFragmentManager(), "EditTaskItemNameDialog");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        uiSettings = googleMap.getUiSettings();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        uiSettings.setZoomControlsEnabled(true);
    }

    private void initViews(View rootView) {

        mFabMemu = (FloatingActionsMenu) rootView.findViewById(R.id.fab_menu);
        mListView = (ListView) rootView.findViewById(R.id.task_detail_list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubTaskOrMaterial subTaskOrMaterial = mTaskDetailItemAdapter.getItem(position);
                if (subTaskOrMaterial != null && subTaskOrMaterial.getType() == 0) {
                    String itemName = subTaskOrMaterial.getName();
                    String itemId = mTaskDetailItemAdapter.getRef(position).getKey();
                    showEditListItemNameDialog(itemName, itemId);
                } else {
                    return;
                }
            }
        });
        rootView.findViewById(R.id.fab_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskOrMaterialDialog();
                if(mFabMemu.isExpanded()) {
                    mFabMemu.collapse();
                }
            }
        });
        rootView.findViewById(R.id.fab_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditTaskDialog();
                if(mFabMemu.isExpanded()) {
                    mFabMemu.collapse();
                }
            }
        });
        rootView.findViewById(R.id.fab_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveTaskDialog();
                if(mFabMemu.isExpanded()) {
                    mFabMemu.collapse();
                }
            }
        });
    }
}
