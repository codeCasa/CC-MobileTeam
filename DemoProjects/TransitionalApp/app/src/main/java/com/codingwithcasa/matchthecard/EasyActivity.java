package com.codingwithcasa.matchthecard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.codingwithcasa.matchthecard.CCLib.ProportionalImageView;
import com.codingwithcasa.matchthecard.Utils.CCCache;
import com.codingwithcasa.matchthecard.Utils.Utils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EasyActivity extends Activity implements View.OnClickListener{

    private Button mReady;
    private GridLayout mCardGrid;
    private Timer mCountDownTimer;
    private int mReadyTime = 30;
    private EasyActivity mInstance; //So we can handle UI based events
    private final ProportionalImageView[] mCardBacks = new ProportionalImageView[9];
    private final ProportionalImageView[] mCardFronts = new ProportionalImageView[9];
    private final int[] mCatCards = new int[]{
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.cat3,
            R.drawable.cat4,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);
        mReady = findViewById(R.id.ReadyBtn);
        mReady.setOnClickListener(this);
        mCardGrid = findViewById(R.id.CardGrid);
        mCountDownTimer = new Timer();

        populateGridView();
        mInstance = this;
    }

    private void populateGridView(){
        for(int i=0; i<9; i++){
            ProportionalImageView image = new ProportionalImageView(this);
            image.setImageResource(R.drawable.cardback);
            mCardGrid.addView(image);
            image.setOnClickListener(this);
            mCardBacks[i] = image;
        }
    }

    private void resetCards(){
        mCardGrid.removeAllViews();
    for(int i=0; i<mCardBacks.length; i++){
            mCardGrid.addView(mCardBacks[i]);
        }
    }

    private void flipCardsToFront(){
        mCardGrid.removeAllViews();
        Random rand = new Random();

        for(int i=0; i<9; i++){
            int catCard = rand.nextInt(mCatCards.length);
            catCard = catCard >= 0 ? catCard : 0;
            ProportionalImageView image = new ProportionalImageView(this);
            image.setImageResource(mCatCards[catCard]);
            mCardGrid.addView(image);
            mCardFronts[i] = image;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ReadyBtn: //The Ready/Reset button was clicked
                if(mReady.getText().toString().equals(getString(R.string.ready))) { //If the button reads 'Ready!'
                    mReady.setText("" + mReadyTime);
                    flipCardsToFront();
                    //start the timer
                    mCountDownTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            mReadyTime--;
                            if (mReadyTime <= 0) {
                                //if we hit 0 the set the button text to Reset
                                mInstance.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        resetCards();
                                        mReady.setText(R.string.reset);
                                    }
                                });
                                mCountDownTimer.cancel();
                            }else {
                                //Since the timer runs on a different thread we must access
                                //UI elements on the UI thread
                                mInstance.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mReady.setText("" + mReadyTime);
                                    }
                                });
                            }
                        }
                    }, 1000, 1000);//Wait 1 second before start and execute every 1 second thereafter
                }else if(mReady.getText().toString().equals(getString(R.string.reset))){ //Reset the UI
                    resetCards();
                    mReady.setText(R.string.ready);
                    mReadyTime = 30;
                    mCountDownTimer = new Timer();
                }
                break;
            default: // hit a card back
                if(mReady.getText().toString().equals(getString(R.string.ready))) {
                    //User hasn't started the game yet so alert them
                    Toast.makeText(this, R.string.hit_ready, Toast.LENGTH_SHORT).show();
                }
                if(mReady.getText().toString().equals(getString(R.string.reset))) {
                    //now we can handle the click logic
                }
                break;
        }
    }
}
