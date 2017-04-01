package com.example.jonguk.andrexampleimagelist.util.thread;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jonguk on 2017. 4. 1..
 */

public class ThreadHelper {

    public static Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }

    public static Scheduler io() {
        return Schedulers.io();
    }

}
