package utils;

import de.boe_dev.mytasks.BuildConfig;

/**
 * Created by ben on 05.05.16.
 */
public class Constants {

    public static final String FIREBASE_LOCATION_TASKS = "taskList";
    public static final String FIREBASE_LOCATION_SUBTASKS = "subTasks";
    public static final String FIREBASE_LOCATION_USERS = "users";

    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";
    public static final String FIREBASE_PROPERTY_EMAIL = "email";

    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_TASKS = FIREBASE_URL + "/" + FIREBASE_LOCATION_TASKS;
    public static final String FIREBASE_URL_SUBTASKS = FIREBASE_URL + "/" + FIREBASE_LOCATION_SUBTASKS;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;

    public static final String KEY_LIST_ID = "LIST_ID";
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
    public static final String KEY_PROVIDER = "PROVIDER";
    public static final String KEY_ENCODED_EMAIL = "ENCODED_EMAIL";
    public static final String KEY_LIST_OWNDER = "LIST_OWNER";
    public static final String KEY_GOOGLE_EMAIL = "GOOGLE_EMAIL";


    public static final String PASSWORD_PROVIDER = "password";
    public static final String GOOGLE_PROVIDER = "google";
    public static final String PROVIDER_DATA_DISPLAY_NAME = "displayName";

}
