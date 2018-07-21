package com.codingwithcasa.matchthecard;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.codingwithcasa.matchthecard.Utils.CCCache;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * test on splash
 * test master
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        //Add the application context to the cache for later reference
        //This will allow us to do UI or Android specific api calls when not in an activity
        CCCache.addItem("ApplicationContext",getApplicationContext());
        HandlerThread ht = new HandlerThread("AndroidFileLogger.Horse");
        ht.start();
        //Set up a third party logging mechanism
        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .date(new Date())
                .dateFormat(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.US))
                .build();

        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
        //Get some the current device's information and log it
        //This can be useful if a specific OS/Brand/Model experience an issue
        //that other devices are not
        String model = Build.MODEL;
        if (!model.startsWith(Build.MANUFACTURER))
            model = Build.MANUFACTURER + " " + model;
        Logger.i("Android version: " +  Build.VERSION.SDK_INT);
        Logger.i("Device: " + model);
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo (this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
        }
        Logger.i("App version: " + (info == null ? "(null)" : info.versionCode));

        //After about 2.5secs we can move to the main screen
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LobbyActivity.class));
                finish(); // removes the splash screen from the stack so user can't go back to it
            }
        }, 2500);
    }
}
