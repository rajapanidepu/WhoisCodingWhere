package com.apps.whoiscodingwhere.model;

import android.location.Location;

/**
 * Created by rpanidep on 3/25/2016.
 */
public class PostModel {
    String languages;
    String info;
    Location location;
    Boolean isHelpNeeded;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;
    String userName;

    public PostModel(String userName, String languages, String info, Location location, Boolean isHelpNeeded) {
        this.languages = languages;
        this.userName = userName;
        this.isHelpNeeded = isHelpNeeded;
        this.location = location;
        this.info = info;
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
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
