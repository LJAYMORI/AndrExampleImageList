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
import com.example.jonguk.andrexampleimagelist.util.thread.ThreadHelper;
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class SearchImageActivity extends BaseActivity {

    private static final String TAG = "SearchImageActivity";

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.appbar_search_input)
    EditText mSearchInputView;
    @BindView(R.id.appbar_clear)
    View mSearchInputClearView;
    @BindView(R.id.progress_layout)
    View mProgressLayout;

    private ImageListAdapter mAdapter;
    private SearchImageRequestHelper mSearchRequestHelper;
    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ImageListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mSearchRequestHelper = new SearchImageRequestHelper(this, mAdapter, destroySignal());

        // observe complete searching
        mSearchRequestHelper.noItemsObservable()
                .takeUntil(destroySignal())
                .filter(hasNoItems -> hasNoItems)
                .observeOn(ThreadHelper.mainThread())
                .subscribe(b -> showToast(getStringWithoutException(R.string.search_complete)),
                        err -> Log.w(TAG, "SearchRequestHelper.noItemsObservable", err));

        // observe scroll event
        mCompositeSubscription.add(RxRecyclerView.scrollEvents(mRecyclerView)
                .takeUntil(destroySignal())
                .map(RecyclerViewScrollEvent::dy)
                .filter(dy -> dy > 0)
                .map(dy -> layoutManager.findLastVisibleItemPosition() + 2)
                .distinctUntilChanged()
                .filter(position -> position > mAdapter.getItemCount())
                .subscribe(position -> mSearchRequestHelper.nextPageRequest(
                        () -> {},
                        () -> {},
                        () -> showSnackBar(mLayout, "scroll?")),
                        err -> Log.w(TAG, "RxRecyclerView.scrollEvents", err)));

        // observe searching keyword clear
        mCompositeSubscription.add(RxView.clicks(mSearchInputClearView)
                .takeUntil(destroySignal())
                .filter(v -> mSearchInputView != null)
                .subscribe(v -> mSearchInputView.setText(""), err ->
                        Log.w(TAG, "RxView.clicks - searchInputClearView", err)));

        // observe searching keyword changing and searching action
        Observable<String> textChangeObs = RxTextView.afterTextChangeEvents(mSearchInputView)
                .takeUntil(destroySignal())
                .debounce(1, TimeUnit.SECONDS)
                .filter(event -> event != null && event.editable() != null)
                .map(event -> event.editable().toString());

        Observable<String> editorActionObs = RxTextView.editorActionEvents(mSearchInputView)
                .takeUntil(destroySignal())
                .filter(action -> action.actionId() == EditorInfo.IME_ACTION_SEARCH)
                .map(event -> event.view().getText().toString());

        mCompositeSubscription.add(Observable.amb(textChangeObs, editorActionObs)
                .takeUntil(destroySignal())
                .observeOn(ThreadHelper.io())
                .filter(query -> !TextUtils.isEmpty(query))
                .doOnSubscribe(() -> mRecyclerView.scrollToPosition(0))
                .subscribe(query -> mSearchRequestHelper.firstPageRequest(query,
                        () -> mProgressLayout.setVisibility(View.VISIBLE),
                        () -> mProgressLayout.setVisibility(View.GONE),
                        () -> showSnackBar(mLayout, getStringWithoutException(R.string.search_fail),
                                getStringWithoutException(R.string.retry),
                                v -> mSearchRequestHelper.retry())), err -> {}));
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

}
