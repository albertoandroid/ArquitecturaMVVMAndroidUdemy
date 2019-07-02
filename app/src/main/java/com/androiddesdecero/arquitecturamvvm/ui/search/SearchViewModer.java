package com.androiddesdecero.arquitecturamvvm.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.room.util.StringUtil;

import com.androiddesdecero.arquitecturamvvm.repository.RepoRepository;
import com.androiddesdecero.arquitecturamvvm.repository.Resource;

import java.util.Objects;

public class SearchViewModer extends ViewModel {



    static class LoadMoreState{
        private final boolean running;
        private final String errorMessage;
        private boolean handledError = false;

        LoadMoreState(boolean running, String errorMessage){
            this.running = running;
            this.errorMessage = errorMessage;
        }

        boolean isRunning(){
            return running;
        }

        String getErrorMessage(){
            return errorMessage;
        }

        String getErrorMessageIfNoHandled(){
            if(handledError){
                return null;
            }
            handledError = true;
            return errorMessage;
        }
    }

    static class NextPageHandler implements Observer<Resource<Boolean>>{

        private LiveData<Resource<Boolean>> nextPageLiveData;
        private final MutableLiveData<LoadMoreState> loadMoreState = new MutableLiveData<>();
        private String query;
        private final RepoRepository repository;
        boolean hasMore;

        NextPageHandler(RepoRepository repository){
            this.repository = repository;
            reset();
        }

        void queryNextPage(String query){
            if (Objects.equals(this.query, query)) {
                return;
            }
            unregister();
            this.query = query;
            nextPageLiveData = repository.searchNextPage(query);
            nextPageLiveData.observeForever(this);
        }

        private void unregister(){
            if(nextPageLiveData != null){
                nextPageLiveData.removeObserver(this);
                nextPageLiveData = null;
                if(hasMore){
                    query = null;
                }
            }
        }

        private void reset(){
            unregister();
            hasMore = true;
            loadMoreState.setValue(new LoadMoreState(false, null));
        }

        MutableLiveData<LoadMoreState> getLoadMoreState(){
            return loadMoreState;
        }

        @Override
        public void onChanged(Resource<Boolean> result) {
            if(result == null){
                reset();
            }else {
                switch (result.status){
                    case SUCCESS:
                        hasMore = Boolean.TRUE.equals(result.data);
                        unregister();
                        loadMoreState.setValue(new LoadMoreState(false, null));
                        break;
                    case ERROR:
                        hasMore = true;
                        unregister();
                        loadMoreState.setValue(new LoadMoreState(false, result.message));
                        break;
                }
            }
        }
    }
}
