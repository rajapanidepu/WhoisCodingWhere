package com.apps.whoiscodingwhere.model;

import java.util.ArrayList;

/**
 * Created by rpanidep on 3/26/2016.
 */
public class PostsResponse {
    ArrayList<PostModel> posts;

    public ArrayList<PostModel> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostModel> posts) {
        this.posts = posts;
    }
}
