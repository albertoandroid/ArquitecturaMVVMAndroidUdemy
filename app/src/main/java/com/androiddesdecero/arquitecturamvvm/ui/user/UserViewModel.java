package com.androiddesdecero.arquitecturamvvm.ui.user;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.androiddesdecero.arquitecturamvvm.model.Repo;
import com.androiddesdecero.arquitecturamvvm.model.User;
import com.androiddesdecero.arquitecturamvvm.repository.RepoRepository;
import com.androiddesdecero.arquitecturamvvm.repository.Resource;
import com.androiddesdecero.arquitecturamvvm.repository.UserRepository;
import com.androiddesdecero.arquitecturamvvm.utils.AbsentLiveData;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class UserViewModel extends ViewModel {

    final MutableLiveData<String> login = new MutableLiveData<>();

    private final LiveData<Resource<List<Repo>>> repositories;
    private final LiveData<Resource<User>> user;

    @Inject
    public UserViewModel(UserRepository userRepository, RepoRepository repoRepository){
        user = Transformations.switchMap(login, new Function<String, LiveData<Resource<User>>>() {
            @Override
            public LiveData<Resource<User>> apply(String login) {
                if(login == null){
                    return AbsentLiveData.create();
                }else {
                    return userRepository.loadUser(login);
                }
            }
        });

        repositories = Transformations.switchMap(login, login -> {
            if(login == null){
                return AbsentLiveData.create();
            }else {
                return repoRepository.loadRepos(login);
            }
        });
    }

    public void setLogin(String login){
        if(Objects.equals(this.login.getValue(), login)){
            return;
        }
        this.login.setValue(login);
    }

    public LiveData<Resource<User>> getUser() {
        return user;
    }

    public LiveData<Resource<List<Repo>>> getRepositories() {
        return repositories;
    }

    public void retry(){
        if(this.login.getValue() != null){
            this.login.setValue(this.login.getValue());
        }
    }
}
