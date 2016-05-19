package de.boe_dev.mytasks.ui.taskDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.boe_dev.mytasks.R;
import model.SubTaskOrMaterial;
import model.Task;
import utils.Constants;

/**
 * Created by ben on 19.05.16.
 */
public class TaskDetailFragment extends Fragment implements OnMapReadyCallback {

    //@BindView(R.id.task_detail_list)
    ListView mListView;
    FloatingActionButton fab;

    //private SupportMapFragment mapFragment;
    private UiSettings uiSettings;
    private TaskDetailItemAdapter mTaskDetailItemAdapter;
    private Firebase mRef;
    private String mTaskId;
    private double latitude, longitude = 0.0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        if (getArguments().containsKey(Constants.KEY_LIST_ID)) {
            mTaskId = getArguments().getString(Constants.KEY_LIST_ID);
        }
        //mapFragment = (SupportMapFragment) this.getFragmentManager().findFragmentById(R.id.map);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_detail_task, container, false);
        ButterKnife.bind(getActivity());

        mListView = (ListView) rootView.findViewById(R.id.task_detail_list);
        fab = (FloatingActionButton) rootView.findViewById(R.id.new_task_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTaskOrMaterialDialog(v);
            }
        });

        mRef = new Firebase(Constants.FIREBASE_URL_TASKS).child(mTaskId);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                latitude = task.getLatitude();
                longitude = task.getLongitude();
                if (latitude != 0.0 && longitude != 0.0) {
                    //mapFragment.getMapAsync(TaskDetailFragment.this);
                } else {
                    //mapFragment.getView().setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase listItemsRfef = new Firebase(Constants.FIREBASE_URL_SUBTASKS).child(mTaskId);

        mTaskDetailItemAdapter = new TaskDetailItemAdapter(getActivity(), SubTaskOrMaterial.class, R.layout.item_materials, listItemsRfef, mTaskId);
        mListView.setAdapter(mTaskDetailItemAdapter);


        return rootView;
    }

    public void showAddTaskOrMaterialDialog(View view) {
        DialogFragment dialog = AddTaskOrMaterialDialog.newInstance(mTaskId);
        dialog.show(this.getFragmentManager(), getContext().getResources().getString(R.string.add_task_or_material_dialog));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        uiSettings = googleMap.getUiSettings();
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
        uiSettings.setZoomControlsEnabled(true);
    }

}
