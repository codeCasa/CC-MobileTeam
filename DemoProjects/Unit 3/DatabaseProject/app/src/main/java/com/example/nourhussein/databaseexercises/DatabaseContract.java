package com.example.nourhussein.databaseexercises;

import android.provider.BaseColumns;

/**
 * Created by nourhussein
 */

public final class DatabaseContract {

    private DatabaseContract() {

    }

    public static class Candyland implements BaseColumns {
        public static final String TABLE_NAME = "candies";
        public static final String COL_NAME = "name"; //primary key
        public static final String COL_CALS = "calories";
        public static final String COL_TYPE = "type";
    }


    public static class Veggies implements BaseColumns {
        public static final String TABLE_NAME = "vegetables";
        public static final String COL_NAME = "name"; //primary key
        public static final String COL_CALS = "calories";
        public static final String COL_COLOR = "color";
    }

    public static class Kiddo implements BaseColumns {
        public static final String TABLE_NAME = "children";
        public static final String COL_NAME = "name"; //primary key
        public static final String COL_AGE = "age";
        public static final String COL_FAV_CANDY = "favorite_candy_name"; //foreign key
        public static final String COL_FAV_VEG = "favorite_vegetable_name"; //foreign key
    }
}
