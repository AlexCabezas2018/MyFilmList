package com.example.myfilmlist.integration.daofilm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    /**
     * Returns if the film exists in the table.
     * @param idGiven
     * @return
     */
    public boolean isFilmInDB(String idGiven) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_IMDB_ID + "== '" + idGiven + "';"; //This query

        SQLiteDatabase db = getWritableDatabase();
        Cursor filmsCursor = db.rawQuery(query, null); //getting a cursor from the query
        filmsCursor.moveToFirst();

        if(filmsCursor.getCount() == 0) {
            filmsCursor.close();
            db.close();
            return false; //It's not in the database
        }
        else{
            filmsCursor.close();
            db.close();
            return true;
        }
        //It is on the database
    }

    public TFilmPreview searchFilmByTitle(String title) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + "== '" + title + "';";

        SQLiteDatabase database = getWritableDatabase();
        Cursor filmPreviewCursor = database.rawQuery(query, null);
        filmPreviewCursor.moveToFirst();

        if(filmPreviewCursor.getCount() == 0) {
            filmPreviewCursor.close();
            database.close();
            return null;
        }
        else {
            TFilmPreview toReturn = new TFilmPreview();
            toReturn.setTitle(filmPreviewCursor.getString(filmPreviewCursor.getColumnIndex(COLUMN_TITLE)));
            toReturn.setImageURL(filmPreviewCursor.getString(filmPreviewCursor.getColumnIndex(COLUMN_IMG_URL)));
            toReturn.setImdbID(filmPreviewCursor.getString(filmPreviewCursor.getColumnIndex(COLUMN_IMDB_ID)));
            toReturn.setType(filmPreviewCursor.getString(filmPreviewCursor.getColumnIndex(COLUMN_TYPE)));
            toReturn.setYear(filmPreviewCursor.getString(filmPreviewCursor.getColumnIndex(COLUMN_YEAR)));

            return toReturn;
        }
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
     * Delete a film from the viewed ones.
     * @param filmIdToDelete
     */
    public void deleteFilm(String filmIdToDelete) throws DAOException {
        /*  Setting up the values to insert into de database  */
        boolean checkIfExists = isFilmInDB(filmIdToDelete);
        if(checkIfExists) { //If the film is viewed...
            SQLiteDatabase database = getWritableDatabase();
            database.delete(TABLE_NAME, COLUMN_IMDB_ID + "='" + filmIdToDelete + "'", null);
            database.close(); //Closes the instance to the database
        }
        else throw new DAOException("The film isn't added to viewed films!");
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
