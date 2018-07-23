package com.codingwithcasa.matchthecard.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    //The string of SQL needed to create the stats table
    private static final String SQL_CREATE_STATS_TABLE =
            "CREATE TABLE " + DatabaseContract.GameStatistics.TABLE_NAME + " (" +
                    DatabaseContract.GameStatistics.COL_ID + " TEXT, " +
                    DatabaseContract.GameStatistics.COL_NUM_MOVES + " INTEGER, " +
                    DatabaseContract.GameStatistics.COL_TIME + " INTEGER," +
                    DatabaseContract.GameStatistics.COL_DIFFICULTY + " TEXT, " +
                    "PRIMARY KEY (" + DatabaseContract.GameStatistics.COL_ID + "))";

    //The string of SQL needed to create the high scores table
    private static final String SQL_CREATE_HIGH_SCORES_TABLE =
            "CREATE TABLE " + DatabaseContract.HighScores.TABLE_NAME + " (" +
                    DatabaseContract.HighScores.COL_ID + " TEXT, " +
                    DatabaseContract.HighScores.COL_NUM_MOVES + " INTEGER, " +
                    DatabaseContract.HighScores.COL_TIME + " INTEGER," +
                    DatabaseContract.HighScores.COL_PLAYER_NAME + " TEXT," +
                    DatabaseContract.HighScores.COL_DIFFICULTY + " TEXT, " +
                    "PRIMARY KEY (" + DatabaseContract.HighScores.COL_ID + "))";


    private static final String DATABASE_NAME = "MatchTheCardStats.db";

    //using a singleton pattern for the DatabaseManager to prevent creating concurrent copies in memory
    private static DatabaseManager instance = null;
    private SQLiteDatabase myDB = null;


    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        myDB = getWritableDatabase();
    }

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    /*
    OnCreate method for this database manager object- creates the two tables defined using the
    strings defined above
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STATS_TABLE);
        db.execSQL(SQL_CREATE_HIGH_SCORES_TABLE);
    }

    /*
    Reverts the database to a scrubbed version of itself. Deletes all existing tables (and any data
    that happens to be in them at the time) and recreating the same empty tables
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.HighScores.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.GameStatistics.TABLE_NAME);
        onCreate(db);
    }


    /*
    Helper method to insert a new record into the stats table. Returns false if the operation
    fails and is not completed.
     */
    public boolean insertResult(String id, int numMoves, int timeTaken, boolean updateHighScores,
                                String username, String difficulty) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.GameStatistics.COL_ID, id);
        values.put(DatabaseContract.GameStatistics.COL_TIME, timeTaken);
        values.put(DatabaseContract.GameStatistics.COL_NUM_MOVES, numMoves);
        values.put(DatabaseContract.GameStatistics.COL_DIFFICULTY, difficulty);

        long row = myDB.insert(DatabaseContract.GameStatistics.TABLE_NAME, null, values);
        if (row == -1) return false;

        if (updateHighScores) {
            updateHighScores(id, numMoves, timeTaken, username, difficulty);
        }
        return true;
    }

    /*
    INCOMPLETE: Updates highscores table with given entry. If the table contains more than 10 entries
     after that insertion. Removes the lowest entry.
     */
    private boolean updateHighScores(String id, int numMoves, int timeTaken, String username,
                                     String difficulty) {
        //TODO: Complete Logic for updating high scores table
        return true;
    }

    /*
    Function to fetch the id of lowest highscore entry (with longest play time for the given difficulty)
     */
    private String fetchLowestHighScoreTimeID(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScores.COL_ID + " FROM " +
                DatabaseContract.HighScores.TABLE_NAME + " WHERE (" + DatabaseContract.HighScores.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScores.COL_TIME +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        String maxTimeID = null;
        while (cursor.moveToNext()) {
            maxTimeID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.HighScores.COL_ID));
        }

        return maxTimeID;
    }

    /*
    INCOMPLETE: Function to fetch the id of lowest highscore entry (with most moves for the given difficulty)
     */
    private String fetchLowestHighScoreMovesID(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScores.COL_ID + " FROM " +
                DatabaseContract.HighScores.TABLE_NAME + " WHERE (" + DatabaseContract.HighScores.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScores.COL_NUM_MOVES +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        String maxMovesID = null;
        while (cursor.moveToNext()) {
            maxMovesID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.HighScores.COL_ID));
        }

        return maxMovesID;
    }

    /*
    Function to fetch the the lowest highscore based on time for a given difficulty
     */
    public int fetchLongestTime(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScores.COL_TIME + " FROM " +
                DatabaseContract.HighScores.TABLE_NAME + " WHERE (" + DatabaseContract.HighScores.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScores.COL_TIME +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        int maxTime = -1;
        while (cursor.moveToNext()) {
            maxTime = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.HighScores.COL_TIME));
        }

        return maxTime;
    }

    /*
    Function to fetch the the lowest highscore based on number of moves for a given difficulty
     */
    public int fetchMostMoves(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScores.COL_NUM_MOVES + " FROM " +
                DatabaseContract.HighScores.TABLE_NAME + " WHERE (" + DatabaseContract.HighScores.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScores.COL_NUM_MOVES +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        int maxMoves = -1;
        while (cursor.moveToNext()) {
            maxMoves = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.HighScores.COL_NUM_MOVES));
        }

        return maxMoves;
    }

}
