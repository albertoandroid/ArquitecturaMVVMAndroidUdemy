package com.androiddesdecero.arquitecturamvvm.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.androiddesdecero.arquitecturamvvm.model.Contributor;
import com.androiddesdecero.arquitecturamvvm.model.Repo;
import com.androiddesdecero.arquitecturamvvm.model.RepoSearchResult;
import com.androiddesdecero.arquitecturamvvm.model.User;

@Database(entities = {User.class, Repo.class, Contributor.class, RepoSearchResult.class}, version = 1)
public abstract class GitHubDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public RepoDao repoDao();
}
