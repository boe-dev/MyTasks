package data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ben on 20.05.16.
 */
public class TaskProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TaskDbHelper mOpenHelper;

    static final int MATERIAL = 100;
    static final int TASKS = 200;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TaskContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, TaskContract.PATH_MATERIAL, MATERIAL);
        matcher.addURI(authority, TaskContract.PATH_TASK, TASKS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TaskDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MATERIAL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TaskContract.MaterialEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case TASKS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
        }

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MATERIAL: {
                return TaskContract.MaterialEntry.CONTENT_TYPE;
            }

            case TASKS: {
                return TaskContract.TaskEntry.CONTENT_TYPE;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case MATERIAL: {
                long _id = db.insert(TaskContract.MaterialEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TaskContract.MaterialEntry.buildMaterialUri(_id);
                break;
                }


            case TASKS: {
                long _id = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = TaskContract.TaskEntry.buildMaterialUri(_id);
                break;
            }

        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = 0;
        if ( null == selection ) selection = "1";

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MATERIAL: {
                rowsDeleted = db.delete(TaskContract.MaterialEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case TASKS: {
                rowsDeleted = db.delete(TaskContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsUpdated = 0;
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MATERIAL: {
                rowsUpdated = db.update(TaskContract.MaterialEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }

            case TASKS: {
                rowsUpdated = db.update(TaskContract.TaskEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }



}
