package com.androiddesdecero.arquitecturamvvm.api;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

import retrofit2.Response;

public class ApiResponse<T> {

    private static final Pattern LINK_PATTERN = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    private static final Pattern PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)");

    private static final String NEXT_LINK = "next";
    public final int code;
    public final T body;
    public final String errorMessage;
    public final Map<String, String> links;

    public ApiResponse(Throwable error){
        code = 500;
        body = null;
        errorMessage = error.getMessage();
        links = Collections.emptyMap();
    }

    public ApiResponse(Response<T> response){
        code = response.code();
        if(response.isSuccessful()){
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if(response.errorBody() != null){
                try{
                    message = response.errorBody().string();
                }catch (IOException ignored){
                    Log.d(ignored.getMessage(), "Error while parsing response");
                }

            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }


}
