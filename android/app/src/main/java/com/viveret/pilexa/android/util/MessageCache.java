package com.viveret.pilexa.android.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by viveret on 3/8/17.
 */
public class MessageCache extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PilexaMessage.db";
    private static final String TABLE_NAME = "Messages";


    private static final String SQL_CREATE_ENTRIES = ""
            + "CREATE TABLE `" + TABLE_NAME + "` ("
            + "`id` INTEGER PRIMARY KEY NOT NULL,"
            + "`body` VARCHAR(200) NOT NULL,"
            + "`isFromUser` BOOLEAN NOT NULL);";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS `" + TABLE_NAME + "`;";

    public MessageCache(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void insert(Message m) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("body", m.body);
        values.put("isFromUser", m.isFromUser);

        m.setId(db.insert(TABLE_NAME, null, values));
    }

    public void clearMessages() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "1=1", new String[]{});
    }

    public List<Message> getMessages() {
        final String[] cols = new String[]{"id", "body", "isFromUSer"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,                     // The table to query
                cols,                           // The columns to return
                "",                 // The columns for the WHERE clause
                new String[]{},                 // The values for the WHERE clause
                null,                 // don't group the rows
                null,                  // don't filter by row groups
                null                  // The sort order
        );

        List<Message> ret = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
            boolean isFromUser = cursor.getInt(cursor.getColumnIndexOrThrow("isFromUser")) != 0;
            ret.add(new Message(itemId, body, isFromUser));
        }
        cursor.close();

        Collections.reverse(ret);
        return ret;
    }
}
