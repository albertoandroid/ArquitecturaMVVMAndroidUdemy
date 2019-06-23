package com.androiddesdecero.arquitecturamvvm.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RepoSearchResponse {

    @SerializedName("total_count")
    public int total;
    @SerializedName("items")
    public List<Repo> items;
    public Integer nextPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public List<Integer> getRepoIds(){
        List<Integer> repoIds = new ArrayList<>();
        for (Repo repo: items){
            repoIds.add(repo.id);
        }
        return repoIds;
    }
}
