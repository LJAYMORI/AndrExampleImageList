package com.example.jonguk.andrexampleimagelist.screen.search_image.list.data;

import android.support.annotation.LayoutRes;

import com.example.jonguk.andrexampleimagelist.R;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

public abstract class AbsSearchImageData {

    private static AtomicLong sIdGenerator = new AtomicLong();

    public enum Type {
        ITEM(R.layout.item_image),
        LOADING(R.layout.item_loading),
        COMPLETE(R.layout.item_complete);

        private int layoutId;

        Type(@LayoutRes int layoutId) {
            this.layoutId = layoutId;
        }

        public int getLayoutId() {
            return layoutId;
        }
    }

    private long mId;
    {
        mId = sIdGenerator.getAndIncrement();
    }

    public long getId() {
        return mId;
    }

    public abstract Type getType();

    public abstract int getTypeOrdinal();

    public boolean isSameId(long id) {
        return mId == id;
    }

    public abstract boolean isSameContent(AbsSearchImageData data);

}
