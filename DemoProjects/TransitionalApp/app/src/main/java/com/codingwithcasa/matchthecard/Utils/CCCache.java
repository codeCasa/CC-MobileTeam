package com.codingwithcasa.matchthecard.Utils;

import com.orhanobut.logger.Logger;

import java.util.HashMap;

public class CCCache {
    private static HashMap<String,Object> CC_CACHE;

    /**
     * Gets an instance of the cache
     * @return The cache
     */
    public static HashMap<String, Object> getInstance() {
        if(CC_CACHE == null){
            synchronized (CCCache.class){
                CC_CACHE = new HashMap<>();
            }
        }
        return CC_CACHE;
    }

    /**
     * Add an item to the cache
     * @param name Name of the item
     * @param item The item to add
     */
    public static void addItem(String name, Object item){
        getInstance().put(name,item);
    }

    /**
     * Get an item from the cache
     * @param name The name of the item to retrieve
     * @return null if no item was found
     */
    public static Object getItem(String name){
        Object obj =getInstance().get(name);
        if(obj == null)
            Logger.w(name + " does not exist in the cache");
        return obj;
    }
}
