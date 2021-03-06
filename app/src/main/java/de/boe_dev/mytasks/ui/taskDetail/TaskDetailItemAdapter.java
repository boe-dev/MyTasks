package de.boe_dev.mytasks.ui.taskDetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.FirebaseRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.boe_dev.mytasks.R;
import model.SubTaskOrMaterial;

/**
 * Created by benny on 06.05.16.
 */
public class TaskDetailItemAdapter extends FirebaseListAdapter <SubTaskOrMaterial> {

    private SubTaskOrMaterial subTaskOrMaterial;
    private String mListId;

    TextView subTaskOrMaterialName;
    CheckBox checkBox;

    public TaskDetailItemAdapter(Activity activity, Class<SubTaskOrMaterial> modelClass, int modelLayout, Query ref, String listId) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
        this.mListId = listId;
    }

    public void setSubTaskOrMaterialList(SubTaskOrMaterial subTaskOrMaterialList) {
        this.subTaskOrMaterial = subTaskOrMaterialList;
        this.notifyDataSetChanged();
    }

    @Override
    protected void populateView(View view, SubTaskOrMaterial item) {

        subTaskOrMaterialName = (TextView) view.findViewById(R.id.item_material_name);
        view.findViewById(R.id.item_material_checkbox).setVisibility(View.GONE);

        subTaskOrMaterialName.setText(item.getName());
        if (item.getType() == 1) {
            view.setBackgroundColor(Color.parseColor("#11000000"));
        }

    }
}
