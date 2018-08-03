package com.codingwithcasa.matchthecard;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codingwithcasa.matchthecard.Utils.CCCache;

import java.util.Timer;
import java.util.TimerTask;

public class EasyActivity extends Activity implements View.OnClickListener{

    private Button mReady;
    private Timer mCountDownTimer;
    private int mReadyTime = 30;
    private EasyActivity mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);
        mReady = findViewById(R.id.ReadyBtn);
        mReady.setOnClickListener(this);
        mCountDownTimer = new Timer();
        mInstance = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ReadyBtn:
                if(mReady.getText().toString().equals(getString(R.string.ready))) {
                    mReady.setText("" + mReadyTime);
                    mCountDownTimer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            mReadyTime--;
                            if (mReadyTime <= 0) {
                                mInstance.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mReady.setText(R.string.reset);
                                    }
                                });
                                mCountDownTimer.cancel();
                            }else {
                                mInstance.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mReady.setText("" + mReadyTime);
                                    }
                                });
                            }
                        }
                    }, 1000, 1000);
                }else if(mReady.getText().toString().equals(getString(R.string.reset))){
                    mReady.setText(R.string.ready);
                    mReadyTime = 30;
                    mCountDownTimer = new Timer();
                }
                break;
        }
    }
}
