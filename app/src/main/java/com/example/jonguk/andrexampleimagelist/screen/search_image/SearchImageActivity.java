package com.example.jonguk.andrexampleimagelist.screen.search_image;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchImageActivity extends BaseActivity {

    private static final String TAG = "SearchImageActivity";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.appbar_search_input) EditText mSearchInputView;
    @BindView(R.id.appbar_clear) View mSearchInputClearView;
    @BindView(R.id.progress_layout) View mProgressLayout;

    private ImageListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ImageListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        RxView.clicks(mSearchInputClearView)
                .takeUntil(destroySignal())
                .filter(v -> mSearchInputView != null)
                .subscribe(v -> mSearchInputView.setText(""), err -> {});

        /*RxTextView.editorActionEvents(mSearchInputView)
                .takeUntil(destroySignal())
                .filter(action -> action.actionId() == EditorInfo.IME_ACTION_SEARCH)
                .map(event -> event.view().getText().toString())
                .subscribe(this::requestImage, err ->
                        Log.w(TAG, "RxTextView.editorActionEvents - ime_action_search", err));*/

        RxTextView.afterTextChangeEvents(mSearchInputView)
                .takeUntil(destroySignal())
                .observeOn(Schedulers.io())
                .debounce(1, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .map(event -> {
                    Editable editable = event.editable();
                    return editable != null ? editable.toString() : "";
                })
                .filter(s -> !TextUtils.isEmpty(s))
                .flatMap(event -> RxTextView.editorActionEvents(mSearchInputView)
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(action -> action.actionId() == EditorInfo.IME_ACTION_SEARCH)
                        .map(action -> action.view().getText().toString()).filter(q -> !TextUtils.isEmpty(q)))
                .subscribe(this::requestImage, err ->
                        Log.w(TAG, "RxTextView.afterTextChangeEvents - searchInputView", err));
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
                .doOnCompleted(() -> mProgressLayout.setVisibility(View.GONE))
                .doOnError(err -> mProgressLayout.setVisibility(View.GONE))
                .subscribe(mAdapter::addItems, err -> Log.w(TAG, "imageRequest", err));
    }
}
