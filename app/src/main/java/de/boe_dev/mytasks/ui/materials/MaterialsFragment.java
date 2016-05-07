package de.boe_dev.mytasks.ui.materials;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.firebase.client.Firebase;

import butterknife.ButterKnife;
import de.boe_dev.mytasks.R;
import model.SubTaskOrMaterial;
import utils.Constants;

/**
 * Created by benny on 03.05.16.
 */
public class MaterialsFragment extends Fragment {

    private MaterialsAdapter mMaterialAdapter;

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
}
