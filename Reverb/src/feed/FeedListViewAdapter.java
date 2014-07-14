package feed;

import java.util.ArrayList;

import com.re.reverb.R;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedListViewAdapter extends BaseExpandableListAdapter
{

	private final ArrayList<Message> messages;
	public LayoutInflater inflater;
	public Activity activity;

	public FeedListViewAdapter(Activity act, ArrayList<Message> messages)
	{
		activity = act;
		this.messages = messages;
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition)
	{
		return messages.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent)
	{
		final MessageReply child = (MessageReply) getChild(groupPosition, childPosition);
		TextView text = null;
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.message_child, null);
		}

		((TextView) convertView.findViewById(R.id.userName)).setText(child.getUserName());
		((TextView) convertView.findViewById(R.id.handle)).setText(child.getHandle());
		((TextView) convertView.findViewById(R.id.body)).setText(child.getBody());
		
		convertView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Toast.makeText(activity, child, Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		return messages.get(groupPosition).getChildren().size();
	}

	@Override
	public Object getGroup(int groupPosition)
	{
		return messages.get(groupPosition);
	}

	@Override
	public int getGroupCount()
	{
		return messages.size();
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
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.message_parent, null);
		}
		Message message = (Message) getGroup(groupPosition);
		((TextView) convertView.findViewById(R.id.userName)).setText(message.getUserName());
		((TextView) convertView.findViewById(R.id.handle)).setText(message.getHandle());
		((TextView) convertView.findViewById(R.id.body)).setText(message.getBody());
		return convertView;
	}

	@Override
	public boolean hasStableIds()
	{
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition)
	{
		return false;
	}
}