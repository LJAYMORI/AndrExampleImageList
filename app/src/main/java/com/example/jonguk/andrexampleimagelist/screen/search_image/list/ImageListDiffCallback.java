package com.example.jonguk.andrexampleimagelist.screen.search_image.list;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageJson;

import java.util.List;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

public class ImageListDiffCallback extends DiffUtil.Callback {
    private static final String TAG = "ImageListDiffCallback";

    private final List<SearchImageJson> oldImageList;
    private final List<SearchImageJson> newImageList;

    public ImageListDiffCallback(List<SearchImageJson> oldImageList, List<SearchImageJson> newImageList) {
        this.oldImageList = oldImageList;
        this.newImageList = newImageList;
    }

    @Override
    public int getOldListSize() {
        return oldImageList.size();
    }

    @Override
    public int getNewListSize() {
        return newImageList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        SearchImageJson oldJson = oldImageList.get(oldItemPosition);
        SearchImageJson newJson = newImageList.get(newItemPosition);
        try {
            return oldJson.image.equals(newJson.image);
        } catch (Exception e) {
            Log.w(TAG, "areItemsTheSame", e);
            return false;
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        SearchImageJson oldJson = oldImageList.get(oldItemPosition);
        SearchImageJson newJson = newImageList.get(newItemPosition);
        try {
            return oldJson.equals(newJson);
        } catch (Exception e) {
            Log.w(TAG, "areContentsTheSame", e);
            return false;
        }
    }

}
