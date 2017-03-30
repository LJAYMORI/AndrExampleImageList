package com.example.jonguk.andrexampleimagelist.screen.search_image.list;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.jonguk.andrexampleimagelist.R;
import com.example.jonguk.andrexampleimagelist.common.custom_view.MyImageView;
import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageJson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jonguk on 2017. 3. 30..
 */

class ImageViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "ImageViewHolder";

    @BindView(R.id.image_view) MyImageView imageView;

    ImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bind(SearchImageJson data) {
        Log.d(TAG, data.toString());
        imageView.setAspectRatio(Float.parseFloat(data.width) / Float.parseFloat(data.height));
        imageView.setImageURI(data.image);
    }
}
