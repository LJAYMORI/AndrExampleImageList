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
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ImageListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mCompositeSubscription.add(RxView.clicks(mSearchInputClearView)
                .takeUntil(destroySignal())
                .filter(v -> mSearchInputView != null)
                .subscribe(v -> mSearchInputView.setText(""), err ->
                        Log.w(TAG, "RxView.clicks - searchInputClearView", err)));

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
                .observeOn(Schedulers.io())
                .filter(query -> !TextUtils.isEmpty(query))
                .distinctUntilChanged()
                .subscribe(this::requestImage, err ->
                        Log.w(TAG, "change, editorAction", err)));
    }

    @Override
    protected void onDestroy() {
        mCompositeSubscription.unsubscribe();
        super.onDestroy();
    }

    private void requestImage(String query) {
        ImageRequest.images(getResources(), query)
                .takeUntil(destroySignal())
                .doOnSubscribe(() -> {
                    mProgressLayout.setVisibility(View.VISIBLE);
                    mAdapter.clear();
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(response -> response.channel.item)
                .distinctUntilChanged()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(list -> {
                    if (list == null || list.size() < 1) {
                        showSnackBar(mLayout, getStringWithoutException(R.string.no_item));
                    }
                })
                .doOnCompleted(() -> mProgressLayout.setVisibility(View.GONE))
                .doOnError(err -> {
                    mProgressLayout.setVisibility(View.GONE);
                    showSnackBar(mLayout, getStringWithoutException(R.string.search_fail),
                            getStringWithoutException(R.string.retry), v -> requestImage(query));
                })
                .subscribe(mAdapter::addItems, err -> Log.w(TAG, "imageRequest", err));
    }
}
