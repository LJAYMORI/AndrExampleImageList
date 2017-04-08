package com.example.jonguk.andrexampleimagelist.screen.search_image.list.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.jonguk.andrexampleimagelist.screen.search_image.list.data.AbsSearchImageData;

import butterknife.ButterKnife;

/**
 * Created by Jonguk on 2017. 4. 8..
 */

public abstract class AbsViewHolder extends RecyclerView.ViewHolder {
    public AbsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(@NonNull AbsSearchImageData data);
}
