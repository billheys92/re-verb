package com.re.reverb.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;

public class MainViewPagerActivity extends ActionBarActivity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener
{

    static final int CREATE_POST_REQUEST = 1;  // The request code for creating a post activity
    static final int CREATE_REPLY_POST_REQUEST = 3;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 2; //for checking playservices

	static final int NUM_PAGES = 3;
	private int defaultPage = 1;
	
	MainViewPagerAdapter mPagerAdapter;
    ViewPager mViewPager;
    LocationClient mLocationClient;
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    //FeedFragment feedFragment = new FeedFragment();
    NewFeedFragment newFeedFragment = new NewFeedFragment();
    UserProfileFragment userProfileFragment = new UserProfileFragment();
    RegionsFragment regionsFragment = new RegionsFragment();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(defaultPage);

        //set default preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        servicesConnected();

        mLocationClient = new LocationClient(this, this, this);

        mPrefs = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();

    }

    public void returnToDefaultPage() {
        mViewPager.setCurrentItem(defaultPage);
    }
    
    public class MainViewPagerAdapter extends FragmentPagerAdapter {
    	 
    	public MainViewPagerAdapter(FragmentManager fm) {
    	     super(fm);
    	 }
    	 
    	 @Override
    	 public Fragment getItem(int i) {
    	     switch(i){
    	     	case 0: return userProfileFragment;
    	     	case 1: return newFeedFragment;
    	     	case 2: return regionsFragment;
    	     	default: //TODO throw an error
    	    	 	return null;
    	     }
    	 }

    	 @Override
    	 public int getCount() {
    	     return NUM_PAGES;
    	 }
    }

    public void startCreatePostActivity(View view){
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivityForResult(intent, CREATE_POST_REQUEST);
    }

    public void startCreateReplyPostActivity(View view, int postId)
    {
        Intent intent = new Intent(this, CreateReplyPostActivity.class);
        intent.putExtra(CreateReplyPostActivity.POST_ID_EXTRA, postId);
        startActivityForResult(intent, CREATE_REPLY_POST_REQUEST);
    }

    public void startCreateRegionActivity(View view) {
        Intent intent = new Intent(this, CreateRegionActivity.class);
        intent.putExtra("SELECTED_REGION_ID", -1);
        startActivity(intent);
    }

    public void openSettings()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        servicesConnected();    //try request again
                        break;
                }
                break;

            case CREATE_POST_REQUEST :
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        newFeedFragment.onRefresh();
                        break;

                }
                break;
            case CREATE_REPLY_POST_REQUEST :
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        newFeedFragment.onRefresh();
                        break;

                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == defaultPage) {
            super.onBackPressed();
        }
        else {
            mViewPager.setCurrentItem(defaultPage);
        }

    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }


    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Reverb", "Google Play services is available.");
            return true;
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            if (errorDialog != null) {
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getSupportFragmentManager(), "Location Updates");
            }
            return false;
        }
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        Toast.makeText(this, "Connected to location services", Toast.LENGTH_SHORT).show();
        Log.d("Reverb", "Connected to location services");
        updateLocation();
    }

    void updateLocation()
    {
        if(mLocationClient.isConnected())
        {
            Location location = mLocationClient.getLastLocation();
            String msg = "Updated Location: " +
                    Double.toString(location.getLatitude()) + "," +
                    Double.toString(location.getLongitude());
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//            PostManagerImpl.getNumNewPosts(location.getLatitude(),location.getLongitude(),1.0f,"2015-02-25 00:46:15");
            Reverb.getInstance().setCurrentLocation((float) location.getLatitude(), (float) location.getLongitude());
        }
        else
        {
            mLocationClient.connect();
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected from location services. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    private void showErrorDialog(int errorCode) {

        Toast.makeText(this, "Error Connecting to Location Services", Toast.LENGTH_SHORT).show();
    }

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        // Save the last known locations
        Log.d("Reverb","Pausing app and saving last known location");
        mEditor.putFloat("LAST_KNOWN_LAT", (float) Reverb.getInstance().getCurrentLocation().getLatitude());
        mEditor.putFloat("LAST_KNOWN_LONG", (float) Reverb.getInstance().getCurrentLocation().getLatitude());
        mEditor.commit();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_view_pager_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_anonymous:
                toggleAnonymity();
                return true;
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleAnonymity()
    {
        Toast.makeText(this, "Toggling Anonymity (but not actually)", Toast.LENGTH_SHORT).show();
    }

}
