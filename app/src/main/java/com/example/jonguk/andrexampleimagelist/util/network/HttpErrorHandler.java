package com.example.jonguk.andrexampleimagelist.util.network;

import android.support.annotation.NonNull;

import retrofit2.HttpException;
import rx.functions.Action1;
import rx.functions.Action2;

/**
 * Created by Jonguk on 2017. 4. 1..
 */

public class HttpErrorHandler implements Action1<Throwable> {

    /**
     * https://developers.daum.net/services/apis/docs/errors
     * 200	정상	정상 처리된 경우
     * 401	AccessDeniedError	jsonp를 지원하지 않는 API를 jsonp로 호출한 경우
     * 401	Unauthorized	appkey를 사용가능한 whitelist가 아닌곳에서 호출한 경우
     * 403	NotAuthorizedError	등록되지 않은 appkey사용
     * 404	ResourceNotFound	API path가 잘못되어 존재하지 않는 API를 호출한 경우
     * 409	MissingParameter	필수 파라미터를 입력하지 않은 경우
     * 429	RequestThrottled	사용 가능한 Quata이상으로 API호출시
     * 500	기타	API 서비스 내부 시스템 오류
     * 504	RequestTimeout	API 서비스 연결 실패. 서비스 시스템 과부하 또는 장애로 인한 서비스 연결 실패
     */
    public static final int CODE_UNKNOWN = -1;

    private final Action2<Integer, String> mCallback;
    private final int mPermittedErrorCode;

    public HttpErrorHandler(@NonNull Action2<Integer, String> callback) {
        this (callback, CODE_UNKNOWN);
    }

    public HttpErrorHandler(@NonNull Action2<Integer, String> callback, int permittedErrorCode) {
        mCallback = callback;
        mPermittedErrorCode = permittedErrorCode;
    }

    @Override
    public void call(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
            int code = exception.code();
            String message = exception.message();
            if (code != mPermittedErrorCode) {
                mCallback.call(code, message);
            }

        } else {
            mCallback.call(CODE_UNKNOWN, "Unknown");
        }
    }
}
