package com.codingwithcasa.matchthecard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codingwithcasa.matchthecard.CCLib.ProportionalImageView;
import com.codingwithcasa.matchthecard.Utils.CCCache;
import com.codingwithcasa.matchthecard.Utils.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EasyActivity extends Activity implements View.OnClickListener{

    private Button mReady;
    private GridLayout mCardGrid;
    private Timer mCountDownTimer;
    private int mReadyTime = 30;
    private Dialog mDialog;
    private EasyActivity mInstance; //So we can handle UI based events
    private final ProportionalImageView[] mCardBacks = new ProportionalImageView[9];
    private final ProportionalImageView[] mCardFronts = new ProportionalImageView[9];
    private final int[] mCatCards = new int[]{
            R.drawable.cat1,
            R.drawable.cat2,
            R.drawable.cat3,
            R.drawable.cat4,
    };
    private final int[] mOrder = new int[9];

    private double timeElapsed = 0; //tracks the amount of time it takes the user to finish the game
    private int movesMade = 0; // tracks the number of guesses the user makes before winning the game
    private long startTime, endTime; //variables to measure elapsed time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);
        mReady = findViewById(R.id.ReadyBtn);
        mReady.setOnClickListener(this);
        mCardGrid = findViewById(R.id.CardGrid);
        mCountDownTimer = new Timer();

        populateGridView();
        setupCardPicker();
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


    /**
     * This method does a lot of the heavy lifting. It sets up the card picker dialog.
     * It also places the down a card when a cell is selected.
     */
    private void setupCardPicker(){
        //Build the card picker dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.card_picker, null));
        mDialog = builder.create();
        mDialog.create();

        // We need to get the options from the card picker dialog
        View cardPickerHolder = mDialog.findViewById(R.id.CardImageLinearLayout);
        ArrayList<View> imageChoices = Utils.getAllChildren(cardPickerHolder);

        //This onclick listener is shared for each card cell, and when an item is clicked
        //we put the picked card in the appropriate cell and check the order
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProportionalImageView iv = (ProportionalImageView)v;//the clicked imaged
                ProportionalImageView clicked = (ProportionalImageView)CCCache.getItem("ClickedCard"); // retreieve the clicked cell from the cache
                if(clicked == null) return;
                //places the card down on the grid
                switch(iv.getId()){
                    case R.id.cat1:
                        clicked.setImageResource(R.drawable.cat1);
                        clicked.setImageId(R.drawable.cat1);
                        break;
                    case R.id.cat2:
                        clicked.setImageResource(R.drawable.cat2);
                        clicked.setImageId(R.drawable.cat2);
                        break;
                    case R.id.cat3:
                        clicked.setImageResource(R.drawable.cat3);
                        clicked.setImageId(R.drawable.cat3);
                        break;
                    case R.id.cat4:
                        clicked.setImageResource(R.drawable.cat4);
                        clicked.setImageId(R.drawable.cat4);
                        break;
                }

                mDialog.dismiss(); //close the dialog

                for (int i = 0; i< mCardFronts.length; i++) {
                    /*
                        Because the clicked cell retrieved from the cache is a shallow copy
                        We must find the original instance and alter it's data, since we use
                        the original instances to confirm the proper order
                        Another way would be to completely override the original instance
                        with the clicked one.
                     */
                    if(mCardFronts[i].getOriginalId() == clicked.getImageId()){
                        mCardFronts[i].setImageId(clicked.getImageId());
                    }
                }

                //check the order
                if(areCardsInOrder()){
                    Toast.makeText(mInstance, R.string.win, Toast.LENGTH_SHORT).show();
                    endTime = SystemClock.elapsedRealtime();
                    timeElapsed = (endTime - startTime)/1000;
                    //TODO: check if high score in terms of moves or time and add to database

                }else{
                    Toast.makeText(mInstance, R.string.keep_going, Toast.LENGTH_SHORT).show();
                }
            }
        };

        //add the above listener to the dialog option
        for (View v: imageChoices) {
            if(v instanceof ProportionalImageView)
                v.setOnClickListener(listener);
        }
    }


    /**
     * This method flips the cards over to the front side to show the cats
     */
    private void flipCardsToFront(){
        //remove the old cards
        mCardGrid.removeAllViews();
        Random rand = new Random();
        for(int i=0; i<9; i++){
            //pick a random card
            int catCard = rand.nextInt(mCatCards.length);
            catCard = catCard >= 0 ? catCard : 0;
            ProportionalImageView image = new ProportionalImageView(this); // A custom image view to fill the card cell
            image.setImageResource(mCatCards[catCard]);
            image.setOriginalId(mCatCards[catCard]);// need to know the original card image id
            mOrder[i]=mCatCards[catCard];//need to remember the orignal order
            mCardGrid.addView(image);// add it to the grid
            mCardFronts[i] = image;
        }
    }

    /**
     * This method determines if the cards were placed back in the correct order
     * @return true if the cards are in the correct order
     */
    private boolean areCardsInOrder(){
        for (int i=0; i<mCardFronts.length; i++) {
            if(mCardFronts[i].getImageId() != mOrder[i])
                return false;
        }

        return true;
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
                                startTime = SystemClock.elapsedRealtime();
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
                    movesMade = 0;
                }
                break;
            default: // hit a card back
                if(mReady.getText().toString().equals(getString(R.string.ready))) {
                    //User hasn't started the game yet so alert them
                    Toast.makeText(this, R.string.hit_ready, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mReady.getText().toString().equals(getString(R.string.reset))) {
                    //now we can handle the click logic

                    //add the clicked card to the cache so we can reference it later
                    CCCache.addItem("ClickedCard", v);
                    //show the card picker dialog
                    mDialog.show();

                    //increment the number of moves made by the user so far by 1
                    movesMade++;
                    Log.d("clicks now at", ""+movesMade);
                }
                break;
        }
    }
}
