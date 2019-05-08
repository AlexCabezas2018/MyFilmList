package com.example.myfilmlist.integration.daofilm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myfilmlist.business.film.TFilmPreview;
import com.example.myfilmlist.exceptions.DAOException;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHandlerViewedFilms extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "viewed_films.db";
    private static final String TABLE_NAME = "VIEWEDFILMS";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMDB_ID = "imdbId";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_IMG_URL = "img_url";

    public SQLiteHandlerViewedFilms(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_IMDB_ID + " TEXT PRIMARY KEY NOT NULL, "
                + COLUMN_YEAR + " TEXT, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_IMG_URL + " TEXT );";

        db.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)  {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean isFilmInDB(String idGiven) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IMDB_ID + "== '" + idGiven + "';"; //This query returns everything from the table.

        SQLiteDatabase db = getWritableDatabase();
        Cursor filmsCursor = db.rawQuery(query, null); //getting a cursor from the query
        filmsCursor.moveToFirst();

        if(filmsCursor.getCount() == 0) return false; //It's not in the database
        else return true; //It is on the database
    }


    /**
     * Adds a film to the viewed ones.
     * @param filmToInsert
     */
    public void insertFilm(TFilmPreview filmToInsert) throws DAOException {
        /*  Setting up the values to insert into de database  */
        boolean checkIfNotExists = isFilmInDB(filmToInsert.getImdbID());
        if(!checkIfNotExists) { //If the review not exists...
            ContentValues values = new ContentValues();
            values.put(COLUMN_IMDB_ID, filmToInsert.getImdbID());
            values.put(COLUMN_TITLE, filmToInsert.getTitle());
            values.put(COLUMN_TYPE, filmToInsert.getType());
            values.put(COLUMN_YEAR, filmToInsert.getYear());
            values.put(COLUMN_IMG_URL, filmToInsert.getImageURL());


            SQLiteDatabase database = getWritableDatabase();
            database.insert(TABLE_NAME, null, values); //Inserting the values.
            database.close(); //Closes the instance to the database
        }
        else throw new DAOException("The film has already seen!");
    }

    /**
     * Returns all the viewed films from the table.
     * @return Collection
     * @throws DAOException
     */
    public List<TFilmPreview> getAllViewedFilms() throws DAOException {
        List<TFilmPreview> toReturn = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1"; //This query returns everything from the table.

        SQLiteDatabase db = getWritableDatabase();
        Cursor filmsCursor = db.rawQuery(query, null); //getting a cursor from the query
        filmsCursor.moveToFirst();

        if(filmsCursor.getCount() == 0) return null;

        while(!filmsCursor.isAfterLast()) { //Iterates to all the viewed films
            String title, imdbId, type, imgUrl, year;

            /* Checking if the data is corrupted */
            boolean isTitleNotNull = (title = filmsCursor.getString(filmsCursor.getColumnIndex(COLUMN_TITLE))) != null;
            boolean isImdbIdNotNull = (imdbId = filmsCursor.getString(filmsCursor.getColumnIndex(COLUMN_IMDB_ID))) != null;
            boolean isTypeNotNull = (type = filmsCursor.getString(filmsCursor.getColumnIndex(COLUMN_TYPE))) != null;
            boolean isImgUrlNotNull = (imgUrl = filmsCursor.getString(filmsCursor.getColumnIndex(COLUMN_IMG_URL))) != null;
            boolean isYearNotNull = (year = filmsCursor.getString(filmsCursor.getColumnIndex(COLUMN_YEAR))) != null;

            if(isImdbIdNotNull && isTitleNotNull &&
                    isTypeNotNull && isYearNotNull && isImgUrlNotNull)
                        toReturn.add(new TFilmPreview(title, year, imdbId, type, imgUrl)); //Add the film to the list
            else throw new DAOException("Database exception in table " + TABLE_NAME + ". " +
                    "The data is corrupted. Please reinstall the application.");

            filmsCursor.moveToNext();
        }

        /* Closes everything */
        filmsCursor.close();
        db.close();

        return toReturn;
    }

}
