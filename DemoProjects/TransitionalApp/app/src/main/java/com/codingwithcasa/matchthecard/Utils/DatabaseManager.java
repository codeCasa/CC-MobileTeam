package com.codingwithcasa.matchthecard.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class DatabaseManager extends SQLiteOpenHelper {

    //The string of SQL needed to create the stats table
    private static final String SQL_CREATE_STATS_TABLE =
            "CREATE TABLE " + DatabaseContract.GameStatistics.TABLE_NAME + " (" +
                    DatabaseContract.GameStatistics.COL_ID + " TEXT, " +
                    DatabaseContract.GameStatistics.COL_NUM_MOVES + " INTEGER, " +
                    DatabaseContract.GameStatistics.COL_TIME + " INTEGER," +
                    DatabaseContract.GameStatistics.COL_DIFFICULTY + " TEXT, " +
                    "PRIMARY KEY (" + DatabaseContract.GameStatistics.COL_ID + "))";

    //The string of SQL needed to create the high scores table based on time
    private static final String SQL_CREATE_HIGH_SCORES_TIME_TABLE =
            "CREATE TABLE " + DatabaseContract.HighScoresTime.TABLE_NAME + " (" +
                    DatabaseContract.HighScoresTime.COL_ID + " TEXT, " +
                    DatabaseContract.HighScoresTime.COL_TIME + " INTEGER," +
                    DatabaseContract.HighScoresTime.COL_PLAYER_NAME + " TEXT," +
                    DatabaseContract.HighScoresTime.COL_DIFFICULTY + " TEXT, " +
                    "PRIMARY KEY (" + DatabaseContract.HighScoresTime.COL_ID + "))";

    //The string of SQL needed to create the high scores table based on number of moves
    private static final String SQL_CREATE_HIGH_SCORES_MOVES_TABLE =
            "CREATE TABLE " + DatabaseContract.HighScoresTime.TABLE_NAME + " (" +
                    DatabaseContract.HighScoresMoves.COL_ID + " TEXT, " +
                    DatabaseContract.HighScoresMoves.COL_NUM_MOVES + " INTEGER, " +
                    DatabaseContract.HighScoresMoves.COL_PLAYER_NAME + " TEXT," +
                    DatabaseContract.HighScoresMoves.COL_DIFFICULTY + " TEXT, " +
                    "PRIMARY KEY (" + DatabaseContract.HighScoresMoves.COL_ID + "))";


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
        db.execSQL(SQL_CREATE_HIGH_SCORES_TIME_TABLE);
        db.execSQL(SQL_CREATE_HIGH_SCORES_MOVES_TABLE);
    }

    /*
    Reverts the database to a scrubbed version of itself. Deletes all existing tables (and any data
    that happens to be in them at the time) and recreating the same empty tables
    */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.HighScoresTime.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.HighScoresMoves.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.GameStatistics.TABLE_NAME);
        onCreate(db);
    }


    /*
    Helper method to insert a new record into the stats table. Returns false if the operation
    fails and is not completed.
     */
    public boolean insertResult(String id, int numMoves, int timeTaken, boolean updateHighScoresTime,
                                boolean updateHighScoresMoves, String username, String difficulty) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.GameStatistics.COL_ID, id);
        values.put(DatabaseContract.GameStatistics.COL_TIME, timeTaken);
        values.put(DatabaseContract.GameStatistics.COL_NUM_MOVES, numMoves);
        values.put(DatabaseContract.GameStatistics.COL_DIFFICULTY, difficulty);

        long row = myDB.insert(DatabaseContract.GameStatistics.TABLE_NAME, null, values);
        if (row == -1) return false;

        if (updateHighScoresTime) {
            updateHighScoresTime(id, timeTaken, username, difficulty);
        }

        if (updateHighScoresMoves) {
            updateHighScoresMoves(id, numMoves, username, difficulty);
        }
        return true;
    }

    /*
    INCOMPLETE: Updates highscores table with given entry. If the table contains more than 10 entries
     after that insertion. Removes the lowest entry.
     */
    private boolean updateHighScoresTime(String id, int timeTaken, String username,
                                     String difficulty) {
        //insert new entry
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.HighScoresTime.COL_ID, id);
        values.put(DatabaseContract.HighScoresTime.COL_TIME, timeTaken);
        values.put(DatabaseContract.HighScoresTime.COL_DIFFICULTY, difficulty);
        values.put(DatabaseContract.HighScoresTime.COL_PLAYER_NAME, username);

        long row = myDB.insert(DatabaseContract.HighScoresTime.TABLE_NAME, null, values);

        if (row == -1) return false;

        //check number of entries now
        String query = "SELECT COUNT(*) FROM " + DatabaseContract.HighScoresTime.TABLE_NAME + " WHERE (" +
                DatabaseContract.HighScoresTime.COL_DIFFICULTY + " = \"" + difficulty + "\")";
        Cursor countCursor = myDB.rawQuery(query,null);
        countCursor.moveToFirst();
        int numEntries = countCursor.getInt(0);

        //if number of entries is more than ten, remove the lowest score
        if (numEntries > 10) {
            String removingID = fetchLowestHighScoreTimeID(difficulty);
            String deleteQuery = "DELETE FROM " + DatabaseContract.HighScoresTime.TABLE_NAME +
                    " WHERE " + DatabaseContract.HighScoresTime.COL_ID + " = " + removingID;
            Cursor delCursor = myDB.rawQuery(deleteQuery,null);
        }

        return true;
    }



    private boolean updateHighScoresMoves(String id, int numMoves, String username,
                                         String difficulty) {
        //insert new entry
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.HighScoresMoves.COL_ID, id);
        values.put(DatabaseContract.HighScoresMoves.COL_NUM_MOVES, numMoves);
        values.put(DatabaseContract.HighScoresMoves.COL_DIFFICULTY, difficulty);
        values.put(DatabaseContract.HighScoresMoves.COL_PLAYER_NAME, username);

        long row = myDB.insert(DatabaseContract.HighScoresMoves.TABLE_NAME, null, values);

        if (row == -1) return false;

        //check number of entries now
        String query = "SELECT COUNT(*) FROM " + DatabaseContract.HighScoresMoves.TABLE_NAME + " WHERE (" +
                DatabaseContract.HighScoresMoves.COL_DIFFICULTY + " = \"" + difficulty + "\")";
        Cursor countCursor = myDB.rawQuery(query,null);
        countCursor.moveToFirst();
        int numEntries = countCursor.getInt(0);

        //if number of entries is more than ten, remove the lowest score
        if (numEntries > 10) {
            String removingID = fetchLowestHighScoreMovesID(difficulty);
            String deleteQuery = "DELETE FROM " + DatabaseContract.HighScoresMoves.TABLE_NAME +
                    " WHERE " + DatabaseContract.HighScoresMoves.COL_ID + " = " + removingID;
            Cursor delCursor = myDB.rawQuery(deleteQuery,null);
        }

        return true;
    }

    /*
    Function to fetch the id of lowest highscore entry (with longest play time for the given difficulty)
     */
    private String fetchLowestHighScoreTimeID(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScoresTime.COL_ID + " FROM " +
                DatabaseContract.HighScoresTime.TABLE_NAME + " WHERE (" + DatabaseContract.HighScoresTime.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScoresTime.COL_TIME +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        String maxTimeID = null;
        while (cursor.moveToNext()) {
            maxTimeID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.HighScoresTime.COL_ID));
        }

        return maxTimeID;
    }

    /*
    Function to fetch the id of lowest highscore entry (with most moves for the given difficulty)
     */
    private String fetchLowestHighScoreMovesID(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScoresMoves.COL_ID + " FROM " +
                DatabaseContract.HighScoresMoves.TABLE_NAME + " WHERE (" + DatabaseContract.HighScoresMoves.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScoresMoves.COL_NUM_MOVES +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        String maxMovesID = null;
        while (cursor.moveToNext()) {
            maxMovesID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.HighScoresMoves.COL_ID));
        }

        return maxMovesID;
    }

    /*
    Function to fetch the the lowest highscore based on time for a given difficulty
     */
    public int fetchLongestTime(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScoresTime.COL_TIME + " FROM " +
                DatabaseContract.HighScoresTime.TABLE_NAME + " WHERE (" + DatabaseContract.HighScoresTime.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScoresTime.COL_TIME +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        int maxTime = -1;
        while (cursor.moveToNext()) {
            maxTime = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.HighScoresTime.COL_TIME));
        }

        return maxTime;
    }

    /*
    Function to fetch the the lowest highscore based on number of moves for a given difficulty
     */
    public int fetchMostMoves(String difficulty) {
        String query = "SELECT " + DatabaseContract.HighScoresMoves.COL_NUM_MOVES + " FROM " +
                DatabaseContract.HighScoresMoves.TABLE_NAME + " WHERE (" + DatabaseContract.HighScoresMoves.COL_DIFFICULTY +
                " = \"" + difficulty + "\") ORDER BY " + DatabaseContract.HighScoresMoves.COL_NUM_MOVES +
                " DESC LIMIT 1";

        Cursor cursor = myDB.rawQuery(
                query,
                null
        );

        int maxMoves = -1;
        while (cursor.moveToNext()) {
            maxMoves = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.HighScoresMoves.COL_NUM_MOVES));
        }

        return maxMoves;
    }

}
