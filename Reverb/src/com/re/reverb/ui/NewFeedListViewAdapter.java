package com.re.reverb.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.re.reverb.R;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.feed.NewPostFeed;
import com.re.reverb.androidBackend.post.ChildPost;
import com.re.reverb.androidBackend.post.ParentPost;
import com.re.reverb.androidBackend.post.content.StandardPostContent;
import com.re.reverb.network.PostManagerImpl;
import com.re.reverb.network.RequestQueueSingleton;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NewFeedListViewAdapter extends BaseExpandableListAdapter
{

    private NewPostFeed feed;
    public LayoutInflater inflater;
    public Activity activity;

    public NewFeedListViewAdapter(Activity act,
                                  NewPostFeed feed)
    {
        activity = act;
        try
        {
            this.feed = feed;
            this.feed.init();
        } catch (UnsuccessfulRefreshException e)
        {
            Toast.makeText(activity, R.string.refresh_error_toast_message, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        inflater = act.getLayoutInflater();
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    @Override
    public ChildPost getChild(int groupPosition,
                              int childPosition)
    {
        try
        {
            ParentPost p = feed.getPosts().get(groupPosition);
            return p.getChildPosts().get(childPosition);
        } catch (NullPointerException e)
        {
            return null;
        }
    }

    @Override
    public long getChildId(int groupPosition,
                           int childPosition)
    {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition,
                             final int childPosition,
                             boolean isLastChild,
                             View convertView,
                             ViewGroup parent)
    {
        final ChildPost child = getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.child_post_row, null);
        }
        StandardPostContent postContent = (StandardPostContent) child.getContent();

        NetworkImageView netProfilePicture = (NetworkImageView) convertView.findViewById(R.id.profilePicture);
        netProfilePicture.setDefaultImageResId(R.drawable.anonymous_pp);
        netProfilePicture.setImageUrl(postContent.getProfilePictureURL(), RequestQueueSingleton.getInstance().getImageLoader());
        ((TextView) convertView.findViewById(R.id.postMessage)).setText(postContent.getPostBody());
        ((TextView) convertView.findViewById(R.id.username)).setText(postContent.getUsername());
        ((TextView) convertView.findViewById(R.id.handle)).setText(postContent.getHandle());

        ((ImageView) convertView.findViewById(R.id.voteIcon)).setImageResource(R.drawable.votes_icon);
        ((TextView) convertView.findViewById(R.id.voteCount)).setText(postContent.getNumVotes().toString());
        ((ImageView) convertView.findViewById(R.id.moreIcon)).setImageResource(R.mipmap.more_icon);

        NetworkImageView netMessageImage = (NetworkImageView) convertView.findViewById(R.id.messageImage);
        netMessageImage.setImageUrl(postContent.getMessageImage(), RequestQueueSingleton.getInstance().getImageLoader());
        Calendar now = GregorianCalendar.getInstance();
        now.setTime(new Date());

        Calendar then = GregorianCalendar.getInstance();
        then.setTime(child.getTimeCreated());

        boolean sameDay = now.get(Calendar.YEAR) == then.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR);

        if(sameDay)
        {
            String hourOfDay = Integer.toString(then.get(Calendar.HOUR));
            String minuteOfDay = Integer.toString(then.get(Calendar.MINUTE));
            if(minuteOfDay.length() < 2) minuteOfDay += "0";

            ((TextView) convertView.findViewById(R.id.timeNumber)).setText(hourOfDay + ":" + minuteOfDay);

            String amPm = then.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            ((TextView) convertView.findViewById(R.id.timeLetter)).setText(amPm);

        }
        else
        {
            String month = getMonthForInt(then.get(Calendar.MONTH)).substring(0,3);
            String day = Integer.toString(then.get(Calendar.DAY_OF_MONTH));

            ((TextView) convertView.findViewById(R.id.timeNumber)).setText(month + " " + day);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        try
        {
            return feed.getPosts().get(groupPosition).getChildPosts().size();
        } catch (NullPointerException e)
        {
            return 0;
        }
    }

    @Override
    public ParentPost getGroup(int groupPosition)
    {
        try
        {
            return feed.getPosts().get(groupPosition);
        } catch (NullPointerException e)
        {
            return null;
        }
    }

    @Override
    public int getGroupCount()
    {
        try
        {
            return feed.getPosts().size();
        } catch (NullPointerException e)
        {
            return 0;
        }

    }

    @Override
    public void onGroupCollapsed(int groupPosition)
    {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return 0;
    }

    @Override
    public View getGroupView(final int groupPosition,
                             final boolean isExpanded,
                             View convertView,
                             final ViewGroup parent)
    {
        final ParentPost parentPost = getGroup(groupPosition);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.parent_post_row, null);
        }

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isExpanded)
                {
                    if (activity instanceof MainViewPagerActivity)
                    {
                        PostManagerImpl.getPostReplies(feed, parentPost, (ExpandableListView) parent, groupPosition);
                        System.out.println("Get Replies for: " + parentPost.getPostId());
                    } else
                    {
                        System.out.println("Wrong activity for get post replies");
                    }
                }
                else
                {
                    ((ExpandableListView) parent).collapseGroup(groupPosition);
                }
            }
        });

        StandardPostContent postContent = (StandardPostContent) parentPost.getContent();

        NetworkImageView netProfilePicture = (NetworkImageView) convertView.findViewById(R.id.profilePicture);
        netProfilePicture.setDefaultImageResId(R.drawable.anonymous_pp);
        netProfilePicture.setImageUrl(postContent.getProfilePictureURL(), RequestQueueSingleton.getInstance().getImageLoader());

        NetworkImageView netMessageImage = (NetworkImageView) convertView.findViewById(R.id.messageImage);
        netMessageImage.setImageUrl(postContent.getMessageImage(), RequestQueueSingleton.getInstance().getImageLoader());

        ((ImageView) convertView.findViewById(R.id.voteIcon)).setImageResource(R.drawable.votes_icon);
        ((TextView) convertView.findViewById(R.id.voteCount)).setText(postContent.getNumVotes().toString());

        final ImageView replyImage = (ImageView) convertView.findViewById(R.id.replyIcon);
        replyImage.setImageResource(R.drawable.reply_icon);
        replyImage.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(activity instanceof MainViewPagerActivity)
                {
                    ((MainViewPagerActivity) activity).startCreateReplyPostActivity(v, parentPost.getPostId());
                }
                else
                {
                    System.out.println("Wrong activity for reply icon");
                }
            }
        });

        ((ImageView) convertView.findViewById(R.id.repostIcon)).setImageResource(R.mipmap.repost_icon);

        final ImageView moreImage = ((ImageView) convertView.findViewById(R.id.moreIcon));
        moreImage.setImageResource(R.mipmap.more_icon);
        moreImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(activity instanceof MainViewPagerActivity)
                {
                    //((MainViewPagerActivity) activity).startCreateReplyPostActivity(v, parentPost.getPostId());
                }
                else
                {
                    System.out.println("Wrong activity for more icon");
                }
            }
        });


        ((TextView) convertView.findViewById(R.id.postMessage)).setText(postContent.getPostBody());
        ((TextView) convertView.findViewById(R.id.username)).setText(postContent.getUsername());
        ((TextView) convertView.findViewById(R.id.handle)).setText(postContent.getHandle());

        Calendar now = GregorianCalendar.getInstance();
        now.setTime(new Date());

        Calendar then = GregorianCalendar.getInstance();
        then.setTime(parentPost.getTimeCreated());

        boolean sameDay = now.get(Calendar.YEAR) == then.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR);

        if(sameDay)
        {
            String hourOfDay = Integer.toString(then.get(Calendar.HOUR));
            String minuteOfDay = Integer.toString(then.get(Calendar.MINUTE));
            if(minuteOfDay.length() < 2) minuteOfDay += "0";

            ((TextView) convertView.findViewById(R.id.timeNumber)).setText(hourOfDay + ":" + minuteOfDay);

            String amPm = then.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            ((TextView) convertView.findViewById(R.id.timeLetter)).setText(amPm);

        }
        else
        {
            String month = getMonthForInt(then.get(Calendar.MONTH)).substring(0,3);
            String day = Integer.toString(then.get(Calendar.DAY_OF_MONTH));

            ((TextView) convertView.findViewById(R.id.timeNumber)).setText(month + " " + day);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition,
                                     int childPosition)
    {
        return true;
    }

} 