package com.example.jonguk.andrexampleimagelist.screen.search_image;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.example.jonguk.andrexampleimagelist.R;
import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageResponseJson;
import com.example.jonguk.andrexampleimagelist.util.network.BaseRequest;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Jonguk on 2017. 3. 30..
 */

class SearchImageRequest extends BaseRequest {

    private static final int RESULT_COUNT_DEFAULT = 10;

    interface ApiService {
        @GET("search/image")
        Observable<SearchImageResponseJson> images(@Query("apikey") String apiKey,
                                                   @Query("q") String query,
                                                   @Query("output") String output,
                                                   @Query("pageno")int pageno,
                                                   @Query("result")int result);
    }

    static Observable<SearchImageResponseJson> images(@NonNull Context context,
                                                      @NonNull String query,
                                                      @IntRange(from = 1) int pageno) {
        return Observable.defer(() -> createRequest(ApiService.class)
                .images(context.getString(R.string.api_key_daum_public2), query, "json", pageno,
                        RESULT_COUNT_DEFAULT)
                .distinct());
    }
}
