package com.example.jonguk.andrexampleimagelist.util.image_loader;

import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Jonguk on 2017. 3. 28..
 */

public class ImageLoader {
    public static final String NO_IMAGE_URL = "http://www.jarodsafehouse.com/_wp/wp-content/uploads/2014/08/sorry_noimage350x272.jpg";

    private ImageLoader() {}

    public static void initialize(Context context) {
        Fresco.initialize(context);
        /*ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context)
                .setBitmapMemoryCacheParamsSupplier(new DefaultBitmapMemoryCacheParamsSupplier())
                .setCacheKeyFactory(cacheKeyFactory)
                .setDownsampleEnabled(true)
                .setWebpSupportEnabled(true)
                .setEncodedMemoryCacheParamsSupplier(encodedCacheParamsSupplier)
                .setExecutorSupplier(executorSupplier)
                .setImageCacheStatsTracker(imageCacheStatsTracker)
                .setMainDiskCacheConfig(mainDiskCacheConfig)
                .setMemoryTrimmableRegistry(memoryTrimmableRegistry)
                .setNetworkFetchProducer(networkFetchProducer)
                .setPoolFactory(poolFactory)
                .setProgressiveJpegConfig(progressiveJpegConfig)
                .setRequestListeners(requestListeners)
                .setSmallImageDiskCacheConfig(smallImageDiskCacheConfig)
                .build();
        Fresco.initialize(context, config);*/
    }
}
