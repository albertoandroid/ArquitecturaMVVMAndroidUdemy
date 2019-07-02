package com.androiddesdecero.arquitecturamvvm.ui.search;

import androidx.lifecycle.ViewModel;
import androidx.room.util.StringUtil;

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
}
