package com.re.reverb.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;

import com.re.reverb.R;
import com.re.reverb.androidBackend.DummyFeed;
import com.re.reverb.androidBackend.Feed;
import com.re.reverb.androidBackend.Post;

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
		
		initFeedData();
	    FeedListViewAdapter adapter = new FeedListViewAdapter(getActivity(), posts);
		//final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
		//		android.R.layout.simple_list_item_1, list);
		elv.setAdapter(adapter);
		
		return theView;
	}
	
	
	  public void initFeedData() {
		  LinkedList<Post> postList = (LinkedList<Post>) dataFeed.getAllPosts();
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