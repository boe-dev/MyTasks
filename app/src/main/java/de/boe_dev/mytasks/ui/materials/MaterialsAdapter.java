package de.boe_dev.mytasks.ui.materials;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import data.TaskContract;
import de.boe_dev.mytasks.R;
import model.SubTaskOrMaterial;

/**
 * Created by ben on 05.05.16.
 */
public class MaterialsAdapter extends CursorAdapter{

    public static class ViewHolder {
        public final TextView materialNameText;
        public final CheckBox materialCheckBox;

        public ViewHolder(View view) {
            materialNameText = (TextView) view.findViewById(R.id.item_material_name);
            materialCheckBox = (CheckBox) view.findViewById(R.id.item_material_checkbox);
        }
    }

    public MaterialsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_materials, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.materialNameText.setText(cursor.getString(2));
        viewHolder.materialCheckBox.setChecked((cursor.getInt(3) == 1));
        viewHolder.materialCheckBox.setTag(cursor.getInt(0));
        viewHolder.materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ContentValues values = new ContentValues();
                values.put("checked", (isChecked) ? 1 : 0);
                context.getContentResolver().update(TaskContract.MaterialEntry.CONTENT_URI, values, "_id = ?", new String[] {String.valueOf(buttonView.getTag())});
            }
        });
    }
}
