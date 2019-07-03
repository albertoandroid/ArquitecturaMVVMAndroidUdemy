package com.androiddesdecero.arquitecturamvvm.di;

import com.androiddesdecero.arquitecturamvvm.ui.repo.RepoFragment;
import com.androiddesdecero.arquitecturamvvm.ui.search.SearchFragment;
import com.androiddesdecero.arquitecturamvvm.ui.user.UserFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract RepoFragment contributeRepoFragment();

    @ContributesAndroidInjector
    abstract UserFragment contributeUserFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();
}
