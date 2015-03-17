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
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.feed.AbstractFeed;
import com.re.reverb.androidBackend.feed.NewPostFeed;
import com.re.reverb.androidBackend.post.ChildPost;
import com.re.reverb.androidBackend.post.ParentPost;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.post.content.PostContent;
import com.re.reverb.androidBackend.post.content.StandardPostContent;
import com.re.reverb.androidBackend.post.dto.CreateRepostDto;
import com.re.reverb.androidBackend.post.dto.PostActionDto;
import com.re.reverb.network.PostManagerImpl;
import com.re.reverb.network.RequestQueueSingleton;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class NewFeedListViewAdapter extends BaseExpandableListAdapter
{

    private AbstractFeed feed;
    private FeedFragment feedFragment;
    public LayoutInflater inflater;
    public Activity activity;
    public int defaultProfPicResource;

    public NewFeedListViewAdapter(Activity act,
                                  AbstractFeed feed,
                                  FeedFragment feedFragment)
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
        if(Reverb.getInstance().isAnonymous())
        {
            defaultProfPicResource = R.drawable.anonymous_pp_dark;
        }
        else
        {
            defaultProfPicResource = R.drawable.anonymous_pp;
        }
        this.feedFragment = feedFragment;
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

        setSharedPostParameters(convertView, postContent, child.getPostId(), child);

        Calendar now = GregorianCalendar.getInstance();
        now.setTime(new Date());
        Calendar then = GregorianCalendar.getInstance();
        then.setTime(child.getTimeCreated());
        setPostTime(now, then, convertView);

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

        setSharedPostParameters(convertView, postContent, parentPost.getPostId(), parentPost);

        final ImageView replyImage = (ImageView) convertView.findViewById(R.id.replyIcon);
        replyImage.setImageResource(R.mipmap.reply_icon);
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
        ((TextView) convertView.findViewById(R.id.replyCount)).setText(parentPost.getNumReplys().toString().equals("0") ? "" : parentPost.getNumReplys().toString());

        final ImageView repostImage =  (ImageView) convertView.findViewById(R.id.repostIcon);
        final TextView repostCount = ((TextView) convertView.findViewById(R.id.repostCount));
        repostImage.setImageResource(R.mipmap.repost_icon);
        repostImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(activity instanceof MainViewPagerActivity)
                {
                        Location location = Reverb.getInstance().getCurrentLocation();
                        PostManagerImpl.submitRepost(new CreateRepostDto(parentPost.getUserId(),
                                parentPost.getPostId(),
                                Reverb.getInstance().isAnonymous(),
                                location.getLatitude(),
                                location.getLongitude(),
                                Reverb.getInstance().getRegionManager().getCurrentRegion().getRegionId(),
                                parentPost.getContent().getMessageString()), parentPost, repostCount);
                }
                else
                {
                    System.out.println("Wrong activity for Repost icon");
                }
            }
        });

        Calendar now = GregorianCalendar.getInstance();
        now.setTime(new Date());
        Calendar then = GregorianCalendar.getInstance();
        TimeZone temp = then.getTimeZone();
//        then.setTimeZone(TimeZone.getTimeZone("UTC"));
        then.setTime(parentPost.getTimeCreated());
//        then.setTimeZone(temp);
        setPostTime(now, then, convertView);

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


    private void setPostTime(Calendar now, Calendar then, View convertView)
    {
        boolean sameDay = now.get(Calendar.YEAR) == then.get(Calendar.YEAR) && now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR);

        if(sameDay)
        {
            String hourOfDay = Integer.toString(then.get(Calendar.HOUR));
            String minuteOfDay = Integer.toString(then.get(Calendar.MINUTE));
            Date time = then.getTime();
            if(minuteOfDay.length() < 2 ) minuteOfDay = "0" + minuteOfDay;

            ((TextView) convertView.findViewById(R.id.timeNumber)).setText(hourOfDay + ":" + minuteOfDay);

            String amPm = then.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            ((TextView) convertView.findViewById(R.id.timeLetter)).setText(amPm);

        }
        else
        {
            String month = getMonthForInt(then.get(Calendar.MONTH)).substring(0,3);
            String day = Integer.toString(then.get(Calendar.DAY_OF_MONTH));

            ((TextView) convertView.findViewById(R.id.timeNumber)).setText(month + " " + day);
            ((TextView) convertView.findViewById(R.id.timeLetter)).setText("");
        }
    }

    private void setSharedPostParameters(View convertView, final StandardPostContent postContent, final int postId, final Post post)
    {
        NetworkImageView netProfilePicture = (NetworkImageView) convertView.findViewById(R.id.profilePicture);
        netProfilePicture.setScaleType(ImageView.ScaleType.FIT_XY);
        netProfilePicture.setDefaultImageResId(defaultProfPicResource);
        if(postContent.getProfilePictureName() != null && !postContent.getProfilePictureName().equals("null") && !postContent.getProfilePictureName().equals(""))
        {
            netProfilePicture.setImageUrl(postContent.getProfilePictureURL(), RequestQueueSingleton.getInstance().getImageLoader());
        }
        else
        {
            netProfilePicture.setImageUrl(null,RequestQueueSingleton.getInstance().getImageLoader());
        }
        NetworkImageView netMessageImage = (NetworkImageView) convertView.findViewById(R.id.messageImage);
        if( postContent.getMessageImageName() != null && !postContent.getMessageImageName().equals("") && !postContent.getMessageImageName().equals("null"))
        {
            netMessageImage.setDefaultImageResId(R.drawable.default_image);
            netMessageImage.setImageUrl(postContent.getMessageImage(), RequestQueueSingleton.getInstance().getImageLoader());
        }
        else
        {
            netMessageImage.setDefaultImageResId(android.R.color.transparent);
            netMessageImage.setImageUrl(null,RequestQueueSingleton.getInstance().getImageLoader());
        }

        ((TextView) convertView.findViewById(R.id.postMessage)).setText(postContent.getPostBody());
        ((TextView) convertView.findViewById(R.id.username)).setText(postContent.getUsername());
        ((TextView) convertView.findViewById(R.id.handle)).setText(postContent.getHandle());

        final ImageView voteImage = ((ImageView) convertView.findViewById(R.id.voteIcon));
        voteImage.setImageResource(R.drawable.votes_icon);
        final TextView voteCount = ((TextView) convertView.findViewById(R.id.voteCount));
        voteImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(activity instanceof MainViewPagerActivity)
                {
                    try
                    {
                        PostManagerImpl.submitFavoritePost(new PostActionDto(postId, Reverb.getInstance().getCurrentUserId()), postContent, voteCount);
                    } catch (NotSignedInException e)
                    {
                        Toast.makeText(activity.getApplicationContext(), R.string.not_signed_in_message, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    System.out.println("Wrong activity for favorite icon");
                }
            }
        });
        voteCount.setText(postContent.getNumVotes().toString().equals("0") ? " " : postContent.getNumVotes().toString());

        final ImageView moreImage = ((ImageView) convertView.findViewById(R.id.moreIcon));
        moreImage.setImageResource(R.mipmap.more_icon);
        moreImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                feedFragment.onOpenOverlayClick(postId, post);
            }
        });
    }

    public void switchUIToAnonymous()
    {
        this.defaultProfPicResource = R.drawable.anonymous_pp_dark;
        notifyDataSetChanged();
    }

    public void switchUIToPublic()
    {
        this.defaultProfPicResource = R.drawable.anonymous_pp;
        notifyDataSetChanged();
    }
} 