package com.androiddesdecero.arquitecturamvvm.repository;

import androidx.annotation.MainThread;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.androiddesdecero.arquitecturamvvm.AppExecutors;
import com.androiddesdecero.arquitecturamvvm.api.ApiResponse;

import java.util.Objects;

public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    NetworkBoundResource(AppExecutors appExecutors){
        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(ResultType data) {
                result.removeSource(dbSource);
                if(NetworkBoundResource.this.shouldFetch(data)){
                    NetworkBoundResource.this.fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, (ResultType newData)->{
                        NetworkBoundResource.this.setValue(Resource.success(newData));
                    });
                }
            }
        });
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue){
        if(!Objects.equals(result.getValue(), newValue)){
            result.setValue(newValue);
        }
    }


    private void fetchFromNetwork(final LiveData<ResultType> dbSource){

    }

    @MainThread
    protected abstract boolean shouldFetch(ResultType data);

    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    protected void onFechtFailed(){}

    public LiveData<Resource<ResultType>> asLiveData(){
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response){
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(RequestType item);

    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
}
