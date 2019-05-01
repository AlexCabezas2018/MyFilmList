package com.example.myfilmlist.integration.daoreview.sqlitehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfilmlist.business.review.TReview;

public class SQLiteHandlerReviews extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reviews.db";
    private static final String TABLE_NAME = "reviews";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_IMDB_ID = "imdbId";
    private static final String COLUMN_CONTENT = "content";


    public SQLiteHandlerReviews(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COLUMN_IMDB_ID + " TEXT ," + COLUMN_CONTENT + " TEXT );";

        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Adds a new Review
     */
    public void addReview(TReview review) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMDB_ID, review.getImdbId());
        values.put(COLUMN_CONTENT, review.getContent());
        SQLiteDatabase db = getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //FOR DEBUG
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex(COLUMN_IMDB_ID)) != null) {
                dbString += c.getString(c.getColumnIndex(COLUMN_IMDB_ID)) + "\n";
                c.moveToNext();
            }
        }

        db.close();
        return dbString;
    }

}
