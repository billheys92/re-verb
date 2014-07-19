package com.re.reverb.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;

import com.re.reverb.R;
import com.re.reverb.androidBackend.DummyNetworkFeed;
import com.re.reverb.androidBackend.Feed;
import com.re.reverb.androidBackend.Post;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.network.RequestQueueSingleton;

public class FeedFragment extends Fragment
{
	SparseArray<Post> posts = new SparseArray<Post>();
	Feed dataFeed = new DummyNetworkFeed();
		
	public FeedFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RequestQueueSingleton.getInstance(getActivity().getApplicationContext());
		
		View theView = inflater.inflate(R.layout.fragment_main_feed, container, false);
		
		ExpandableListView elv = (ExpandableListView) theView.findViewById(R.id.feedListView);
		
	    FeedListViewAdapter adapter = new FeedListViewAdapter(getActivity(), dataFeed);
		elv.setAdapter(adapter);
		elv.setOnScrollListener(new FeedScrollListener());
		
		dataFeed.setBaseAdapter((BaseAdapter)(elv.getAdapter()));
		
		return theView;
	}
	
	private class FeedScrollListener implements AbsListView.OnScrollListener
	{
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
		{
			if(view.getLastVisiblePosition() == view.getAdapter().getCount()-1)
			{
				try
				{
					dataFeed.incrementFeed();
					((BaseAdapter) view.getAdapter()).notifyDataSetChanged();
					
				} catch (UnsuccessfulFeedIncrementException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("Reverb","Scrolled to the bottom");
			}
			
		}
	}
	
	
}

