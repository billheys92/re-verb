package com.re.reverb.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.DummyFeed;
import com.re.reverb.androidBackend.Feed;
import com.re.reverb.androidBackend.Post;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public class MainFeedFragment extends Fragment
{
	SparseArray<Post> posts = new SparseArray<Post>();
	Feed dataFeed = new DummyFeed();
		
	public MainFeedFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View theView = inflater.inflate(R.layout.fragment_main_feed, container, false);
		
		ExpandableListView elv = (ExpandableListView) theView.findViewById(R.id.feedListView);
		
		
		putFeedDataInAdapter();
	    FeedListViewAdapter adapter = new FeedListViewAdapter(getActivity(), posts);
		//final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
		//		android.R.layout.simple_list_item_1, list);
		elv.setAdapter(adapter);
		elv.setOnScrollListener(new AbsListView.OnScrollListener()
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
						posts.append(posts.size(), dataFeed.getAllPosts().peek());
						((BaseAdapter) view.getAdapter()).notifyDataSetChanged();
						
					} catch (UnsuccessfulFeedIncrementException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsuccessfulRefreshException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.d("Reverb","Scrolled to the bottom");
				}
				
			}
		});
		
		return theView;
	}
	
	
	  public void putFeedDataInAdapter() {
		  Stack<Post> postList = new Stack<Post>();
		try
		{
			postList = (Stack<Post>) dataFeed.getAllPosts();
		} catch (UnsuccessfulRefreshException e)
		{
			Toast.makeText(getActivity(), R.string.refresh_error_toast_message, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		  for (int j = 0; j < postList.size(); j++) {
			  posts.append(j, postList.get(j));
		  }
	  }
}

class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
    }

    @Override
    public long getItemId(int position) {
      String item = getItem(position);
      return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

}

