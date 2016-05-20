package de.boe_dev.mytasks.ui.materials;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.Firebase;

import butterknife.ButterKnife;
import data.TaskContract;
import data.TaskProvider;
import de.boe_dev.mytasks.R;
import model.SubTaskOrMaterial;
import utils.Constants;

/**
 * Created by benny on 03.05.16.
 */
public class MaterialsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private MaterialsAdapter mMaterialAdapter;
    // private Uri mUri = Uri.parse("content://de.boe_dev.mytasks/material/100");
    private Uri mUri = TaskContract.MaterialEntry.CONTENT_URI;

    private static final String[] DETAIL_COLUMNS = {
            TaskContract.MaterialEntry.TABLE_NAME + "." + TaskContract.MaterialEntry.COLUMN_ID,
            TaskContract.MaterialEntry.COLUMN_TASK_ID,
            TaskContract.MaterialEntry.COLUMN_NAME,
            TaskContract.MaterialEntry.COLUMN_CHECKED
    };




    //@BindView(R.id.list_view_materials) ListView materialList;

    public MaterialsFragment() {
    }

    public static MaterialsFragment newInstance(){
        MaterialsFragment fragment = new MaterialsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_materials, container, false);
        ButterKnife.bind(getActivity());

//        Firebase ref = new Firebase(Constants.FIREBASE_URL_SUBTASKS);
//        mMaterialAdapter = new MaterialsAdapter(getActivity(), SubTaskOrMaterial.class, R.layout.item_materials, ref);
//        ListView materialList = (ListView) rootview.findViewById(R.id.list_view_materials);
//        materialList.setAdapter(mMaterialAdapter);

        return rootview;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mMaterialAdapter.cleanup();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            do {
                Log.v("MaterialFragment, ", data.getString(2));
            } while (data.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
