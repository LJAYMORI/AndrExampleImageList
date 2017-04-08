package com.example.jonguk.andrexampleimagelist.screen.search_image.list;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.AbsSearchImageData;

import java.util.List;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

public class ImageListDiffCallback extends DiffUtil.Callback {
    private static final String TAG = "ImageListDiffCallback";

    private final List<AbsSearchImageData> oldImageList;
    private final List<? extends AbsSearchImageData> newImageList;

    public ImageListDiffCallback(List<AbsSearchImageData> oldImageList, List<? extends AbsSearchImageData> newImageList) {
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
        AbsSearchImageData oldJson = oldImageList.get(oldItemPosition);
        AbsSearchImageData newJson = newImageList.get(newItemPosition);
        try {
            return oldJson.isSameId(newJson.getId());
        } catch (Exception e) {
            Log.w(TAG, "areItemsTheSame", e);
            return false;
        }
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        AbsSearchImageData oldJson = oldImageList.get(oldItemPosition);
        AbsSearchImageData newJson = newImageList.get(newItemPosition);
        try {
            return oldJson.isSameContent(newJson);
        } catch (Exception e) {
            Log.w(TAG, "areContentsTheSame", e);
            return false;
        }
    }

}
