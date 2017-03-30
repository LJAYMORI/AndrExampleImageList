package com.example.jonguk.andrexampleimagelist.common.custom_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class MyImageView extends SimpleDraweeView {

    /**
     android:id="@+id/my_image_view"
     android:layout_width="20dp"
     android:layout_height="20dp"
     fresco:fadeDuration="300"
     fresco:actualImageScaleType="focusCrop"
     fresco:placeholderImage="@color/wait_color"
     fresco:placeholderImageScaleType="fitCenter"
     fresco:failureImage="@drawable/error"
     fresco:failureImageScaleType="centerInside"
     fresco:retryImage="@drawable/retrying"
     fresco:retryImageScaleType="centerCrop"
     fresco:progressBarImage="@drawable/progress_bar"
     fresco:progressBarImageScaleType="centerInside"
     fresco:progressBarAutoRotateInterval="1000"
     fresco:backgroundImage="@color/blue"
     fresco:overlayImage="@drawable/watermark"
     fresco:pressedStateOverlayImage="@color/red"
     fresco:roundAsCircle="false"
     fresco:roundedCornerRadius="1dp"
     fresco:roundTopLeft="true"
     fresco:roundTopRight="false"
     fresco:roundBottomLeft="false"
     fresco:roundBottomRight="true"
     fresco:roundWithOverlayColor="@color/corner_color"
     fresco:roundingBorderWidth="2dp"
     fresco:roundingBorderColor="@color/border_color"/>
     */

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
