package com.re.reverb.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.feed.DummyNetworkFeed;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.network.RequestQueueSingleton;


public class FeedFragment extends Fragment implements OnRefreshListener, OnFeedDataChangedListener
{
	SparseArray<Post> posts = new SparseArray<Post>();
	Feed dataFeed = new DummyNetworkFeed();
	SwipeRefreshLayout swipeRefreshLayout;
	FeedListViewAdapter adapter;	

	public FeedFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			
		View rootView = inflater.inflate(R.layout.fragment_main_feed, container, false);
		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorScheme(R.color.reverb_blue_1, 
				R.color.reverb_blue_2, 
				R.color.reverb_blue_3, 
				R.color.reverb_blue_4);
		ExpandableListView elv = (ExpandableListView) rootView.findViewById(R.id.feedListView);
		
		RequestQueueSingleton.getInstance(getActivity().getApplicationContext());
		
		
	    adapter = new FeedListViewAdapter(getActivity(), dataFeed);
		elv.setAdapter(adapter);
		elv.setOnScrollListener(new FeedScrollListener());

        dataFeed.setOnDataChangedListener(this);

		return rootView;
	}
	

	@Override
	public void onRefresh()
	{
		try
		{
			dataFeed.refreshPosts();
            adapter.notifyDataSetChanged();
		} catch (UnsuccessfulRefreshException e)
		{
			Toast.makeText(getActivity(), R.string.refresh_error_toast_message, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
        new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 3000);
	}

    @Override
    public void onDataChanged() {
        adapter.notifyDataSetChanged();
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
					if(dataFeed.fetchMore()){
                        ((BaseAdapter) view.getAdapter()).notifyDataSetChanged();
                    }

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

