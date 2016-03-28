package com.apps.whoiscodingwhere.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.apps.whoiscodingwhere.R;
import com.apps.whoiscodingwhere.activities.MainActivity;
import com.apps.whoiscodingwhere.model.PostModel;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by rpanidep on 3/25/2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private ArrayList<PostModel> mDataset;
    private Context context;


    // Provide a suitable constructor (depends on the kind of dataset)
    public PostsAdapter(ArrayList<PostModel> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_header, parent, false);
            return new ViewHeaderHolder(v);
        }
        // create a new view
        else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_post_cardview, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (holder instanceof ViewHeaderHolder) {
            ViewHeaderHolder VHheader = (ViewHeaderHolder) holder;

        } else if (holder instanceof ViewHolder) {
            position = position - 1;
            ViewHolder VHitem = (ViewHolder) holder;
//            VHitem.txtName.setText("HELLO");

            String postText = mDataset.get(position).getUserName() + " is coding on " + mDataset.get(position).getLanguages();
            VHitem.tv.setText(postText);

            VHitem.profilePictureView.setProfileId(mDataset.get(position).getUserId());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ProfilePictureView profilePictureView;
        TextView tv;


        public ViewHolder(View v) {
            super(v);
            mView = v;
            this.tv = (TextView) v.findViewById(R.id.info_text);
            profilePictureView = (ProfilePictureView) v.findViewById(R.id.friendProfilePicture);
        }

    }

    public static class ViewHeaderHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        // each data item is just a string in this case
        public View mView;
        private GoogleMap mMap;

        public ViewHeaderHolder(View v) {
            super(v);
            mView = v;
            v.setNestedScrollingEnabled(false);
            SupportMapFragment mapFragment = (SupportMapFragment) MainActivity.fragmentManager.findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            mapFragment.getView().setNestedScrollingEnabled(false);
            mapFragment.getView().setScrollContainer(false);
            mapFragment.getView().setEnabled(false);
            mView.setClickable(true);
            mView.setFocusable(true);
            mView.setDuplicateParentStateEnabled(false);
            mView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e("rpanidep",""+event.getAction());
                    return false;
                }
            });

            mView.setNestedScrollingEnabled(false);
            mView.setNestedScrollingEnabled(false);
            mView.setScrollContainer(false);
            mView.setEnabled(false);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("rpanidep","Here I am");
                }
            });


        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Add a marker in Sydney, Australia, and move the camera.
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }
}