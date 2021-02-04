package com.example.MovieDB.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.MovieDB.data.Movie;

import java.util.ArrayList;


public class FavoriteDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoriteDatabase";

    private static final int DATABASE_VERSION = 1;
    //table name
    private static final String TABLE_FAVORITE = "favorite";

    public static final String LOGTAG = "FAVORITE";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;
    public FavoriteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }
    public void create(SQLiteDatabase db) {
        Log.d("seungrok", "Table생성");
        final String CREATE_TABLE = "CREATE TABLE " +
                FavoriteContract.FavoriteEntry.TABLE_NAME + "(" +
                FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID + " INTEGER, " +
                FavoriteContract.FavoriteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL " +
                ")";
        try{
            db.execSQL(CREATE_TABLE);
        }catch(SQLException e){
            //
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i != i1) {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteEntry.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
        }
    public void addFavorite(SQLiteDatabase db, Movie movie){
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie.getId());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movie.getTitle());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
        try{
            db.insert(FavoriteContract.FavoriteEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    public void deleteFavorite(SQLiteDatabase db, Movie movie){
        db.delete(FavoriteContract.FavoriteEntry.TABLE_NAME,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID+ "=" + movie.getId(), null);
    }
    public ArrayList<Movie> getAllFavorite(){
        String[] columns = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_TITLE,
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW,
                FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE

        };
        String sortOrder =
                FavoriteContract.FavoriteEntry._ID + " ASC";
        ArrayList<Movie> favoriteList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteContract.FavoriteEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                sortOrder);

        if (cursor.moveToFirst()){
            do {
                Movie movie = new Movie();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_OVERVIEW)));
                movie.setVote_average(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_VOTE_AVERAGE))));

                favoriteList.add(movie);

            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return favoriteList;
    }


}
