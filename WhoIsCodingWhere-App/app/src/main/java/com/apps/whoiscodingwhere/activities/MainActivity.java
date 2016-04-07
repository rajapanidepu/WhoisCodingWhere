package com.apps.whoiscodingwhere.activities;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apps.whoiscodingwhere.R;
import com.apps.whoiscodingwhere.adapter.PostsAdapter;
import com.apps.whoiscodingwhere.constants.Constants;
import com.apps.whoiscodingwhere.gcm.QuickstartPreferences;
import com.apps.whoiscodingwhere.gcm.RegistrationIntentService;
import com.apps.whoiscodingwhere.model.PostModel;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CHECK_SETTINGS = 1;
    private static final int NOTIFICATION_ID = 123;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    public static FragmentManager fragmentManager;
    public static Location mLastLocation;
    PendingResult<LocationSettingsResult> result;
    ArrayList<PostModel> myDataset = new ArrayList<PostModel>();
    SharedPreferences prefs;
    private int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean isReceiverRegistered;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.e(TAG, "onReceive: " + getString(R.string.gcm_send_message));
                } else {
                    Log.e(TAG, "onReceive: " + getString(R.string.token_error_message));
                }
            }
        };


        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Log.e(TAG, "onCreate: starting service" );
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }else{
            Log.e(TAG, "onCreate: play services failed" );
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        prefs = this.getSharedPreferences(
                "com.apps.whoiscodingwhere", Context.MODE_PRIVATE);

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                Log.e("rpanidep", "Main Activity : updated accessToken = " + currentAccessToken.getToken());
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_post, null);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setView(dialogView)
                        // Add action buttons
                        .setPositiveButton(R.string.post, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                                EditText languages = (EditText) dialogView.findViewById(R.id.etLanguages);
                                final String lang = languages.getText().toString();
                                EditText info = (EditText) dialogView.findViewById(R.id.etInfo);
                                final String infotext = info.getText().toString();
                                myDataset.add(new PostModel(AccessToken.getCurrentAccessToken().getUserId(), prefs.getString("name", "invalid"), lang, infotext, null, null, false));
                                mAdapter.notifyDataSetChanged();

                                //create notification
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(getApplicationContext())
                                                .setSmallIcon(R.drawable.com_facebook_button_icon)
                                                .setContentTitle("My notification")
                                                .setContentText("Hello World!");

                                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);

                                // The stack builder object will contain an artificial back stack for the
                                // started Activity.
                                // This ensures that navigating backward from the Activity leads out of
                                // your application to the Home screen.
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                // Adds the back stack for the Intent (but not the Intent itself)
                                stackBuilder.addParentStack(MainActivity.class);
                                // Adds the Intent that starts the Activity to the top of the stack
                                stackBuilder.addNextIntent(resultIntent);
                                PendingIntent resultPendingIntent =
                                        stackBuilder.getPendingIntent(
                                                0,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );
                                mBuilder.setContentIntent(resultPendingIntent);
                                mBuilder.addAction(R.drawable.com_facebook_send_button_icon,
                                        "end", null);
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                // mId allows you to update the notification later on.
                                mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

                                //sending to server
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ServerURL + "/posts",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // Display the first 500 characters of the response string.
                                                Log.e("rpanidep", response);
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("languages", lang);
                                        params.put("info", infotext);
                                        params.put("userId", AccessToken.getCurrentAccessToken().getUserId());
                                        params.put("name", prefs.getString("name", "invalid"));
                                        if (mLastLocation != null) {
                                            params.put("latitude", "" + mLastLocation.getLatitude());
                                            params.put("longitude", "" + mLastLocation.getLongitude());
                                        }
                                        return params;
                                    }

                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Content-Type", "application/x-www-form-urlencoded");
                                        return params;
                                    }
                                };
                                queue.add(stringRequest);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create();
                builder.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mRecyclerView = (RecyclerView) findViewById(R.id.posts_recycler);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        myDataset.add(new PostModel("23456", "Raja", "Java", "", null, true));
//
//        myDataset.add(new PostModel("65432", "Ravi", "PHP", "", null, false));
//        myDataset.add(new PostModel("6543", "Teja", "C++", "", null, true));

        loadPostsFromServer();
        mAdapter = new PostsAdapter(myDataset, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        ProfilePictureView navProfilePictureView;

        navProfilePictureView = (ProfilePictureView) navigationView.getHeaderView(0).findViewById(R.id.navProfilePicture);
        String profileId = AccessToken.getCurrentAccessToken().getUserId();

        if (profileId != null) {
            navProfilePictureView.setProfileId(profileId);

        } else {
            Log.e("rpanidep", "ERROR - PROFILE ID is NULL");
        }
//        TextView tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
//        tvUserName.setText(prefs.getString("name", "invalid"));

//        TextView tvEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
//        tvEmail.setText(prefs.getString("email","invalid"));
        navigationView.setNavigationItemSelectedListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this).addConnectionCallbacks(this).addApi(AppIndex.API).addApi(LocationServices.API).build();

    }


    private void loadPostsFromServer() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String uri = String.format(Constants.ServerURL + "/posts");
        Log.e("rpanidep", "Loading posts from server");
        StringRequest myReq = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("rpanidep", "posts from server " + response);

                        parseJsonArray(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(myReq);

    }

    private void parseJsonArray(String jsonString) {
        try {
            Gson gson = new Gson();
            JSONObject json = new JSONObject(jsonString);
            JSONArray posts = json.getJSONArray("PostsResponse");
            for (int i = 0; i < posts.length(); i++) {
                String postString = posts.optJSONObject(i).toString();
                PostModel post = gson.fromJson(postString, PostModel.class);
                Log.e("rpanidep", "" + gson.toJson(post).toString());
                myDataset.add(post);
                mAdapter.notifyDataSetChanged();
            }

            ((PostsAdapter) mAdapter).addMarkers();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.apps.whoiscodingwhere/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.apps.whoiscodingwhere/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("rpanidep", "at onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        } else {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    client);
            if (mLastLocation != null) {
                Log.e("rpanidep", "lati " + mLastLocation.getLatitude() + " longi " + mLastLocation.getLongitude());
                ((PostsAdapter) mAdapter).updateLocation(mLastLocation);

            }

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(new LocationRequest());
            result = LocationServices.SettingsApi.checkLocationSettings(client,
                    builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                    final Status status = locationSettingsResult.getStatus();
                    final LocationSettingsStates locationSettingsStates = locationSettingsResult.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location requests here.

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        MainActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("rpanidep", "at onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("rpanidep", "at onConnectionFailed");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            Log.e("rpanidep", " At onActivityRequest " + resultCode);
        } else {
            Log.e("rpanidep", MY_PERMISSIONS_REQUEST_LOCATION + " At onActivityRequest " + resultCode);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        Log.e(TAG, "checkPlayServices: "+resultCode );
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        Log.e(TAG, "checkPlayServices: returning true for playservices");
        return true;
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }
}
