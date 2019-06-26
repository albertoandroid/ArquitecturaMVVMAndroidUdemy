package com.androiddesdecero.arquitecturamvvm.model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(indices = {@Index("id"), @Index("owner_login")},
        primaryKeys = {"name", "owner_login"})
public class Repo {

    public static final int UNKNOWN_ID = -1;

    public final int id;
    @SerializedName("name")
    public final String name;

    @SerializedName("full_name")
    public final String fullName;
    @SerializedName("description")
    public final String description;
    @SerializedName("stargazers_count")
    public final int stars;
    @SerializedName("owner")
    @Embedded(prefix = "owner_")
    public final Owner owner;

    public Repo(int id, String name, String fullName, String description, int stars, Owner owner) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.stars = stars;
        this.owner = owner;
    }

    public static class Owner{
        @SerializedName("login")
        public final String login;

        @SerializedName("url")
        public final String url;

        public Owner(String login, String url) {
            this.login = login;
            this.url = url;
        }
    }
}
