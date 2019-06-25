package com.androiddesdecero.arquitecturamvvm.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androiddesdecero.arquitecturamvvm.api.ApiResponse;
import com.androiddesdecero.arquitecturamvvm.api.WebServiceApi;
import com.androiddesdecero.arquitecturamvvm.db.GitHubDb;
import com.androiddesdecero.arquitecturamvvm.model.RepoSearchResponse;
import com.androiddesdecero.arquitecturamvvm.model.RepoSearchResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class 28 implements Runnable {

    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String query;
    private final WebServiceApi githubService;
    private final GitHubDb db;

    FetchNextSearchPageTask(String query, WebServiceApi githubService, GitHubDb db){
        this.query = query;
        this.githubService = githubService;
        this.db = db;
    }


    @Override
    public void run() {
        RepoSearchResult current = db.repoDao().findSearchResult(query);
        if(current == null){
            liveData.postValue(null);
            return;
        }
        final Integer nextPage = current.next;
        if(nextPage == null){
            liveData.postValue(Resource.success(false));
            return;
        }
        try {
            Response<RepoSearchResponse> response = githubService
                    .searchRepos(query, nextPage).execute();
            ApiResponse<RepoSearchResponse> apiResponse = new ApiResponse<>(response);
            if(apiResponse.isSuccessful()){
                List<Integer> ids = new ArrayList<>();
                ids.addAll(current.repoIds);
                ids.addAll(apiResponse.body.getRepoIds());
                RepoSearchResult merged = new RepoSearchResult(query, ids, apiResponse.body.total, apiResponse.getNextPage());

                try{
                    db.beginTransaction();
                    db.repoDao().insert(merged);
                    db.repoDao().insertRepos(apiResponse.body.getItems());
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                liveData.postValue(Resource.success(apiResponse.getNextPage()!=null));
            }else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, true));
            }
        }catch (Exception e){
            liveData.postValue(Resource.error(e.getMessage(), true));
        }

    }
    LiveData<Resource<Boolean>> getLiveData(){
        return liveData;
    }
}
