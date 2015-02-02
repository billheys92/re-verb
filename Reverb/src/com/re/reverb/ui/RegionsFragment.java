package com.re.reverb.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.re.reverb.R;
import com.re.reverb.androidBackend.AvailableRegionsUpdateRegion;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionImageUrlFactory;
import com.re.reverb.network.RequestQueueSingleton;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class RegionsFragment extends ListFragment implements AvailableRegionsUpdateRegion
{

    private static enum TabType {
        SUBSCRIBED,
        NEARBY
    }

    private static View view;
    private ArrayList<Region> nearbyRegionNames;
    private ArrayAdapter<Region> adapter;
    private TabType selectedTab = TabType.SUBSCRIBED;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        this.nearbyRegionNames = new ArrayList<Region>();
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
            adapter = new RegionsArrayAdapter(getActivity(), nearbyRegionNames);
            setListAdapter(adapter);


            final Button subscribedTab = (Button)view.findViewById(R.id.subscribedRegionsTabButton);
            final Button nearbyTab = (Button)view.findViewById(R.id.nearbyRegionsTabButton);
            subscribedTab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(selectedTab != TabType.SUBSCRIBED) {
                        selectedTab = TabType.SUBSCRIBED;
                        subscribedTab.setBackgroundResource(R.drawable.selected_tab);
                        nearbyTab.setBackgroundColor(getResources().getColor(R.color.reverb_blue_1));
                    }
                }
            });
            nearbyTab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(selectedTab != TabType.NEARBY) {
                        selectedTab = TabType.NEARBY;
                        nearbyTab.setBackgroundResource(R.drawable.selected_tab);
                        subscribedTab.setBackgroundColor(getResources().getColor(R.color.reverb_blue_1));
                    }
                }
            });
        } catch (InflateException e) {
            e.printStackTrace();
        }

        return view;
	}


    @Override
    public void onAvailableRegionsUpdated()
    {
        this.nearbyRegionNames.clear();
        for(Region region: Reverb.getInstance().getRegionManager().getNearbyRegions()) {
            this.nearbyRegionNames.add(region);
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

    private class RegionsArrayAdapter extends ArrayAdapter<Region> {
        private final Context context;
        private final ArrayList<Region> values;

        public RegionsArrayAdapter(Context context, ArrayList<Region> values) {
            super(context, R.layout.region_list_row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.region_list_row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.regionName);
            NetworkImageView imageView = (NetworkImageView) rowView.findViewById(R.id.regionThumbnail);
            textView.setText(values.get(position).getName());
            imageView.setDefaultImageResId(R.drawable.anonymous_pp);
            imageView.setImageUrl(RegionImageUrlFactory.createFromRegion(values.get(position)).toString(), RequestQueueSingleton.getInstance().getImageLoader());
            final ImageView toggleSubscribedImage = (ImageView) rowView.findViewById(R.id.subscribeToRegionToggleButton);
            toggleSubscribedImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    toggleSubscribedImage.setImageDrawable(getResources().getDrawable( R.drawable.checkmark ));
                }
            });
            return rowView;
        }
    }
}
