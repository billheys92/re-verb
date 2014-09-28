package com.re.reverb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

import com.re.reverb.R;

public class MainViewPagerActivity extends FragmentActivity
{
	
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
    }
    
    public class MainViewPagerAdapter extends FragmentPagerAdapter {
    	 
    	public MainViewPagerAdapter(FragmentManager fm) {
    	     super(fm);
    	 }
    	 
    	 @Override
    	 public Fragment getItem(int i) {
    	     switch(i){
    	     	case 0: return new UserProfileFragment();
    	     	case 1: return new FeedFragment();
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

    public void startCreatePostActivity(View view){
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }
    
    
}
