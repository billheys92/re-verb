package com.re.reverb.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.TableLayout;

import com.re.reverb.R;
import com.re.reverb.androidBackend.DummyNetworkFeed;
import com.re.reverb.androidBackend.Feed;
import com.re.reverb.androidBackend.NewPostFeed;
import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.ParentPost;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulFeedIncrementException;
import com.re.reverb.network.RequestQueueSingleton;

import java.util.ArrayList;

public class NewFeedFragment extends Fragment implements OnRefreshListener, OnFeedDataChangedListener
{
    ArrayList<ParentPost> posts = new ArrayList<ParentPost>();
    NewPostFeed dataFeed;
    SwipeRefreshLayout swipeRefreshLayout;
    NewFeedListViewAdapter adapter;

    public NewFeedFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        dataFeed = new NewPostFeed(this.getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.fragment_new_main_feed, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.reverb_blue_1,
                R.color.reverb_blue_2,
                R.color.reverb_blue_3,
                R.color.reverb_blue_4);
        ExpandableListView elv = (ExpandableListView) rootView.findViewById(R.id.newFeedListView);
        elv.setGroupIndicator(null);

        RequestQueueSingleton.getInstance(getActivity().getApplicationContext());


        adapter = new NewFeedListViewAdapter(getActivity(), dataFeed);
        elv.setAdapter(adapter);
        elv.setOnScrollListener(new FeedScrollListener());

        dataFeed.setOnDataChangedListener(this);

        return rootView;
    }

    @Override
    public void onDataChanged()
    {

    }

    @Override
    public void onRefresh()
    {

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
