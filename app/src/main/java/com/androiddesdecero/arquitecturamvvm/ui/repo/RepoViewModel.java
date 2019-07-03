package com.androiddesdecero.arquitecturamvvm.ui.repo;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.androiddesdecero.arquitecturamvvm.model.Contributor;
import com.androiddesdecero.arquitecturamvvm.model.Repo;
import com.androiddesdecero.arquitecturamvvm.repository.RepoRepository;
import com.androiddesdecero.arquitecturamvvm.repository.Resource;
import com.androiddesdecero.arquitecturamvvm.utils.AbsentLiveData;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class RepoViewModel extends ViewModel {

    final MutableLiveData<RepoId> repoId;
    private final LiveData<Resource<Repo>> repo;
    private final LiveData<Resource<List<Contributor>>> contributors;

    @Inject
    public RepoViewModel(RepoRepository repository){
        this.repoId = new MutableLiveData<>();
        repo = Transformations.switchMap(repoId, new Function<RepoId, LiveData<Resource<Repo>>>() {
            @Override
            public LiveData<Resource<Repo>> apply(RepoId input) {
                if(input.isEmpty()){
                    return AbsentLiveData.create();
                }else{
                    return repository.loadRepo(input.owner, input.name);
                }
            }
        });

        contributors = Transformations.switchMap(repoId, new Function<RepoId, LiveData<Resource<List<Contributor>>>>() {
            @Override
            public LiveData<Resource<List<Contributor>>> apply(RepoId input) {
                if(input.isEmpty()){
                    return AbsentLiveData.create();
                }else{
                    return repository.loadContributors(input.owner, input.name);
                }
            }
        });
    }

    public LiveData<Resource<Repo>> getRepo(){
        return repo;
    }

    public LiveData<Resource<List<Contributor>>> getContributors(){
        return contributors;
    }

    public void retry(){
        RepoId current = repoId.getValue();
        if(current != null && !current.isEmpty()){
            repoId.setValue(current);
        }
    }

    public void setId(String owner, String name){
        RepoId update = new RepoId(owner, name);
        if(Objects.equals(repoId.getValue(), update)){
            return;
        }
        repoId.setValue(update);
    }


    static class RepoId{
        public final String owner;
        public final String name;

        RepoId(String owner, String name){
            this.owner = owner == null ? null :owner.trim();
            this.name = name == null ? null : name.trim();
        }

        boolean isEmpty(){
            return owner == null || name == null || owner.length() == 0 || name.length() == 0;
        }
    }
}
