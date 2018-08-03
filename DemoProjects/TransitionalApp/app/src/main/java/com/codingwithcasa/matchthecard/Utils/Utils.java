package com.codingwithcasa.matchthecard.Utils;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class Utils {

    /**
     * Get the application context from the cache
     * @return
     */
    public static Context getApplicationContext(){
        return (Context)CCCache.getItem("ApplicationContext");
    }

    /**
     * Get all the children of a specified view
     * @param v - The parent view
     * @return - A list of the view's children
     */
    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}
