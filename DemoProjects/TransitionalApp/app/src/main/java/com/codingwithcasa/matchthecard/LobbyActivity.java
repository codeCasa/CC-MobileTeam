package com.codingwithcasa.matchthecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * test
 * test 2
 */
public class LobbyActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lobby);
    }
    public void easyActivity(View view){
        Intent intent = new Intent(this, EasyActivity.class);
        startActivity(intent);
    }
}
