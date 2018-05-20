package com.yorel.watchmego;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WatchMe extends AppCompatActivity implements SensorEventListener{
    private SensorManager mySensorManager;
    private Sensor myAccelerometer;
    private final float NOISE = 2.0f;
    private boolean firstRun;// = false;
    private float lastX, lastY,lastZ;
    public static boolean isFlat = false;
    public static float deltaX = 0.0f, deltaY = 0.0f, deltaZ = 0.0f;
    PudgeyWalk pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pw = new PudgeyWalk(this);
        setContentView(pw);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        firstRun = false;
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mySensorManager != null)
            mySensorManager.unregisterListener(this);
        if(pw != null)
            pw.setRun(false);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mySensorManager!=null && myAccelerometer!= null)
            mySensorManager.registerListener(this,myAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        if(pw != null)
            pw.setRun(true);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];;
        if(!firstRun){
            lastX = x;
            lastY = y;
            lastZ = z;
            firstRun = true;
        }else{
            deltaX = Math.abs(lastX - x);
            deltaY = Math.abs(lastY - y);
            deltaZ = Math.abs(lastZ - z);
            if (deltaX < NOISE) deltaX = (float)0.0;
            if (deltaY < NOISE) deltaY = (float)0.0;
            if (deltaZ < NOISE) deltaZ = (float)0.0;
            lastX = x;
            lastY = y;
            lastZ = z;
        }
        float normalizedGravity =(float) Math.sqrt(x * x + y * y + z * z);
        z = (z / normalizedGravity);
        int inclination = (int) Math.round(Math.toDegrees(Math.acos(z)));
        //device is 'flat-ish' to the ground
        if (inclination < 25 || inclination > 155)
            isFlat = true;
        else
            isFlat = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
