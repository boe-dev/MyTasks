package de.boe_dev.mytasks.ui.materials;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.boe_dev.mytasks.R;
import model.Material;

/**
 * Created by ben on 05.05.16.
 */
public class MaterialsAdapter extends FirebaseListAdapter<Material>{

    @BindView(R.id.item_material_name) TextView materialNameText;
    @BindView(R.id.item_material_checkbox) CheckBox materialCheckBox;

    public MaterialsAdapter(Activity activity, Class<Material> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        ButterKnife.bind(activity);
    }

    @Override
    protected void populateView(View v, Material list) {
        super.populateView(v, list);
        materialNameText.setText(list.getName());
        //materialCheckBox.setChecked(list.isChecked());
    }
}
