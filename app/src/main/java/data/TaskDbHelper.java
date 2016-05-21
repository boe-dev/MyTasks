package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ben on 20.05.16.
 */
public class TaskDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mytasks.db";

    private static final String SQL_CREATE_MATERIALS = "CREATE TABLE "+ TaskContract.MaterialEntry.TABLE_NAME +" (" +
            TaskContract.MaterialEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
            TaskContract.MaterialEntry.COLUMN_TASK_ID + " TEXT NOT NULL, " +
            TaskContract.MaterialEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            TaskContract.MaterialEntry.COLUMN_CHECKED +  " INTEGER)";

    private static final String SQL_CREATE_TASKS = "CREATE TABLE "+ TaskContract.TaskEntry.TABLE_NAME +" (" +
            TaskContract.TaskEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
            TaskContract.TaskEntry.COLUMN_TASK_ID + " TEXT NOT NULL, " +
            TaskContract.TaskEntry.COLUMN_NAME + " TEXT NOT NULL)";

    public TaskDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_MATERIALS);
        db.execSQL(SQL_CREATE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.MaterialEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
