package com.re.reverb.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.re.reverb.R;
import com.re.reverb.androidBackend.AvailableRegionsUpdateRegion;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionImageUrlFactory;
import com.re.reverb.network.RequestQueueSingleton;

import java.util.ArrayList;

public class RegionsFragment extends ListFragment implements AvailableRegionsUpdateRegion
{

    private static enum TabType {
        SUBSCRIBED,
        NEARBY
    }

    private static View view;
    private ArrayList<Region> regionsList;
    private ArrayAdapter<Region> adapter;
    private TabType selectedTab = TabType.SUBSCRIBED;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        this.regionsList = new ArrayList<Region>();
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
            adapter = new RegionsArrayAdapter(getActivity(), regionsList);
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
                        Reverb.notifyAvailableRegionsUpdateListeners();
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
                        Reverb.notifyAvailableRegionsUpdateListeners();
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
        this.regionsList.clear();
        ArrayList<Region> newList;
        if(this.selectedTab == TabType.NEARBY) {
            newList = Reverb.getInstance().getRegionManager().getNearbyRegions();
        }
        else
        {
            newList = Reverb.getInstance().getRegionManager().getSubscribedRegions();
        }

        for(Region region: newList) {
            this.regionsList.add(region);
        }
//        Collections.copy(this.regionsList, Reverb.getInstance().getRegionManager().getNearbyRegionNames());
//        this.subscribedRegionNames = Reverb.getInstance().getRegionManager().getSubscribedRegionNames();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView parent, View v, int position, long id)
    {
        super.onListItemClick(parent, v, position, id);
           v.setBackgroundResource(R.drawable.horizontal_reverb_themed_bg_gradient);
//        Intent intent = new Intent(this.getActivity(), CreateRegionActivity.class);
//        intent.putExtra("SELECTED_REGION_ID", position);
//        startActivity(intent);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Region selectedRegion = values.get(position);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.region_list_row, parent, false);
            rowView.setClickable(true);
            if(Reverb.getInstance().getRegionManager().getCurrentRegion().getRegionId() == selectedRegion.getRegionId()) {
                rowView.setBackgroundResource(R.drawable.horizontal_reverb_themed_bg_gradient);
            }
            rowView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   Reverb.getInstance().getRegionManager().setCurrentRegion(selectedRegion);
                    notifyDataSetChanged();
                }
            });
            TextView regionNameTextView = (TextView) rowView.findViewById(R.id.regionName);
            TextView regionDescriptionTextView = (TextView) rowView.findViewById(R.id.regionDescriptionTextView);
            NetworkImageView imageView = (NetworkImageView) rowView.findViewById(R.id.regionThumbnail);
            regionNameTextView.setText(selectedRegion.getName());
            regionDescriptionTextView.setText(selectedRegion.getDescription());
            imageView.setDefaultImageResId(R.drawable.anonymous_pp);
//            imageView.setImageUrl(RegionImageUrlFactory.createFromRegion(selectedRegion).toString(), RequestQueueSingleton.getInstance().getImageLoader());
            final ImageView toggleSubscribedImage = (ImageView) rowView.findViewById(R.id.subscribeToRegionToggleButton);
            if(!selectedRegion.canUnsubscribe()) {
                toggleSubscribedImage.setImageDrawable(null);
                toggleSubscribedImage.setClickable(false);
            }
            else if(selectedRegion.isSubscribedTo())
            {
                toggleSubscribedImage.setImageDrawable(getResources().getDrawable( R.drawable.checkmark ));
            }
            toggleSubscribedImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(!selectedRegion.isSubscribedTo())
                    {
                        Reverb.getInstance().getRegionManager().subscribeToRegion(selectedRegion);
                        toggleSubscribedImage.setImageDrawable(getResources().getDrawable( R.drawable.checkmark ));
                        Toast.makeText(getActivity(), "Subscribed to region "+selectedRegion.getName(), Toast.LENGTH_SHORT).show();
                    }
                    else if(selectedRegion.canUnsubscribe())
                    {
                        Reverb.getInstance().getRegionManager().unsubscribeFromRegion(selectedRegion);
                        toggleSubscribedImage.setImageDrawable(getResources().getDrawable( R.drawable.plus_sign ));
                        Toast.makeText(getActivity(), "Unsubscribed From region "+selectedRegion.getName(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ImageView regionDetailsButton = (ImageView)rowView.findViewById(R.id.regionViewInfoButton);
            regionDetailsButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //TODO: only fetch names and ids in the array of rgions call and then get
                    //TODO: more details in the next activity. This requires moving region static maps api call until later
                    Intent intent = new Intent(getActivity(), CreateRegionActivity.class);
                    intent.putExtra("SELECTED_REGION_ID", position);
                    startActivity(intent);
                }
            });
            return rowView;
        }
    }
}
