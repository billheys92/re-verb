package com.re.reverb.ui;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.re.reverb.R;

public class MainViewPagerActivity extends FragmentActivity implements LocationListener
{
	
	private String latitude;
	private String longitude;
	private LocationManager locationManager;
	private String provider;	
	
	static final int NUM_PAGES = 3;
	private int currentPage = 1;
	
	MainViewPagerAdapter mPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentPage);
        // Get the location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // Define the criteria how to select the location provider -> use
	    // default
	    Criteria criteria = new Criteria();
	    provider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(provider);

	    // Initialize the location fields
	    if (location != null) 
	    {
	      System.out.println("Provider " + provider + " has been selected.");
	      onLocationChanged(location);
	    } 
	    else 
	    {
	      latitude = "Location not available";
	      longitude = "Location not available";
	      System.out.println(latitude);
	      System.out.println(longitude);
	    }
    }
    
    public class MainViewPagerAdapter extends FragmentPagerAdapter {
    	 
    	public MainViewPagerAdapter(FragmentManager fm) {
    	     super(fm);
    	 }
    	 
    	 @Override
    	 public Fragment getItem(int i) {
    	     switch(i){
    	     	case 0: return new UserProfileFragment();
    	     	case 1: return new MainFeedFragment();
    	     	case 2: return new RegionsFragment();
    	     	default: //TODO throw an error
    	    	 	return null;
    	     }
    	 }

    	 @Override
    	 public int getCount() {
    	     return NUM_PAGES;
    	 }

    }

    public void sendMessage(View view){
    	System.out.println("Message Sent");
    }
    
	@Override
	public void onLocationChanged(Location location) {
	    double lat = (double) (location.getLatitude());
	    double lng = (double) (location.getLongitude());
	    latitude = String.valueOf(lat);
	    longitude = String.valueOf(lng);
	    System.out.println("Latitute:" + latitude);
	    System.out.println("Longitute:" + longitude);
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		System.out.println("Enabled new provider " + provider);
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		System.out.println("Disabled provider " + provider);
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
