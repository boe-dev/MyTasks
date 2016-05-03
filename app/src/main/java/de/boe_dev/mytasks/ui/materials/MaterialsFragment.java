package de.boe_dev.mytasks.ui.materials;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.boe_dev.mytasks.R;

/**
 * Created by benny on 03.05.16.
 */
public class MaterialsFragment extends Fragment {

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
        return rootview;
    }
}
