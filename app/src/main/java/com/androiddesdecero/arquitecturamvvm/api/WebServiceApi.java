package com.androiddesdecero.arquitecturamvvm.api;

import androidx.lifecycle.LiveData;

import com.androiddesdecero.arquitecturamvvm.model.Repo;
import com.androiddesdecero.arquitecturamvvm.model.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WebServiceApi {

    @GET("user/{login}")
    LiveData<ApiResponse<User>> getUser(@Path("login") String login);

    @GET("user/{login}/repos")
    LiveData<ApiResponse<List<Repo>>> getRepos(@Path("login") String login);

    @GET("repos/{owner}/{name}")
    LiveData<ApiResponse<Repo>> getRepo(@Path("owner") String owner, @Path("name") String name);
}
