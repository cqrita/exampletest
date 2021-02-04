package com.example.MovieDB.data;


import androidx.annotation.NonNull;

public class Trailer {
    private String id;
    private String key;
    private String site;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", site='" + site + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
