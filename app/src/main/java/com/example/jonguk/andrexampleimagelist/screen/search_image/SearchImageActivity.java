package com.example.jonguk.andrexampleimagelist.screen.search_image;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.jonguk.andrexampleimagelist.R;
import com.example.jonguk.andrexampleimagelist.common.activity.BaseActivity;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.ImageListAdapter;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;

public class SearchImageActivity extends BaseActivity {

    private static final String TAG = "SearchImageActivity";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    private ImageListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ImageListAdapter();
        mRecyclerView.setAdapter(mAdapter);

        requestImage("google");
    }

    private void requestImage(String query) {
        ImageRequest.images(getResources(), query)
                .takeUntil(destroySignal())
                .distinctUntilChanged()
                .map(response -> response.channel.item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> mAdapter.addItems(items), err -> {
                    Log.w(TAG, "imageRequest", err);
                });
    }
}
