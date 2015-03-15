package com.re.reverb.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.re.reverb.R;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionChangeListener;

public class MainViewPagerActivity extends ReverbActivity implements RegionChangeListener
{

    static final int CREATE_POST_REQUEST = 1;  // The request code for creating a post activity
    static final int CREATE_REPLY_POST_REQUEST = 3;
    static final int SELECT_PHOTO = 100;

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

        ViewPager.OnPageChangeListener mViewPageChangeListener = new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position,
                                       float positionOffset,
                                       int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                mPagerAdapter.setCurrentFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        };

        Reverb.getInstance().attachRegionChangedListener(this);
        mViewPager.setOnPageChangeListener(mViewPageChangeListener);
    }

    public void returnToDefaultPage() {
        mViewPager.setCurrentItem(defaultPage);
    }

    @Override
    public void onRegionChanged(Region region)
    {
        if(region != null && region.getName() != null)
        {
            setActionBarTitle("re: " + region.getName().toLowerCase());
        }
    }

    public class MainViewPagerAdapter extends FragmentPagerAdapter
    {
    	private int currentFragment = 1;

        public void setCurrentFragment(int currentFragment)
        {
            this.currentFragment = currentFragment;
        }

        public int getCurrentFragment()
        {
            return currentFragment;
        }

    	public MainViewPagerAdapter(FragmentManager fm)
        {
    	     super(fm);
    	}

    	@Override
    	public OverlayFragment getItem(int i) {
    	    switch(i){
    	    	case 0:
                    return userProfileFragment;
    	    	case 1:
                    return newFeedFragment;
    	    	case 2:
                    return regionsFragment;
    	    	default: //TODO throw an error
    	   	 	return null;
    	    }
    	}

    	 @Override
    	 public int getCount() {
    	     return NUM_PAGES;
    	 }
    }

    @Override
    public OverlayFragment getCurrentFragmentOverlay()
    {
        return mPagerAdapter.getItem(mPagerAdapter.getCurrentFragment());
    }

    @Override
    protected void switchUIToAnonymousMode()
    {
        newFeedFragment.switchUIToAnonymous();
        userProfileFragment.switchUIToAnonymous();
        regionsFragment.switchUIToAnonymous();
    }

    @Override
    protected void switchUIToPublicMode()
    {
        newFeedFragment.switchUIToPublic();
        userProfileFragment.switchUIToPublic();
        regionsFragment.switchUIToPublic();
    }

    public void startCreatePostActivity(View view){
        boolean canWriteToRegion = Reverb.getInstance().getRegionManager().insideRegion(Reverb.getInstance().getRegionManager().getCurrentRegion().getRegionId());
        if(canWriteToRegion)
        {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivityForResult(intent, CREATE_POST_REQUEST);
        }
        else
        {
            Toast.makeText(this, "You must be inside a region to post to it!", Toast.LENGTH_SHORT).show();
        }
    }

    public void startCreateReplyPostActivity(View view, int postId)
    {
        boolean canWriteToRegion = Reverb.getInstance().getRegionManager().insideRegion(Reverb.getInstance().getRegionManager().getCurrentRegion().getRegionId());
        if(canWriteToRegion)
        {

            Intent intent = new Intent(this, CreateReplyPostActivity.class);
            intent.putExtra(CreateReplyPostActivity.POST_ID_EXTRA, postId);
            startActivityForResult(intent, CREATE_REPLY_POST_REQUEST);
        }
        else
        {
            Toast.makeText(this, "You must be inside a region to post to it!", Toast.LENGTH_SHORT).show();
        }
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
            case SELECT_PHOTO:
                switch (resultCode) {
                    case Activity.RESULT_OK :
                        userProfileFragment.onActivityResult(requestCode, resultCode, data);
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

    public void updateUserInfo()
    {
        userProfileFragment.updateUserInfo();
    }
}
