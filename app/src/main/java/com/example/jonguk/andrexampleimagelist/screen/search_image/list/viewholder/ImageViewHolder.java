package com.example.jonguk.andrexampleimagelist.screen.search_image.list.viewholder;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.example.jonguk.andrexampleimagelist.R;
import com.example.jonguk.andrexampleimagelist.common.custom_view.MyImageView;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.AbsSearchImageData;
import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.ImageData;

import butterknife.BindView;

/**
 * Created by Jonguk on 2017. 3. 30..
 */

public class ImageViewHolder extends AbsViewHolder {
    private static final String TAG = "ImageViewHolder";

    @BindView(R.id.image_view) MyImageView imageView;

    public ImageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(@NonNull AbsSearchImageData data) {
        Log.d(TAG, data.toString());
        if (!(data instanceof ImageData)) return;
        ImageData imageData = (ImageData) data;
        imageView.setAspectRatio(imageData.imageWidth / imageData.imageHeight);
        imageView.setImageURI(imageData.imageUrl);
    }
}
