package com.re.reverb.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.re.reverb.R;

public class MainFeedFragment extends Fragment
{
	SparseArray<Message> messages = new SparseArray<Message>();
		
	public MainFeedFragment(){}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View theView = inflater.inflate(R.layout.fragment_main_feed, container, false);
		
		ExpandableListView elv = (ExpandableListView) theView.findViewById(R.id.feedListView);
		
		/*
	    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
	        "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
	        "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
	        "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
	        "Android", "iPhone", "WindowsMobile" };

	    final ArrayList<String> list = new ArrayList<String>();
	    
	    for (int i = 0; i < values.length; ++i) {
	      list.add(values[i]);
	    }*/
	    
		createData();
	    FeedListViewAdapter adapter = new FeedListViewAdapter(getActivity(), messages);
		//final StableArrayAdapter adapter = new StableArrayAdapter(getActivity(),
		//		android.R.layout.simple_list_item_1, list);
		elv.setAdapter(adapter);
		
		return theView;
	}
	
	
	  public void createData() {
		  for (int j = 0; j < 5; j++) {
			  Message message = new Message("Test " + j);
			  for (int i = 0; i < 5; i++) {
				  message.children.add("Sub Item" + i);
			  }
			  messages.append(j, message);
		  }
	  }
}

class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
    }

    @Override
    public long getItemId(int position) {
      String item = getItem(position);
      return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }

}