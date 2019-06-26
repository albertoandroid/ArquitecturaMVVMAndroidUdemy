package com.androiddesdecero.arquitecturamvvm.repository;

import androidx.lifecycle.LiveData;

import com.androiddesdecero.arquitecturamvvm.AppExecutors;
import com.androiddesdecero.arquitecturamvvm.api.ApiResponse;
import com.androiddesdecero.arquitecturamvvm.api.WebServiceApi;
import com.androiddesdecero.arquitecturamvvm.db.GitHubDb;
import com.androiddesdecero.arquitecturamvvm.db.RepoDao;
import com.androiddesdecero.arquitecturamvvm.model.Contributor;
import com.androiddesdecero.arquitecturamvvm.model.Repo;
import com.androiddesdecero.arquitecturamvvm.utils.RateLimiter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RepoRepository {

    private final GitHubDb db;
    private final RepoDao repoDao;
    private final WebServiceApi githubService;
    private final AppExecutors appExecutors;

    private RateLimiter<String> repoListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public RepoRepository(AppExecutors appExecutors, GitHubDb db, RepoDao repoDao, WebServiceApi githubService){
        this.db = db;
        this.repoDao = repoDao;
        this.githubService = githubService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<List<Repo>>> loadRepos(String owner){
        return new NetworkBoundResource<List<Repo>, List<Repo>>(appExecutors){

            @Override
            protected boolean shouldFetch(List<Repo> data) {
                return data == null || data.isEmpty() || repoListRateLimit.shouldFetch(owner);
            }

            @Override
            protected LiveData<List<Repo>> loadFromDb() {
                return repoDao.loadRepositories(owner);
            }

            @Override
            protected void saveCallResult(List<Repo> item) {
                repoDao.insertRepos(item);
            }

            @Override
            protected LiveData<ApiResponse<List<Repo>>> createCall() {
                return githubService.getRepos(owner);
            }

            @Override
            protected void onFetchFailed(){
                repoListRateLimit.reset(owner);
            }
        }.asLiveData();

    }

    public LiveData<Resource<Repo>> loadRepo(String owner, String name){
        return new NetworkBoundResource<Repo, Repo>(appExecutors){

            @Override
            protected boolean shouldFetch(Repo data) {
                return data == null;
            }

            @Override
            protected LiveData<Repo> loadFromDb() {
                return repoDao.load(owner, name);
            }

            @Override
            protected void saveCallResult(Repo item) {
                repoDao.insert(item);
            }

            @Override
            protected LiveData<ApiResponse<Repo>> createCall() {
                return githubService.getRepo(owner, name);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<Contributor>>> loadContributors(String owner, String name){
        return new NetworkBoundResource<List<Contributor>, List<Contributor>>(appExecutors){

            @Override
            protected boolean shouldFetch(List<Contributor> data) {
                return data == null || data.isEmpty();
            }

            @Override
            protected LiveData<List<Contributor>> loadFromDb() {
                return repoDao.loadContributors(owner, name);
            }

            @Override
            protected void saveCallResult(List<Contributor> contributors) {
                for(Contributor contributor: contributors){
                    contributor.setRepoName(name);
                    contributor.setRepoName(owner);
                }
                db.beginTransaction();
                try{
                    repoDao.creatreRepoIfNotExists(new Repo(Repo.UNKNOWN_ID, name, owner + "/" +name,
                            "", 0, new Repo.Owner(owner, null)));
                    repoDao.insertContributors(contributors);
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
            }

            @Override
            protected LiveData<ApiResponse<List<Contributor>>> createCall() {
                return githubService.getContributors(owner, name);
            }
        }.asLiveData();
    }
 }
