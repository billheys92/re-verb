package com.re.reverb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.re.reverb.R;
import com.re.reverb.androidBackend.AvailableRegionsUpdateRegion;
import com.re.reverb.androidBackend.Reverb;

import java.util.ArrayList;
import java.util.Collections;

public class RegionsFragment extends ListFragment implements AvailableRegionsUpdateRegion
{

    private static View view;
    private ArrayList<String> nearbyRegionNames;
    private ArrayList<String> subscribedRegionNames;
    private ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        this.nearbyRegionNames = new ArrayList<String>();
        this.subscribedRegionNames = new ArrayList<String>();
        Reverb.attachAvailableRegionsUpdateListener(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_regions, container, false);
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nearbyRegionNames);
            setListAdapter(adapter);
        } catch (InflateException e) {
            /* fragment is already there, just return view as it is */
        }

        return view;
	}


    @Override
    public void onAvailableRegionsUpdated()
    {
        this.nearbyRegionNames.clear();
        for(String name: Reverb.getInstance().getRegionManager().getNearbyRegionNames()) {
            this.nearbyRegionNames.add(name);
        }
//        Collections.copy(this.nearbyRegionNames, Reverb.getInstance().getRegionManager().getNearbyRegionNames());
//        this.subscribedRegionNames = Reverb.getInstance().getRegionManager().getSubscribedRegionNames();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id)
    {
        super.onListItemClick(parent, v, position, id);
        Intent intent = new Intent(this.getActivity(), CreateRegionActivity.class);
        intent.putExtra("SELECTED_REGION_ID", position);
        startActivity(intent);
    }
}
