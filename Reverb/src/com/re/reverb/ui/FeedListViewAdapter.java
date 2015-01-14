package com.re.reverb.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.re.reverb.R;
import com.re.reverb.androidBackend.feed.Feed;
import com.re.reverb.androidBackend.post.Post;
import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public class FeedListViewAdapter extends BaseExpandableListAdapter {

	private Feed feed;
	public LayoutInflater inflater;
	public Activity activity;

	public FeedListViewAdapter(Activity act, Feed feed) {
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
	public Object getChild(int groupPosition, int childPosition) {
        try {
            Post p = (Post) feed.getPosts().get(groupPosition);
            String child = p.getPostPropertyAtIndex(childPosition);
            return child;
        } catch (NullPointerException e) {
            return null;
        }
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final String children = (String) getChild(groupPosition, childPosition);
		TextView text = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_details, null);
		}
		text = (TextView) convertView.findViewById(R.id.textView1);
		text.setText(children);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(activity, children,
						Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
        try {
            return ((Post) feed.getPosts().get(groupPosition)).getNumProperties();
        } catch (NullPointerException e) {
            return 0;
        }
	}

	@Override
	public Object getGroup(int groupPosition) {
        try {
            return feed.getPosts().get(groupPosition);
        } catch (NullPointerException e) {
            return null;
        }
	}

	@Override
	public int getGroupCount() {
        try {
            return feed.getPosts().size();
        } catch (NullPointerException e) {
            return 0;
        }

	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_group, null);
		}
		Post post = (Post) getGroup(groupPosition);
		String postData = "Error: Blank text";
		try
		{
			postData = (String)post.getContent().getPostData();
		} catch (EmptyPostException e)
		{
			e.printStackTrace();
		}
		((CheckedTextView) convertView).setText(postData);
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
} 