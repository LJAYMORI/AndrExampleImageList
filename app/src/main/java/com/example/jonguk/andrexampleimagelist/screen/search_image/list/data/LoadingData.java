package com.example.jonguk.andrexampleimagelist.screen.search_image.list.data;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

public class LoadingData extends AbsSearchImageData {

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
