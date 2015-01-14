package com.re.reverb.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;

import com.re.reverb.R;
import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.feed.DummyNetworkFeed;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;

/**
 * Created by Bill on 2014-09-24.
 */
public class PostListFragment extends Fragment implements OnFeedDataChangedListener {

    SparseArray<Post> posts = new SparseArray<Post>();
    Feed dataFeed = new DummyNetworkFeed();
    FeedListViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.post_list_fragment, container, false);
        ExpandableListView elv = (ExpandableListView) rootView.findViewById(R.id.postListView);

        adapter = new FeedListViewAdapter(getActivity(), dataFeed);
        elv.setAdapter(adapter);
        elv.setOnScrollListener(new FeedScrollListener());

        dataFeed.setOnDataChangedListener(this);
        return rootView;
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

                } catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d("Reverb", "Scrolled to the bottom of posts list");
            }

        }
    }
}
