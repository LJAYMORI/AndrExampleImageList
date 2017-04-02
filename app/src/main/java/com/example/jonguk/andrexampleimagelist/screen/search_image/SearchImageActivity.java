package com.example.jonguk.andrexampleimagelist.screen.search_image;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.example.jonguk.andrexampleimagelist.R;
import com.example.jonguk.andrexampleimagelist.common.activity.BaseActivity;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.ImageListAdapter;
import com.example.jonguk.andrexampleimagelist.util.keyboard.KeyboardUtils;
import com.example.jonguk.andrexampleimagelist.util.network.HttpErrorHandler;
import com.example.jonguk.andrexampleimagelist.util.thread.ThreadHelper;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.parceler.Parcels;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

public class SearchImageActivity extends BaseActivity {

    private static final String TAG = "SearchImageActivity";

    private static final String KEY_LAYOUT_MANAGER = "layout_manager";
    private static final String KEY_ADAPTER_ITEMS = "adapter_items";
    private static final String KEY_SEARCH_QUERY = "search_query";
    private static final String KEY_SEARCH_PAGE_NO = "search_page_no";

    private static final int NEXT_PAGE_POSITION = 3;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.appbar_search_input)
    EditText mSearchInputView;
    @BindView(R.id.appbar_clear)
    View mSearchInputClearView;
    @BindView(R.id.progress_view)
    View mProgressLayout;

    private ImageListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private SearchImageRequestHelper mSearchRequestHelper;

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ImageListAdapter();
        mSearchRequestHelper = new SearchImageRequestHelper(this, destroySignal());

        boolean isDestroyed = savedInstanceState != null;
        BehaviorSubject<Boolean> destorySubject = BehaviorSubject.create(isDestroyed);
        if (isDestroyed) {
            mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER));
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter.initItems(Parcels.unwrap(savedInstanceState.getParcelable(KEY_ADAPTER_ITEMS)));
            mRecyclerView.setAdapter(mAdapter);
            mSearchRequestHelper.setQueryAndPageNo(savedInstanceState.getString(KEY_SEARCH_QUERY, ""),
                    savedInstanceState.getInt(KEY_SEARCH_PAGE_NO, 1));
        } else {
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }

        // observe complete searching
        mCompositeSubscription.add(mSearchRequestHelper.noItemsObservable()
                .takeUntil(destroySignal())
                .filter(hasNoItems -> hasNoItems)
                .observeOn(ThreadHelper.mainThread())
                .subscribe(b ->
                        showSnackBar(mLayout, getStringWithoutException(R.string.search_complete)),
                        err -> Log.w(TAG, "SearchRequestHelper.noItemsObservable", err)));

        // observe scroll event
        mCompositeSubscription.add(RxRecyclerView.scrollEvents(mRecyclerView)
                .takeUntil(destroySignal())
                .map(event -> event.dy())
                .filter(dy -> dy > 0)
                .map(dy -> mLayoutManager.findLastVisibleItemPosition() + NEXT_PAGE_POSITION)
                .distinctUntilChanged()
                .filter(position -> position > mAdapter.getItemCount())
                .subscribe(position -> mSearchRequestHelper.requestNextPage(
                        // handle error
                        new HttpErrorHandler((code, msg) ->
                                showSnackBar(mLayout, "code:" + code + ", message:" + msg), 409),

                        // handle result
                        list -> mAdapter.addItems(list)),

                        err -> Log.w(TAG, "RxRecyclerView.scrollEvents", err)));

        // observe clear query
        mCompositeSubscription.add(RxView.clicks(mSearchInputClearView)
                .takeUntil(destroySignal())
                .filter(v -> mSearchInputView != null)
                .subscribe(v -> mSearchInputView.setText(""), err ->
                        Log.w(TAG, "RxView.clicks - searchInputClearView", err)));

        // observe query changed event and search action
        Observable<String> editorActionObs = RxTextView.editorActionEvents(mSearchInputView)
                .takeUntil(destroySignal())
                .filter(action -> action.actionId() == EditorInfo.IME_ACTION_SEARCH)
                .map(event -> event.view().getText().toString())
                .flatMap(query -> {
                    destorySubject.onNext(false);
                    return Observable.just(query);
                })
                .filter(query -> !TextUtils.isEmpty(query));

        Observable<String> textChangeObs = RxTextView.afterTextChangeEvents(mSearchInputView)
                .takeUntil(destroySignal())
                .debounce(1L, TimeUnit.SECONDS)
                .filter(event -> event != null && event.editable() != null)
                .map(event -> event.editable().toString())
                .flatMap(query -> {
                    destorySubject.onNext(false);
                    return Observable.just(query);
                })
                .filter(query -> !TextUtils.isEmpty(query) && !query.equals(mSearchRequestHelper.getQuery()));

        mCompositeSubscription.add(Observable.merge(editorActionObs, textChangeObs)
                .takeUntil(destroySignal())
                .flatMap(query -> destorySubject.take(1).filter(b -> !b).flatMap(b -> Observable.just(query)))
                .subscribe(query -> mSearchRequestHelper.requestFirstPage(query,
                        // handle before search
                        () -> {
                            mAdapter.clear();
                            mProgressLayout.setVisibility(View.VISIBLE);
                        },

                        // handle complete search
                        () -> {
                            mProgressLayout.setVisibility(View.GONE);
                            KeyboardUtils.hide(mSearchInputView);
                        },

                        // handle error search
                        new HttpErrorHandler((code, msg) -> {
                            mProgressLayout.setVisibility(View.GONE);
                            showSnackBar(mLayout, "code:" + code + ", msg:" + msg);
                        }),

                        // handle result
                        list -> mAdapter.initItems(list)),

                        err -> Log.w(TAG, "changedText and search action", err)));
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LAYOUT_MANAGER, mLayoutManager.onSaveInstanceState());
        outState.putParcelable(KEY_ADAPTER_ITEMS, Parcels.wrap(mAdapter.getItems()));
        outState.putString(KEY_SEARCH_QUERY, mSearchRequestHelper.getQuery());
        outState.putInt(KEY_SEARCH_PAGE_NO, mSearchRequestHelper.getPageNo());
    }
}
