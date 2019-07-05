package com.androiddesdecero.arquitecturamvvm.di;

import android.app.Application;

import com.androiddesdecero.arquitecturamvvm.GithubApp;
import com.androiddesdecero.arquitecturamvvm.MainActivity;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(GithubApp githubApp);
}
