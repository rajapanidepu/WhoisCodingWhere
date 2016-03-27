package com.apps.whoiscodingwhere.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.whoiscodingwhere.R;
import com.apps.whoiscodingwhere.model.PostModel;
import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by rpanidep on 3/25/2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private ArrayList<PostModel>  mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostsAdapter(ArrayList<PostModel> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_post_cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView tv = (TextView) holder.mView.findViewById(R.id.info_text);
        String postText = mDataset.get(position).getUserName()+" is coding on "+mDataset.get(position).getLanguages();
        tv.setText(postText);
        ProfilePictureView profilePictureView;
        profilePictureView = (ProfilePictureView) holder.mView.findViewById(R.id.friendProfilePicture);
        profilePictureView.setProfileId(mDataset.get(position).getUserId());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}