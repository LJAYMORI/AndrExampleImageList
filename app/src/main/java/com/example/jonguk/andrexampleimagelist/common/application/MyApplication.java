package com.example.jonguk.andrexampleimagelist.common.application;

import android.app.Application;
import android.content.Context;

import com.example.jonguk.andrexampleimagelist.util.image_loader.ImageLoader;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }

    private void initImageLoader() {
        Context context = getApplicationContext();

        ImageLoader.initialize(context);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
