package com.re.reverb.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
import com.re.reverb.network.RequestQueueSingleton;

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
        //((ImageView) convertView.findViewById(R.id.profilePicture)).setImageBitmap(postContent.getProfilePicture());
        netProfilePicture.setImageUrl(postContent.getProfilePictureURL(), RequestQueueSingleton.getInstance().getImageLoader());
        //((ImageView) convertView.findViewById(R.id.profilePicture)).setImageBitmap(postContent.getProfilePicture());
        ((TextView) convertView.findViewById(R.id.postBody)).setText(postContent.getPostBody());
        ((TextView) convertView.findViewById(R.id.username)).setText(postContent.getUsername());
        ((TextView) convertView.findViewById(R.id.handle)).setText(postContent.getHandle());

        NetworkImageView netMessageImage = (NetworkImageView) convertView.findViewById(R.id.messageImage);
        netMessageImage.setImageUrl(postContent.getMessageImage(), RequestQueueSingleton.getInstance().getImageLoader());
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
    public View getGroupView(int groupPosition,
                             boolean isExpanded,
                             View convertView,
                             ViewGroup parent)
    {
        final ParentPost parentPost = getGroup(groupPosition);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.parent_post_row, null);
        }
        StandardPostContent postContent = (StandardPostContent) parentPost.getContent();

        NetworkImageView netProfilePicture = (NetworkImageView) convertView.findViewById(R.id.profilePicture);
        netProfilePicture.setDefaultImageResId(R.drawable.anonymous_pp);
        netProfilePicture.setImageUrl(postContent.getProfilePictureURL(), RequestQueueSingleton.getInstance().getImageLoader());

        NetworkImageView netMessageImage = (NetworkImageView) convertView.findViewById(R.id.messageImage);
        netMessageImage.setImageUrl(postContent.getMessageImage(), RequestQueueSingleton.getInstance().getImageLoader());

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
                    System.out.println("Reply to post: " + parentPost.getPostId());
                }
                else
                {
                    System.out.println("Wrong activity for reply icon");
                }
            }
        });

        ((ImageView) convertView.findViewById(R.id.voteIcon)).setImageResource(R.drawable.votes_icon);
        ((TextView) convertView.findViewById(R.id.voteCount)).setText(postContent.getNumVotes().toString());
        ((TextView) convertView.findViewById(R.id.postBody)).setText(postContent.getPostBody());
        ((TextView) convertView.findViewById(R.id.username)).setText(postContent.getUsername());
        ((TextView) convertView.findViewById(R.id.handle)).setText(postContent.getHandle());
        ((TextView) convertView.findViewById(R.id.timeNumber)).setText(parentPost.getTimeCreated().toString());
        ((TextView) convertView.findViewById(R.id.timeLetter)).setText("format am pm");

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
        return false;
    }

} 