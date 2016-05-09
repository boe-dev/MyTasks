package utils;

import de.boe_dev.mytasks.BuildConfig;

/**
 * Created by ben on 05.05.16.
 */
public class Constants {

    public static final String FIREBASE_LOCATION_TASKS = "taskList";
    public static final String FIREBASE_LOCATION_SUBTASKS = "subTasks";
    public static final String FIREBASE_LOCATION_MATERIALS = "materials";

    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";

    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_TASKS = FIREBASE_URL + "/" + FIREBASE_LOCATION_TASKS;
    public static final String FIREBASE_URL_SUBTASKS = FIREBASE_URL + "/" + FIREBASE_LOCATION_SUBTASKS;
    public static final String FIREBASE_URL_MATERIALS = FIREBASE_URL + "/" + FIREBASE_LOCATION_MATERIALS;

    public static final String KEY_LIST_ID = "LIST_ID";
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";

    public static final String PASSWORD_PROVIDER = "password";
    public static final String GOOGLE_PROVIDER = "google";

}
