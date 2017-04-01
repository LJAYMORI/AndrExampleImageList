package com.example.jonguk.andrexampleimagelist.screen.search_image;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.jonguk.andrexampleimagelist.common.activity.RxActivity;
import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageJson;
import com.example.jonguk.andrexampleimagelist.util.thread.ThreadHelper;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by Jonguk on 2017. 4. 1..
 */

class SearchImageRequestHelper {
    private static final String TAG = "ImageRequestHelper";

    private final BehaviorSubject<Boolean> mInRequestSubject = BehaviorSubject.create(false);
    private final BehaviorSubject<Boolean> mNoItemsSubject = BehaviorSubject.create(false);

    private final WeakReference<Context> mContextRef;
    private final Observable<RxActivity.ActivityLifecycleEvent> mDestroySignal;

    private int mPageNo;
    private String mQuery;

    SearchImageRequestHelper(Context context,
                             Observable<RxActivity.ActivityLifecycleEvent> destroySignal) {
        mContextRef = new WeakReference<>(context);
        mDestroySignal = destroySignal;
    }

    String getQuery() {
        return mQuery;
    }

    int getPageNo() {
        return mPageNo;
    }

    void setQueryAndPageNo(String query, int pageno) {
        mQuery = query;
        mPageNo = pageno;
    }

    void firstPageRequest(@NonNull String query,
                          @NonNull Action0 preCallback,
                          @NonNull Action0 completeCallback,
                          @NonNull Action1<Throwable> errorCallback,
                          @NonNull Action1<List<SearchImageJson>> resultCallback) {

        request(query, 1).takeUntil(mDestroySignal)
                .doOnSubscribe(preCallback)
                .subscribeOn(ThreadHelper.mainThread())
                .observeOn(ThreadHelper.mainThread())
                .doOnCompleted(completeCallback)
                .doOnError(errorCallback)
                .subscribe(resultCallback, err -> Log.w(TAG, "firstPageRequest", err));
    }

    void nextPageRequest(@NonNull Action1<Throwable> errorCallback,
                         @NonNull Action1<List<SearchImageJson>> resultCallback) {
        request(mQuery, mPageNo + 1).takeUntil(mDestroySignal)
                .subscribeOn(ThreadHelper.mainThread())
                .observeOn(ThreadHelper.mainThread())
                .doOnError(errorCallback)
                .subscribe(resultCallback, err ->
                        Log.w(TAG, "nextPageRequest - query :" + mQuery + ", pageNo:" + mPageNo, err));
    }

    Observable<List<SearchImageJson>> retry() {
        return request(mQuery, mPageNo);
    }

    private Observable<List<SearchImageJson>> request(String query, int pageNo) {
        return mInRequestSubject.take(1).flatMap(isRequest -> {
            Context context = mContextRef.get();
            if (isRequest || context == null || TextUtils.isEmpty(query) ||
                    (query.equals(mQuery) && pageNo < mPageNo)) {
                return Observable.empty();

            } else {
                return SearchImageRequest.images(context, query, pageNo)
                        .map(response -> response.channel.item)
                        .doOnSubscribe(() -> {
                            mInRequestSubject.onNext(true);
                        })
                        .doOnError(err -> {
                            mNoItemsSubject.onNext(true);
                            mInRequestSubject.onNext(false);
                        })
                        .doOnNext(list -> {
                            setQueryAndPageNo(query, pageNo);
                            mNoItemsSubject.onNext(list == null || list.size() < 1);
                            mInRequestSubject.onNext(false);
                        });
            }
        });
    }

    Observable<Boolean> noItemsObservable() {
        return mNoItemsSubject;
    }

}
