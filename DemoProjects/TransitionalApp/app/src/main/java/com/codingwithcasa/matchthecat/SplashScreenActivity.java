package com.codingwithcasa.matchthecat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;

import com.codingwithcasa.matchthecat.Utils.CCCache;
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
 */
public class SplashScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        CCCache.addItem("ApplicationContext",getApplicationContext());
        HandlerThread ht = new HandlerThread("AndroidFileLogger.Horse");
        ht.start();
        FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                .date(new Date())
                .dateFormat(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS", Locale.US))
                .build();

        Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LobbyActivity.class));
                finish();
            }
        }, 2500);
    }
}
