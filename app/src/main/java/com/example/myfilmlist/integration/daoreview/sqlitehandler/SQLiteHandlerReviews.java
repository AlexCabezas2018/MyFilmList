package com.example.myfilmlist.integration.daoreview.sqlitehandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfilmlist.business.review.TReview;
import com.example.myfilmlist.exceptions.DAOException;

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
        /* Setting up the values */
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMDB_ID, review.getImdbId());
        values.put(COLUMN_CONTENT, review.getContent());


        SQLiteDatabase db = getWritableDatabase(); //Instance of a writable database

        db.insert(TABLE_NAME, null, values); //Insert the row to the table
        db.close();
    }


    /**
     * Gets a Review From the database, give, an IMDB id. Returns null if the film doesn't exist
     * @param imdb
     * @return
     * @throws DAOException
     */
    public TReview getReviewFromIMDBId(int imdb) throws DAOException {
        TReview toReturn = new TReview();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IMDB_ID + " == " + imdb; //Query: Obtain everything from the table where the imdbid is equals to the given one.

        SQLiteDatabase db = getWritableDatabase();
        Cursor reviewCursor = db.rawQuery(query, null); //Execute the query and generate a cursor, which goes through all the results
        reviewCursor.moveToFirst();

        if(reviewCursor.getCount() == 0) return null; //No reviews were found

        /* In theory, there must be only 1 */
        while(!reviewCursor.isAfterLast()) {
            /* Checking if the information is not corrupted */
            boolean isIMDBNotNull = reviewCursor.getString(reviewCursor.getColumnIndex(COLUMN_IMDB_ID)) != null;
            boolean isContentNotNull = reviewCursor.getString(reviewCursor.getColumnIndex(COLUMN_CONTENT)) != null;
            boolean isIdNotNull = reviewCursor.getString(reviewCursor.getColumnIndex(COLUMN_ID)) != null;

            if(isContentNotNull && isIMDBNotNull && isIdNotNull){
                /* Setting up the review */
                toReturn.setId(Integer.parseInt(reviewCursor.getString(reviewCursor.getColumnIndex(COLUMN_ID))));
                toReturn.setImdbId(reviewCursor.getString(reviewCursor.getColumnIndex(COLUMN_IMDB_ID)));
                toReturn.setContent(reviewCursor.getString(reviewCursor.getColumnIndex(COLUMN_CONTENT)));
            }
            else {
                throw new DAOException("Error from database: The review is corrupted");
            }

            reviewCursor.moveToNext();
        }

        reviewCursor.close();
        db.close();

        return toReturn;
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

        c.close();
        db.close();
        return dbString;
    }

}
