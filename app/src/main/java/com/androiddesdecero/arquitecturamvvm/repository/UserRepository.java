package com.androiddesdecero.arquitecturamvvm.repository;

import androidx.lifecycle.LiveData;

import com.androiddesdecero.arquitecturamvvm.AppExecutors;
import com.androiddesdecero.arquitecturamvvm.api.ApiResponse;
import com.androiddesdecero.arquitecturamvvm.api.WebServiceApi;
import com.androiddesdecero.arquitecturamvvm.db.UserDao;
import com.androiddesdecero.arquitecturamvvm.model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private final UserDao userDao;
    private final WebServiceApi githubService;
    private final AppExecutors appExecutors;

    @Inject
    UserRepository(AppExecutors appExecutors, UserDao userDao, WebServiceApi webServiceApi){
        this.userDao = userDao;
        this.githubService = webServiceApi;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<User>> loadUser(String login){
        return new NetworkBoundResource<User,User>(appExecutors){

            @Override
            protected boolean shouldFetch(User data) {
                return data == null;
            }

            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.findByLogin(login);
            }

            @Override
            protected void saveCallResult(User item) {
                userDao.insert(item);
            }

            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return githubService.getUser(login);
            }
        }.asLiveData();
    }
}
