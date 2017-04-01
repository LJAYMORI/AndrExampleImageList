package com.example.jonguk.andrexampleimagelist.common.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import rx.Observable;
import rx.subjects.BehaviorSubject;


public class RxActivity extends AppCompatActivity {
    private static final String TAG = "RxActivity";

    public enum ActivityLifecycleEvent {
        CREATE(0), DESTROY(5),
        START(1), STOP(4),
        RESUME(2), PAUSE(3);

        public final int order;

        ActivityLifecycleEvent(int order) {
            this.order = order;
        }
    }

    private ActivityLifecycleEvent mLastLifecycleEvent;
    private BehaviorSubject<ActivityLifecycleEvent> mLifecycleSubject;

    /**
     * Gets {@link rx.Observable} represents the Activity lifecycle and it will end on after
     * {@link ActivityLifecycleEvent#DESTROY}
     *
     * @return rx.Observable
     */
    protected Observable<ActivityLifecycleEvent> getLifecycleObservable() {
        if (mLifecycleSubject == null) {
            if (mLastLifecycleEvent == null) {
                mLifecycleSubject = BehaviorSubject.create(); // no default status
            } else {
                mLifecycleSubject = BehaviorSubject.create(mLastLifecycleEvent); // with default status
                mLastLifecycleEvent = null;
            }
        }
        return mLifecycleSubject;
    }

    /**
     * Gets {@link rx.Observable} represents the Fragment lifecycle and it will end at the given endEvent or on after
     * {@link ActivityLifecycleEvent#DESTROY}
     * <p/>
     * <b>usage:</b>
     * <pre class="prettyprint">
     * // Subscribes the observable until DESTROY
     * someObservable
     * .takeUntil(getLifecycleSignal(ActivityLifecycleEvent.DESTROY))
     * .subscribe(System.out::println);
     * </pre>
     *
     * @param filter terminal event
     * @return rx.Observable
     */
    protected Observable<ActivityLifecycleEvent> getLifecycleSignal(ActivityLifecycleEvent filter) {
        return getLifecycleObservable().filter(event -> event.order >= filter.order).take(1);
    }

    protected Observable<ActivityLifecycleEvent> destroySignal() {
        return getLifecycleSignal(ActivityLifecycleEvent.DESTROY);
    }

    /**
     * To enable Lifecycle logging:
     */
    private static final boolean LOG_LIFECYCLE = false;

    public RxActivity() {
        super();
        if (LOG_LIFECYCLE) {
            getLifecycleObservable().subscribe(event -> {
                Log.d(TAG, " -> " + event);
            });
        }
    }

    private void setLifecycleEvent(ActivityLifecycleEvent event) {
        if (mLifecycleSubject == null) {
            mLastLifecycleEvent = event;
        } else {
            mLifecycleSubject.onNext(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // First activity creation means that it will be in foreground shortly.
        // Without this, all the occurrence of ShowFragmentEvent in onCreate() will be ignored.
        setLifecycleEvent(ActivityLifecycleEvent.CREATE);
    }

    @Override
    protected void onStart() {
        setLifecycleEvent(ActivityLifecycleEvent.START);
        super.onStart();
    }

    @Override
    protected void onStop() {
        setLifecycleEvent(ActivityLifecycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLifecycleEvent(ActivityLifecycleEvent.RESUME);
    }

    @Override
    protected void onPause() {
        setLifecycleEvent(ActivityLifecycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        setLifecycleEvent(ActivityLifecycleEvent.DESTROY);
        try {
            super.onDestroy();
        } catch (IllegalStateException e) {
            Log.e(TAG, "error at onDestroy: " + this + ", e:" + e);
        }
        if (mLifecycleSubject != null) {
            mLifecycleSubject.onCompleted();
            mLifecycleSubject = null;
        }
    }
}
