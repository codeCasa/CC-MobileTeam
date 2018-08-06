package com.codingwithcasa.matchthecard.Utils;

import android.provider.BaseColumns;

/**
 * This class just defines the different tables in the databases, as well as their columns.
 */
public final class DatabaseContract {

    private DatabaseContract() {

    }

    public static class GameStatistics implements BaseColumns {
        public static final String TABLE_NAME = "stats";
        public static final String COL_ID = "id"; //primary key
        public static final String COL_TIME = "time";
        public static final String COL_NUM_MOVES = "moves";
        public static final String COL_DIFFICULTY = "difficulty";
    }

    public static class HighScoresMoves implements BaseColumns {
        public static final String TABLE_NAME = "highScores";
        public static final String COL_ID = "id"; //primary key
        public static final String COL_PLAYER_NAME = "name";
        public static final String COL_NUM_MOVES = "moves";
        public static final String COL_DIFFICULTY = "difficulty";
    }

    public static class HighScoresTime implements BaseColumns {
        public static final String TABLE_NAME = "highScores";
        public static final String COL_ID = "id"; //primary key
        public static final String COL_PLAYER_NAME = "name";
        public static final String COL_TIME = "time";
        public static final String COL_DIFFICULTY = "difficulty";
    }
}
