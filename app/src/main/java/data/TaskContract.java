package data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ben on 20.05.16.
 */
public class TaskContract {

    public static final String CONTENT_AUTHORITY = "de.boe_dev.mytasks";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_MATERIAL = "material";

    public static final class MaterialEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MATERIAL).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATERIAL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATERIAL;

        public static final String TABLE_NAME = "materials";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TASK_ID = "task_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CHECKED = "checked";

        public static Uri buildMaterialUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
