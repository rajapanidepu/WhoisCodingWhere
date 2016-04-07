package com.apps.whoiscodingwhere.model;

/**
 * Created by rpanidep on 3/25/2016.
 */
public class PostModel {
    String userId;
    String name;
    String languages;
    String info;
    String latitude;
    String longitude;
    Boolean isHelpNeeded;

    public PostModel(String userId, String name, String languages, String info, String latitude, String longitude, Boolean isHelpNeeded) {
        this.userId = userId;
        this.name = name;
        this.languages = languages;
        this.info = info;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isHelpNeeded = isHelpNeeded;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
