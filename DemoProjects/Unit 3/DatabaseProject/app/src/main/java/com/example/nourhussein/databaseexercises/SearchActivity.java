package com.example.nourhussein.databaseexercises;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText selectClause, fromClause, whereClause;
    private Button submitQueryButton;
    private ListView resulstList;
    private DBManager mDatabasee;
    private SQLiteDatabase myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //get instance of our database and of a readable reference
        mDatabasee = DBManager.getInstance(this);
        myDB = mDatabasee.getReadableDatabase();

        //fetch all the necessary layouts for further use
        selectClause = findViewById(R.id.editText_SELECT_clause);
        fromClause = findViewById(R.id.editText_FROM_clause);
        whereClause = findViewById(R.id.editText_WHERE_clause);
        submitQueryButton = findViewById(R.id.submit_query);
        resulstList = findViewById(R.id.search_result_list);

        submitQueryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runQuery(view);
            }
        });
    }


    private void runQuery(View v) {
        StringBuilder query = new StringBuilder();
        String select = selectClause.getText().toString();
        String from = fromClause.getText().toString();
        String where = whereClause.getText().toString();

        query.append("SELECT ");
        query.append(select);
        query.append(" FROM ");
        query.append(from);
        if (!where.isEmpty()) {
            query.append(" WHERE ");
            query.append(where);
        }

        String queryString = query.toString();

        Cursor cursor = myDB.rawQuery(
                queryString,
                null
        );

        List<String> results = new ArrayList<>();
        String [] resultColumnNames = cursor.getColumnNames();

        String titleRow = "";
        for (int i = 0; i < resultColumnNames.length; i++) {
            if (i != 0) {
                titleRow += " , ";
            }
            titleRow += resultColumnNames[i];
        }
        results.add(titleRow);

        StringBuilder row;
        while(cursor.moveToNext()) {
            row = new StringBuilder();
            for (int i = 0; i < resultColumnNames.length; i++) {
                if (i != 0) {
                    row.append(" , ");
                }
                row.append(cursor.getString(cursor.getColumnIndexOrThrow(resultColumnNames[i])));
            }
            results.add(row.toString());
        }

        //display results
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, results);
        resulstList.setAdapter(adapter);

        //clear edit texts to prep for new queries
        selectClause.setText("");
        fromClause.setText("");
        whereClause.setText("");
    }
}
