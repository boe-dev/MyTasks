package de.boe_dev.mytasks.ui.taskDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.boe_dev.mytasks.R;
import de.boe_dev.mytasks.ui.BaseActivity;
import model.SubTaskOrMaterial;
import model.Task;
import utils.Constants;

/**
 * Created by ben on 05.05.16.
 */
public class TaskDetailActivity extends BaseActivity implements OnMapReadyCallback{

    @BindView(R.id.task_detail_list) ListView mListView;

    private SupportMapFragment mapFragment;
    private UiSettings uiSettings;
    private TaskDetailItemAdapter mTaskDetailItemAdapter;
    private Firebase mRef;
    private String mTaskId;
    private double latitude, longitude = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_task);
        ButterKnife.bind(this);
        Intent intent = this.getIntent();
        mTaskId = intent.getStringExtra(Constants.KEY_LIST_ID);
        if (mTaskId == null) {
            finish();
            return;
        }

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mRef = new Firebase(Constants.FIREBASE_URL_TASKS).child(mTaskId);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Task task = dataSnapshot.getValue(Task.class);
                latitude = task.getLatitude();
                longitude = task.getLongitude();
                if (latitude != 0.0 && longitude != 0.0) {
                    mapFragment.getMapAsync(TaskDetailActivity.this);
                } else {
                    mapFragment.getView().setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase listItemsRfef = new Firebase(Constants.FIREBASE_URL_SUBTASKS).child(mTaskId);

        mTaskDetailItemAdapter = new TaskDetailItemAdapter(this, SubTaskOrMaterial.class, R.layout.item_materials, listItemsRfef, mTaskId);
        mListView.setAdapter(mTaskDetailItemAdapter);





    }

    public void showAddTaskOrMaterialDialog(View view) {
        DialogFragment dialog = AddTaskOrMaterialDialog.newInstance(mTaskId);
        dialog.show(this.getSupportFragmentManager(), getApplicationContext().getResources().getString(R.string.add_task_or_material_dialog));
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
