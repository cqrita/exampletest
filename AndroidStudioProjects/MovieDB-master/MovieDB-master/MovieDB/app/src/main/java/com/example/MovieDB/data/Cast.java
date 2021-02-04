package com.example.MovieDB.data;


import androidx.annotation.NonNull;

public class Cast  {
    private String id;
    private String name;
    private String profile_path;
    private String order;
    private String character;
    private String biography;
    private String birthday;
    private String deathday;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cast{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", profile_path='" + profile_path + '\'' +
                ", order='" + order + '\'' +
                ", character='" + character + '\'' +
                ", biography='" + biography + '\'' +
                ", birthday='" + birthday + '\'' +
                ", deathday='" + deathday + '\'' +
                '}';
    }
}
