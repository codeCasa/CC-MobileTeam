package com.codingwithcasa.matchthecard.Utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Utils {

    public static int getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int)displayMetrics.ydpi;
    }

    public static int getScreenWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int)displayMetrics.xdpi;
    }

    public static Context getApplicationContext(){
        return (Context)CCCache.getItem("ApplicationContext");
    }
}
