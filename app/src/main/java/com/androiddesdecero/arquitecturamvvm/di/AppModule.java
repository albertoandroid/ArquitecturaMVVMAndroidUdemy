package com.androiddesdecero.arquitecturamvvm.di;

import android.app.Application;

import androidx.room.Room;

import com.androiddesdecero.arquitecturamvvm.api.WebServiceApi;
import com.androiddesdecero.arquitecturamvvm.db.GitHubDb;
import com.androiddesdecero.arquitecturamvvm.db.RepoDao;
import com.androiddesdecero.arquitecturamvvm.db.UserDao;
import com.androiddesdecero.arquitecturamvvm.utils.LiveDataCallAdapterFactory;
import com.androiddesdecero.arquitecturamvvm.viewmodel.GithubViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    WebServiceApi provideGithubService(){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(WebServiceApi.class);
    }

    @Singleton
    @Provides
    GitHubDb provideDb(Application app){
        return Room.databaseBuilder(app, GitHubDb.class, "github.db").build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(GitHubDb db){
        return db.userDao();
    }

    @Singleton
    @Provides
    RepoDao provideRepoDao(GitHubDb db){
        return db.repoDao();
    }
}
