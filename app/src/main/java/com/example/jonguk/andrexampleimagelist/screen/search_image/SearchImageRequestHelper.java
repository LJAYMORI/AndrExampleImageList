package com.example.jonguk.andrexampleimagelist.screen.search_image;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.jonguk.andrexampleimagelist.common.activity.RxActivity;
import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageJson;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.ImageListAdapter;
import com.example.jonguk.andrexampleimagelist.util.thread.ThreadHelper;

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

    private final Context mContext;
    private final Observable<RxActivity.ActivityLifecycleEvent> mDestroySignal;
    private final ImageListAdapter mAdapter;

    private int mPageNo;
    private String mQuery;

    SearchImageRequestHelper(Context context, ImageListAdapter adapter,
                             Observable<RxActivity.ActivityLifecycleEvent> destroySignal) {
        mContext = context;
        mAdapter = adapter;
        mDestroySignal = destroySignal;
    }

    void firstPageRequest(String query,
                          Action0 preCallback,
                          Action0 completeCallback,
                          Action1<Throwable> errorCallback) {

        request(query, 1).takeUntil(mDestroySignal)
                .doOnSubscribe(preCallback)
                .subscribeOn(ThreadHelper.mainThread())
                .observeOn(ThreadHelper.mainThread())
                .doOnCompleted(completeCallback)
                .doOnError(errorCallback)
                .subscribe(mAdapter::initItems, err -> Log.w(TAG, "firstPageRequest", err));
    }

    void nextPageRequest(Action1<Throwable> errorCallback) {
        request(mQuery, mPageNo + 1).takeUntil(mDestroySignal)
                .subscribeOn(ThreadHelper.mainThread())
                .observeOn(ThreadHelper.mainThread())
                .doOnError(errorCallback)
                .subscribe(mAdapter::addItems, err ->
                        Log.w(TAG, "nextPageRequest - query :" + mQuery + ", pageNo:" + mPageNo,
                                err));
    }

    Observable<List<SearchImageJson>> retry() {
        return request(mQuery, mPageNo);
    }

    private Observable<List<SearchImageJson>> request(String query, int pageNo) {
        return mInRequestSubject.take(1).flatMap(isRequest -> {
            if (isRequest) {
                return Observable.empty();
            } else {
                return TextUtils.isEmpty(query) || (query.equals(mQuery) && pageNo == mPageNo) ?
                        Observable.empty() : SearchImageRequest.images(mContext, query, pageNo)
                        .map(response -> response.channel.item)
                        .doOnSubscribe(() -> {
                            mNoItemsSubject.onNext(false);
                            mInRequestSubject.onNext(true);
                        })
                        .doOnError(err -> {
                            mNoItemsSubject.onNext(true);
                            mInRequestSubject.onNext(false);
                        })
                        .doOnNext(list -> {
                            mNoItemsSubject.onNext(list == null || list.size() < 1);
                            mInRequestSubject.onNext(false);
                        })
                        .doOnCompleted(() -> {
                            mQuery = query;
                            mPageNo = pageNo;
                        });
            }
        });
    }

    Observable<Boolean> noItemsObservable() {
        return mNoItemsSubject;
    }

}
