package com.apps.whoiscodingwhere.model;

import android.location.Location;

/**
 * Created by rpanidep on 3/25/2016.
 */
public class PostModel {
    String userId;
    String name;
    String languages;
    String info;
    Location location;
    Boolean isHelpNeeded;

    public PostModel(String userId, String name, String languages, String info, Location location, Boolean isHelpNeeded) {
        this.userId = userId;
        this.name = name;
        this.languages = languages;
        this.info = info;
        this.location = location;
        this.isHelpNeeded = isHelpNeeded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getHelpNeeded() {
        return isHelpNeeded;
    }

    public void setHelpNeeded(Boolean helpNeeded) {
        isHelpNeeded = helpNeeded;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String userName) {
        this.name = userName;
    }

}
