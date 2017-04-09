package com.example.jonguk.andrexampleimagelist.screen.search_image.list.data;

import org.parceler.Parcel;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

@Parcel
public class LoadingData extends AbsSearchImageData {

    public LoadingData() {}

    @Override
    public Type getType() {
        return Type.LOADING;
    }

    @Override
    public int getTypeOrdinal() {
        return getType().ordinal();
    }

    @Override
    public boolean isSameContent(AbsSearchImageData data) {
        return isSameId(data.getId());
    }

}
