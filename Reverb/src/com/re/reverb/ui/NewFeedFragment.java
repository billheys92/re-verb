package com.re.reverb.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.feed.NewPostFeed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.dto.PostActionDto;
import com.re.reverb.network.PostManagerImpl;

public class NewFeedFragment extends FeedFragment
{

    ImageButton createPostButton;

    public NewFeedFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_new_main_feed, container, false);

        createPostButton = (ImageButton) rootView.findViewById(R.id.buttonPost);
        ((ReverbActivity) getActivity()).setupUIBasedOnAnonymity(Reverb.getInstance().isAnonymous());
        return setupDataFeed(rootView, new NewPostFeed(this.getActivity().getApplicationContext()));
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
        } catch (NotSignedInException e)
        {
            Toast.makeText(getActivity(), R.string.not_signed_in_message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onOpenOverlayClick(final int messageId, final Post post)
    {
        final Activity activity = this.getActivity();
        displayOverlay(R.layout.overlay_more_options_main, R.id.overlayMainFeedLayoutContainer);
        RelativeLayout reportPostRow = (RelativeLayout)activity.findViewById(R.id.reportPostRow);
        View.OnClickListener listener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    PostActionDto postActionDto = new PostActionDto(messageId, Reverb.getInstance().getCurrentUserId());
                    PostManagerImpl.submitReportPost(postActionDto, activity);
                } catch (NotSignedInException e)
                {
                    e.printStackTrace();
                }
                removeOverlays();
            }
        };
        reportPostRow.setOnClickListener(listener);

        View.OnClickListener exitListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                removeOverlays();
            }
        };
        (activity.findViewById(R.id.mainFeedOverlayLayout)).setOnClickListener(exitListener);
    }

    @Override
    public void onOpenLogoutEditOverlayClick()
    {
        standardOnOpenLogoutEditOverlayClick(R.id.overlayMainFeedLayoutContainer);
    }

    @Override
    public void onEditUserInfoOverlayClick()
    {
        standardOnEditUserInfoOverlayClick(R.id.overlayMainFeedLayoutContainer);
    }

    protected void extraAnonymousUISetup()
    {
        if (createPostButton != null)
        {
            createPostButton.setImageResource(R.drawable.compose_image_dark);
        }
    }

    public void extraPublicUISetup()
    {
        if (createPostButton != null)
        {
            createPostButton.setImageResource(R.drawable.compose_image);
        }
    }
}
