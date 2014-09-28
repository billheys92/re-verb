package com.re.reverb.ui;

import java.util.ArrayList;

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
import com.re.reverb.androidBackend.Feed;
import com.re.reverb.androidBackend.Post;
import com.re.reverb.androidBackend.errorHandling.EmptyPostException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;

public class FeedListViewAdapter extends BaseExpandableListAdapter {

	private Feed feed;
	private ArrayList<Post> posts = new ArrayList<Post>();
	public LayoutInflater inflater;
	public Activity activity;

	public FeedListViewAdapter(Activity act, Feed feed) {
		activity = act;
		try
		{
			this.posts = feed.getPosts();
		} catch (UnsuccessfulRefreshException e)
		{
			Toast.makeText(activity, R.string.refresh_error_toast_message, Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		Post p = posts.get(groupPosition);
		String child = p.getPostPropertyAtIndex(childPosition);
		return child;
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
		return posts.get(groupPosition).getNumProperties();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return posts.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return posts.size();
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
			postData = (String)post.getPostContent().getPostData();
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