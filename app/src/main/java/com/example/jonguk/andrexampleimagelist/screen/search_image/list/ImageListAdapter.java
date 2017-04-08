package com.example.jonguk.andrexampleimagelist.screen.search_image.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.AbsSearchImageData;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.CompleteData;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.ImageData;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.LoadingData;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.viewholder.AbsViewHolder;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.viewholder.CompleteViewHolder;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.viewholder.ImageViewHolder;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.viewholder.LoadingViewHolder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jonguk on 2017. 3. 30..
 */

public class ImageListAdapter extends RecyclerView.Adapter<AbsViewHolder> {

    private final List<AbsSearchImageData> mItems = new LinkedList<>();

    public ImageListAdapter() {
        setHasStableIds(true);
    }

    public List<AbsSearchImageData> getItems() {
        return mItems;
    }

    public void initItems(@NonNull List<ImageData> list) {
            /*final ImageListDiffCallback diffCallback = new ImageListDiffCallback(mItems, list);
            final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);*/
        mItems.clear();
        mItems.addAll(list);
        addLoading();
        notifyDataSetChanged();
    }

    public void addItems(@NonNull List<? extends AbsSearchImageData> list) {
        if (list.size() < 1) {
            return;
        }
        mItems.addAll(list);
        addLoading();
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void addCompleteMessage() {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            AbsSearchImageData data = mItems.get(i);
            if (AbsSearchImageData.Type.COMPLETE.equals(data.getType())) {
                return;
            }
        }
        mItems.add(new CompleteData());
    }

    private void addLoading() {
        removeLoading();
        mItems.add(new LoadingData());
    }

    public void removeLoading() {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            AbsSearchImageData data = mItems.get(i);
            if (AbsSearchImageData.Type.LOADING.equals(data.getType())) {
                mItems.remove(i);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getTypeOrdinal();
    }

    @Override
    public AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbsSearchImageData.Type type = AbsSearchImageData.Type.values()[viewType];
        View v = LayoutInflater.from(parent.getContext()).inflate(type.getLayoutId(), parent, false);
        switch (type) {
            case ITEM:
                return new ImageViewHolder(v);
            case LOADING:
                return new LoadingViewHolder(v);
            case COMPLETE:
                return new CompleteViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(AbsViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
