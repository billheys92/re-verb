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

public class MainViewPagerActivity extends ReverbActivity
{

    static final int CREATE_POST_REQUEST = 1;  // The request code for creating a post activity
    static final int CREATE_REPLY_POST_REQUEST = 3;

	static final int NUM_PAGES = 3;
	private int defaultPage = 1;
	
	MainViewPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode) {

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

    public NewFeedFragment getNewFeedFragment()
    {
        return newFeedFragment;
    }
}
