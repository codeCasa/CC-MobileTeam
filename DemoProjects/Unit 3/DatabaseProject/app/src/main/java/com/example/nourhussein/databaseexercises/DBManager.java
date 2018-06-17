package com.example.nourhussein.databaseexercises;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nourhussein
 */

public class DBManager extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "FingerExercises.db";

    private static final String SQL_CREATE_VEG_TABLE =
            "CREATE TABLE " + DatabaseContract.Veggies.TABLE_NAME + " (" +
                    DatabaseContract.Veggies.COL_NAME + " TEXT, " +
                    DatabaseContract.Veggies.COL_COLOR + " TEXT, " +
                    DatabaseContract.Veggies.COL_CALS + " INTEGER," +
                    "PRIMARY KEY (" + DatabaseContract.Veggies.COL_NAME + "))";

    private static final String SQL_DELETE_TABLE_STARTER =
            "DROP TABLE IF EXISTS ";



    /*
     Ask Student: How do I go about defining the SQL to swt up the other tables defined in DatabaseContract?
     Work together to arrive at:
    */
     private static final String SQL_CREATE_KIDS_TABLE =
     "CREATE TABLE " + DatabaseContract.Kiddo.TABLE_NAME + " (" +
     DatabaseContract.Kiddo.COL_NAME + " TEXT, " +
     DatabaseContract.Kiddo.COL_AGE + " INTEGER, " +
     DatabaseContract.Kiddo.COL_FAV_VEG + " TEXT, " +
     DatabaseContract.Kiddo.COL_FAV_CANDY + " TEXT, " +
     "FOREIGN KEY (" + DatabaseContract.Kiddo.COL_FAV_CANDY + ") REFERENCES " +
     DatabaseContract.Candyland.TABLE_NAME + " (" + DatabaseContract.Candyland.COL_NAME + "), " +
     "FOREIGN KEY (" + DatabaseContract.Kiddo.COL_FAV_VEG + ") REFERENCES " +
     DatabaseContract.Veggies.TABLE_NAME + " (" + DatabaseContract.Veggies.COL_NAME + "), " +
     "PRIMARY KEY (" + DatabaseContract.Kiddo.COL_NAME + "))";

     private static final String SQL_CREATE_CANDY_TABLE =
     "CREATE TABLE " + DatabaseContract.Candyland.TABLE_NAME + " (" +
     DatabaseContract.Candyland.COL_NAME + " TEXT, " +
     DatabaseContract.Candyland.COL_TYPE + " TEXT, " +
     DatabaseContract.Candyland.COL_CALS + " INTEGER," +
     "PRIMARY KEY (" + DatabaseContract.Candyland.COL_NAME + "))";

     /*
     END SOLUTION
      */
    private static DBManager instance = null;
    private SQLiteDatabase myDB = null;

    private DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        myDB = getWritableDatabase();
    }

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_VEG_TABLE);
        db.execSQL(SQL_CREATE_KIDS_TABLE);
        db.execSQL(SQL_CREATE_CANDY_TABLE);
    }

    @Override //required function, simply deletes the tables and creates new empty versions of them
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_STARTER + DatabaseContract.Veggies.TABLE_NAME);
        db.execSQL(SQL_DELETE_TABLE_STARTER + DatabaseContract.Candyland.TABLE_NAME);
        db.execSQL(SQL_DELETE_TABLE_STARTER + DatabaseContract.Kiddo.TABLE_NAME);
        onCreate(db);
    }

    //method to insert into vegetables table, return false if there was an error in inserting teh new value, true otherwise

    public boolean insertVeggie(String name, int calories, String color) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.Veggies.COL_NAME, name);
        values.put(DatabaseContract.Veggies.COL_COLOR, color);
        values.put(DatabaseContract.Veggies.COL_CALS, calories);

        long row = myDB.insert(DatabaseContract.Veggies.TABLE_NAME, null, values);
        if (row == -1) return false;
        return true;
    }


    /*
     * Ask student to help you write the code for inserting into the remaining two tables
     *
     * Solution is:
     *
     */

    public boolean insertCandy (String name, String type, int calories) {
        ContentValues vals = new ContentValues();
        vals.put(DatabaseContract.Candyland.COL_NAME, name);
        vals.put(DatabaseContract.Candyland.COL_TYPE, type);
        vals.put(DatabaseContract.Candyland.COL_CALS, calories);

        long row = myDB.insert(DatabaseContract.Candyland.TABLE_NAME, null, vals);
        if (row == -1) return false;
        return true;
    }


    public boolean insertChild (String name, int age, String candy_fav, String veg_fav) {
        ContentValues vals = new ContentValues();
        vals.put(DatabaseContract.Kiddo.COL_NAME, name);
        vals.put(DatabaseContract.Kiddo.COL_AGE, age);
        vals.put(DatabaseContract.Kiddo.COL_FAV_CANDY, candy_fav);
        vals.put(DatabaseContract.Kiddo.COL_FAV_VEG, veg_fav);

        long row = myDB.insert(DatabaseContract.Kiddo.TABLE_NAME, null, vals);
        if (row == -1) return false;
        return true;
    }

    /*
    END SOLUTION
     */


    //helper functions that poll the edibles databases and fetch their data as an arraylist
    public List<String> pollVeggies() {
        String[] select_cols = null;
        String from = DatabaseContract.Veggies.TABLE_NAME;
        String where_col_name = null;
        String [] where_col_conditions = null;
        String sortOrder = null;

        Cursor cursor = myDB.query(
                from,
                select_cols,
                where_col_name,
                where_col_conditions,
                null, //grouping conditions, won't deal with these guys for now
                null, //grouping conditions, won't deal with these guys for now
                sortOrder
        );

        StringBuilder sb = new StringBuilder();
        List<String> items = new ArrayList();
        //iterate through all results, row by row.
        //for simplicity, here I am simply concatenating the data into a comma separated string to display in my listview
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Veggies.COL_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Veggies.COL_COLOR));
            int calories = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Veggies.COL_CALS));

            sb.append(name);
            sb.append(" , ");
            sb.append(type);
            sb.append(" , ");
            sb.append(calories);

            items.add(sb.toString());
            sb.setLength(0); //clear the string builder
        }
        return items;
    }

    public List<String> pollCandies(){
        String[] select_cols = null;
        String from = DatabaseContract.Candyland.TABLE_NAME;
        String where_col_name = null;
        String [] where_col_conditions = null;
        String sortOrder = null;

        Cursor cursor = myDB.query(
                from,
                select_cols,
                where_col_name,
                where_col_conditions,
                null, //grouping conditions, won't deal with these guys for now
                null, //grouping conditions, won't deal with these guys for now
                sortOrder
        );

        StringBuilder sb = new StringBuilder();
        List<String> items = new ArrayList();
        //iterate through all results, row by row.
        //for simplicity, here I am simply concatenating the data into a comma separated string to display in my listview
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Candyland.COL_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Candyland.COL_TYPE));
            int calories = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Candyland.COL_CALS));

            sb.append(name);
            sb.append(" , ");
            sb.append(type);
            sb.append(" , ");
            sb.append(calories);

            items.add(sb.toString());
            sb.setLength(0); //clear the string builder
        }
        return items;
    }

}
