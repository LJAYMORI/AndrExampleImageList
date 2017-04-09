package com.example.jonguk.andrexampleimagelist.screen.search_image.list.data;

import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageJson;

import org.parceler.Parcel;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

@Parcel
public class ImageData extends AbsSearchImageData {

    public String imageUrl;
    public float imageWidth;
    public float imageHeight;

    public ImageData() {}

    @Override
    public Type getType() {
        return Type.ITEM;
    }

    @Override
    public int getTypeOrdinal() {
        return getType().ordinal();
    }

    @Override
    public boolean isSameContent(AbsSearchImageData data) {
        if (data == null || !(data instanceof ImageData)) return false;
        ImageData imageData = (ImageData) data;
        return imageUrl.equals(imageData.imageUrl)
                && imageWidth == imageData.imageWidth && imageHeight == imageData.imageHeight;
    }

    public static List<ImageData> convertToItemDataList(List<SearchImageJson> list) {
        LinkedList<ImageData> resultList = new LinkedList<>();
        for (SearchImageJson json : list) {
            ImageData data = new ImageData();
            data.imageUrl = json.image;
            data.imageWidth = Float.parseFloat(json.width);
            data.imageHeight = Float.parseFloat(json.height);
            resultList.add(data);
        }
        return resultList;
    }
}
