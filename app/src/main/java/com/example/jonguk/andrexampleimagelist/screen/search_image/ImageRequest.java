package com.example.jonguk.andrexampleimagelist.screen.search_image;

import android.content.res.Resources;

import com.example.jonguk.andrexampleimagelist.R;
import com.example.jonguk.andrexampleimagelist.json.search_image.SearchImageResponseJson;
import com.example.jonguk.andrexampleimagelist.util.network.BaseRequest;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Jonguk on 2017. 3. 30..
 */

public class ImageRequest extends BaseRequest {
    interface ApiService {
        @GET("search/image")
        Observable<SearchImageResponseJson> images(@Query("apikey") String apiKey,
                                                   @Query("q") String query,
                                                   @Query("output") String output);
    }

    static Observable<SearchImageResponseJson> images(Resources res, String query) {
        return createRequest(ApiService.class)
                .images(res.getString(R.string.api_key_daum_public), query, "json");
    }
}
