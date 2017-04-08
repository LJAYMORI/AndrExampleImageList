package com.example.jonguk.andrexampleimagelist.screen.search_image.list.data;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

public class CompleteData extends AbsSearchImageData {

    @Override
    public Type getType() {
        return Type.COMPLETE;
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
