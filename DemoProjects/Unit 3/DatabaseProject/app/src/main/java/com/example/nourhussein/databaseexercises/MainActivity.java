package com.example.nourhussein.databaseexercises;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;
    private Button vegButton, kidButton, candyButton;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = DBManager.getInstance(this);

        /*
        IMPORTANT NOTE: Here, I am bypassing persistence by clearing the tables and inserting default values,
        for a proper db, delete lines that call onUpgrade and insertObject from this onCreate method

         */
//        dbManager.onUpgrade(dbManager.getWritableDatabase(), 1, 1);
//
//        //test inputs
//        dbManager.insertVeggie("Carrot", 25, "orange");
//        dbManager.insertVeggie("Broccoli", 15, "green");
//
//        dbManager.insertCandy("KitKat", "Chocolate", 350);
//        dbManager.insertCandy("Sour Patch Kids", "Gummy", 115);
//
//        dbManager.insertChild("John Doe", 8, "KitKat", "Broccoli");
//        dbManager.insertChild("Jane Doe", 19, "Sour Patch Kids", "Carrot");



        //set up the show veggies button
        vegButton = findViewById(R.id.show_veg_button);

        vegButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                getAllVeggies(v);
            }
        });


        //set up the other two buttons, potentially review with student how to do that
        kidButton = findViewById(R.id.show_kids_button);

        kidButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                getAllKids(v);
            }
        });

        candyButton = findViewById(R.id.show_candy_button);

        candyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                getAllCandy(v);
            }
        });


        //set up the floating action button
        listView = findViewById(R.id.main_list);
    }


    //set up the menu and menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.add_child_option:
                intent = new Intent(this, AddEntryActivity.class);
                intent.putExtra("type", "child");
                break;

            case R.id.add_veggie_option:
                intent = new Intent(this, AddEntryActivity.class);
                intent.putExtra("type", "veg");
                break;

            case R.id.add_candy_option:
                intent = new Intent(this, AddEntryActivity.class);
                intent.putExtra("type", "candy");
                break;

            case R.id.remove_child_option:
                intent = new Intent(this, RemoveEntryActivity.class);
                intent.putExtra("type", "child");
                break;

            case R.id.remove_veggie_option:
                intent = new Intent(this, RemoveEntryActivity.class);
                intent.putExtra("type", "veg");
                break;

            case R.id.remove_candy_option:
                intent = new Intent(this, RemoveEntryActivity.class);
                intent.putExtra("type", "candy");
                break;

            case R.id.search_option:
                intent = new Intent(this, SearchActivity.class);
                break;

            default:
                Log.d("ERR", "Invalid Menu Option");
        }
        startActivity(intent);
        return true;
    }


    @Override
    protected void onDestroy() {
        dbManager.close();
        super.onDestroy();
    }


    public void getAllVeggies(View view){
        SQLiteDatabase readable_db = dbManager.getReadableDatabase();

        String[] select_cols = null; //which columns we want returned, null returns all, same as * in SQL
        String from = DatabaseContract.Veggies.TABLE_NAME;
        String where_col_name = null; //which column we are setting where conditions on, null means no conditions
        String [] where_col_conditions = null; //the conditions for the previously chosen columns
        String sortOrder = DatabaseContract.Veggies.COL_CALS + " DESC"; //sorts the calories column in descending order, if no DESC keyword, sorts in ascending on the column chosen, if null, no sorting done

        Cursor cursor = readable_db.query(
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
            String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Veggies.COL_COLOR));
            int calories = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Veggies.COL_CALS));

            sb.append(name);
            sb.append(" , ");
            sb.append(color);
            sb.append(" , ");
            sb.append(calories);

            items.add(sb.toString());
            sb.setLength(0); //clear the string builder
        }

        //put the info into the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }


    /*
    Have your student come up with the functions for the remaining two tables to display all the contents using the same list!
    Solution below:
     */

    public void getAllCandy(View view){
        SQLiteDatabase readable_db = dbManager.getReadableDatabase();

        String[] select_cols = null; //which columns we want returned, null returns all, same as * in SQL
        String from = DatabaseContract.Candyland.TABLE_NAME;
        String where_col_name = null; //which column we are setting where conditions on, null means no conditions
        String [] where_col_conditions = null; //the conditions for the previously chosen columns
        String sortOrder = DatabaseContract.Candyland.COL_CALS + " DESC"; //sorts the calories column in descending order, if no DESC keyword, sorts in ascending on the column chosen, if null, no sorting done

        Cursor cursor = readable_db.query(
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

        //put the info into the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }


    public void getAllKids(View view){
        SQLiteDatabase readable_db = dbManager.getReadableDatabase();

        String[] select_cols = null; //which columns we want returned, null returns all, same as * in SQL
        String from = DatabaseContract.Kiddo.TABLE_NAME;
        String where_col_name = null; //which column we are setting where conditions on, null means no conditions
        String [] where_col_conditions = null; //the conditions for the previously chosen columns
        /*
        Alternatively,
         */

        String sortOrder = DatabaseContract.Kiddo.COL_AGE + " DESC"; //sorts the age column in descending order, if no DESC keyword, sorts in ascending on the column chosen, if null, no sorting done

        Cursor cursor = readable_db.query(
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
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Kiddo.COL_NAME));
            String candy = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Kiddo.COL_FAV_CANDY));
            String veg = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Kiddo.COL_FAV_VEG));
            int age = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Kiddo.COL_AGE));

            sb.append(name);
            sb.append(" , ");
            sb.append(age);
            sb.append(" , ");
            sb.append(veg);
            sb.append(" , ");
            sb.append(candy);

            items.add(sb.toString());
            sb.setLength(0); //clear the string builder
        }

        //put the info into the list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
    }

}
