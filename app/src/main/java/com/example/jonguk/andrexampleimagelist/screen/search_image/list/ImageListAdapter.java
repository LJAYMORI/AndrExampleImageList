package com.example.jonguk.andrexampleimagelist.screen.search_image.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jonguk.andrexampleimagelist.R;
import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageJson;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jonguk on 2017. 3. 30..
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private final List<SearchImageJson> mItems = new LinkedList<>();

    public void initItems(List<SearchImageJson> list) {
        mItems.clear();
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void addItems(List<SearchImageJson> list) {
        mItems.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
