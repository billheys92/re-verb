package com.re.reverb.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.feed.NewPostFeed;
import com.re.reverb.androidBackend.OnFeedDataChangedListener;
import com.re.reverb.androidBackend.post.ParentPost;
import com.re.reverb.androidBackend.post.dto.PostActionDto;
import com.re.reverb.network.PostManagerImpl;
import com.re.reverb.network.RequestQueueSingleton;

import java.util.ArrayList;

public class NewFeedFragment extends Fragment implements OnRefreshListener, OnFeedDataChangedListener
{
    ArrayList<ParentPost> posts = new ArrayList<ParentPost>();
    NewPostFeed dataFeed;
    SwipeRefreshLayout swipeRefreshLayout;
    NewFeedListViewAdapter adapter;
    private View currentOverlay = null;

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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh()
    {
        try
        {
            ((MainViewPagerActivity) this.getActivity()).updateLocation();
            dataFeed.refreshPosts();
            adapter.notifyDataSetChanged();
        } catch (UnsuccessfulRefreshException e)
        {
            Toast.makeText(getActivity(), R.string.refresh_error_toast_message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
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

                } catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d("Reverb","Scrolled to the bottom");
            }

        }
    }

    //TODO: MORE INFO OVERLAY

    public void onMoreInfoClick(final int messageId)
    {
        //CHANGE TO OVERLAY VIEW
        final Activity activity = this.getActivity();
        displayOverlay(R.layout.overlay_more_options);
        TextView reportText = (TextView)activity.findViewById(R.id.reportPost);
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    PostActionDto postActionDto = new PostActionDto(messageId, Reverb.getInstance().getCurrentUserId());
                    PostManagerImpl.submitReportPost(postActionDto);
                } catch (NotSignedInException e)
                {
                    e.printStackTrace();
                }
                removeOverlays();
            }
        };
        reportText.setOnClickListener(listener);

        View.OnClickListener exitListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                removeOverlays();
            }
        };
        ((FrameLayout) activity.findViewById(R.id.mainFeedOverlayLayout)).setOnClickListener(exitListener);
    }

    private View displayOverlay(int resource)
    {
        removeOverlays();
        LayoutInflater vi = (LayoutInflater) this.getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(resource, null);
        this.currentOverlay = v;

        FrameLayout myLayout = (FrameLayout) this.getActivity().findViewById(R.id.overlayMainFeedLayoutContainer);
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
}
