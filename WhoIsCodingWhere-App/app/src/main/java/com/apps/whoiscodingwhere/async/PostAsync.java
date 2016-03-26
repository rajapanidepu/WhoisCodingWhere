package com.apps.whoiscodingwhere.async;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by rpanidep on 3/25/2016.
 */
public class PostAsync extends AsyncTask<String, String, Void> {

    @Override
    protected Void doInBackground(String... params) {
        Log.e("rpanidep", params[0] + "   " + params[1]);

        return null;
    }
}
