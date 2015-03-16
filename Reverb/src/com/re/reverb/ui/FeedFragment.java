package com.re.reverb.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.feed.AbstractFeed;
import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.utils.GenericOverLay;
import com.re.reverb.androidBackend.utils.MessageOverlay;
import com.re.reverb.network.RequestQueueSingleton;


public abstract class FeedFragment extends OverlayFragment implements OnRefreshListener, OnFeedDataChangedListener, MessageOverlay
{
    AbstractFeed dataFeed;
    SwipeRefreshLayout swipeRefreshLayout;
    NewFeedListViewAdapter adapter;
    View currentOverlay;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        this.logoutEditOverlay = new GenericOverLay(this.getActivity());
        this.editUserInfoOverlay = new GenericOverLay(this.getActivity());
        super.onCreate(savedInstanceState);
    }

    protected View setupDataFeed(View rootView, AbstractFeed dataFeed)
    {
        this.dataFeed = dataFeed;
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.reverb_blue_1,
                R.color.reverb_blue_2,
                R.color.reverb_blue_3,
                R.color.reverb_blue_4);
        ExpandableListView elv = (ExpandableListView) rootView.findViewById(R.id.newFeedListView);
        elv.setGroupIndicator(null);

        RequestQueueSingleton.getInstance(getActivity().getApplicationContext());


        adapter = new NewFeedListViewAdapter(getActivity(), dataFeed, this);
        elv.setAdapter(adapter);
        elv.setOnScrollListener(new FeedScrollListener());

        dataFeed.setOnDataChangedListener(this);

        return rootView;
    }


    @Override
    public void onDataChanged()
    {
        adapter.notifyDataSetChanged();
    }


    protected class FeedScrollListener implements AbsListView.OnScrollListener
    {
        private int preLastItem = -2;

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
                if (preLastItem != view.getLastVisiblePosition() || view.getLastVisiblePosition() == -1)
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
                    } catch (NotSignedInException e)
                    {
                        Toast.makeText(getActivity(), R.string.not_signed_in_message, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Log.d("Reverb","Scrolled to the bottom");
                    preLastItem = view.getLastVisiblePosition();
                }
            }

        }
    }

    public abstract void onOpenOverlayClick(final int messageId, final Post post);

    public View displayOverlay(int layoutResource, int containerId)
    {
        removeOverlays();
        LayoutInflater vi = (LayoutInflater) this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(layoutResource, null);
        this.currentOverlay = v;

        FrameLayout myLayout = (FrameLayout) this.getActivity().findViewById(containerId);
        myLayout.addView(v);
        return v;

    }

    public void removeOverlays()
    {
        if(this.currentOverlay != null)
        {
            ViewGroup parent = (ViewGroup) this.currentOverlay.getParent();
            parent.removeView(this.currentOverlay);
            this.currentOverlay = null;
        }
    }

    protected abstract void extraAnonymousUISetup();

    public void switchUIToAnonymous()
    {
        extraAnonymousUISetup();
        if(adapter != null)
        {
            adapter.switchUIToAnonymous();
        }
    }

    protected abstract void extraPublicUISetup();

    public void switchUIToPublic()
    {
        extraPublicUISetup();
        if(adapter != null)
        {
            adapter.switchUIToPublic();
        }
    }
}

