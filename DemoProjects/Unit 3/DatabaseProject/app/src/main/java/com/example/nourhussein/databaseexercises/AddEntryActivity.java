package com.example.nourhussein.databaseexercises;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class AddEntryActivity extends AppCompatActivity {

    private Button submitButton;
    private TextView numberTextView, titleTextView, typeOrColorTextView;
    private EditText nameEditText, numberEditText, typeOrColorEditText;
    private DBManager mDatabase;
    private String tableName;
    private Spinner vegSpinner, candySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        Intent intent = getIntent();
        tableName = intent.getStringExtra("type");
        mDatabase = DBManager.getInstance(this);

        submitButton = findViewById(R.id.submit_add);

        //fetch all the views
        numberTextView = findViewById(R.id.add_numeric);
        titleTextView = findViewById(R.id.title_add);
        typeOrColorTextView = findViewById(R.id.type_color_text);
        nameEditText = findViewById(R.id.editText_name);
        numberEditText = findViewById(R.id.editText_numeric);
        typeOrColorEditText = findViewById(R.id.editText_type_color);
        vegSpinner = findViewById(R.id.veg_spinner);
        candySpinner = findViewById(R.id.candy_spinner);

        switch (tableName) {
            case "child":
                titleTextView.setText("Add a Child");
                numberTextView.setText("Age");
                typeOrColorTextView.setVisibility(View.INVISIBLE);
                typeOrColorEditText.setVisibility(View.INVISIBLE);
                List<String> allTheVeggies = mDatabase.pollVeggies();
                List<String> allTheCandy = mDatabase.pollCandies();

                ArrayAdapter<String> veg_adapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, allTheVeggies);

                veg_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vegSpinner.setAdapter(veg_adapter);

                ArrayAdapter<String> candy_adapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, allTheCandy);

                veg_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
               candySpinner.setAdapter(candy_adapter);
               vegSpinner.setVisibility(View.VISIBLE);
               candySpinner.setVisibility(View.VISIBLE);
                break;

            case "veg":
                titleTextView.setText("Add a Vegetable");
                numberTextView.setText("Calories");
                typeOrColorTextView.setText("Color");
                break;

            case "candy":
                titleTextView.setText("Add a Candy");
                numberTextView.setText("Calories");
                typeOrColorTextView.setText("Type");
                break;

            default:
                Log.d("ERR", "Incorrect Switch value detected");
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                addEntry(v);
            }
        });
    }

    public void addEntry(View view) {
        String name = nameEditText.getText().toString();
        int number = Integer.parseInt(numberEditText.getText().toString());
        switch (tableName) {
            case "child":
                String veg = vegSpinner.getSelectedItem().toString();
                String candy = candySpinner.getSelectedItem().toString();
                mDatabase.insertChild(name, number, candy, veg);
                break;

            case "veg":
                String color = typeOrColorEditText.getText().toString();
                mDatabase.insertVeggie(name, number, color);
                break;

            case "candy":
                String type = typeOrColorEditText.getText().toString();
                mDatabase.insertCandy(name, type, number);
        }
        finish();
    }
}
