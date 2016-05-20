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

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TaskContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, TaskContract.PATH_MATERIAL, MATERIAL);
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

        if (sUriMatcher.match(uri) == MATERIAL) {
            retCursor = mOpenHelper.getReadableDatabase().query(
                    TaskContract.MaterialEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        }

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        if (sUriMatcher.match(uri) == MATERIAL) {
            return TaskContract.MaterialEntry.CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        if (match == MATERIAL) {
            long _id = db.insert(TaskContract.MaterialEntry.TABLE_NAME, null, values);
            if ( _id > 0 )
                returnUri = TaskContract.MaterialEntry.buildMaterialUri(_id);
            else
                throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rowsDeleted = 0;
        if ( null == selection ) selection = "1";

        if (sUriMatcher.match(uri) == MATERIAL) {
            rowsDeleted = db.delete(TaskContract.MaterialEntry.TABLE_NAME, selection, selectionArgs);
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

        if (sUriMatcher.match(uri) == MATERIAL) {
            rowsUpdated = db.update(TaskContract.MaterialEntry.TABLE_NAME, values, selection,
                    selectionArgs);
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
