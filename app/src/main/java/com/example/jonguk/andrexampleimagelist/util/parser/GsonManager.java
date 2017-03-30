package com.example.jonguk.andrexampleimagelist.util.parser;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class GsonManager {
    private static Gson sGson;
    @NonNull
    public static Gson getGson() {
        if (sGson == null) {
            synchronized (GsonManager.class) {
                if (sGson == null) {
//                    sGson = new Gson();
                    sGson = new GsonBuilder().setPrettyPrinting().create();
                }
            }
        }
        return sGson;
    }
}
