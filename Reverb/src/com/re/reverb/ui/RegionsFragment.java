package com.re.reverb.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
import com.re.reverb.androidBackend.errorHandling.NotSignedInException;
import com.re.reverb.androidBackend.errorHandling.UnsuccessfulRefreshException;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionImageUrlFactory;
import com.re.reverb.androidBackend.utils.GenericOverLay;
import com.re.reverb.network.RequestQueueSingleton;

import java.util.ArrayList;

public class RegionsFragment extends OverlayFragment implements AvailableRegionsUpdateRegion, SwipeRefreshLayout.OnRefreshListener
{

    private static enum TabType {
        SUBSCRIBED,
        NEARBY
    }

    private static View view;
    private ArrayList<Region> regionsList;
    private ArrayAdapter<Region> adapter;
    private TabType selectedTab = TabType.SUBSCRIBED;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageButton createRegionButton;
    private Button subscribedTab;
    private Button nearbyTab;

    @Override
	public void onCreate(Bundle savedInstanceState)
	{
        this.logoutEditOverlay = new GenericOverLay(this.getActivity());
        this.editUserInfoOverlay = new GenericOverLay(this.getActivity());
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

            createRegionButton = (ImageButton) view.findViewById(R.id.createRegionButton);
            subscribedTab = (Button)view.findViewById(R.id.subscribedRegionsTabButton);
            nearbyTab = (Button)view.findViewById(R.id.nearbyRegionsTabButton);

            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorScheme(R.color.reverb_blue_1,
                    R.color.reverb_blue_2,
                    R.color.reverb_blue_3,
                    R.color.reverb_blue_4);

            subscribedTab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(selectedTab != TabType.SUBSCRIBED) {
                        selectedTab = TabType.SUBSCRIBED;
                        Reverb.notifyAvailableRegionsUpdateListeners();
                        setTabUI(Reverb.getInstance().isAnonymous());
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
                        setTabUI(Reverb.getInstance().isAnonymous());
                    }
                }
            });
        } catch (InflateException e) {
            e.printStackTrace();
        }
        ((ReverbActivity) getActivity()).setupUIBasedOnAnonymity(Reverb.getInstance().isAnonymous());

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
           v.setBackgroundColor(getResources().getColor(R.color.very_light_grey));
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
                rowView.setBackgroundColor(getResources().getColor(R.color.very_light_grey));
            }
            rowView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                   Reverb.getInstance().getRegionManager().setCurrentRegion(selectedRegion);
//                   ((ReverbActivity) getActivity()).setActionBarTitle(selectedRegion.getName());
                    notifyDataSetChanged();
                }
            });
            TextView regionNameTextView = (TextView) rowView.findViewById(R.id.regionName);
            TextView regionDescriptionTextView = (TextView) rowView.findViewById(R.id.regionDescriptionTextView);
            TextView regionStatsTextView = (TextView) rowView.findViewById(R.id.regionStatsTextView);
            NetworkImageView imageView = (NetworkImageView) rowView.findViewById(R.id.regionThumbnail);
            regionNameTextView.setText(selectedRegion.getName());
            regionDescriptionTextView.setText(selectedRegion.getDescription());
            regionStatsTextView.setText(selectedRegion.getNumMembers()+" Followers | "+selectedRegion.getNumPosts()+" Posts");
            imageView.setDefaultImageResId(R.mipmap.anonymous_pp);
            if(selectedRegion.getThumbnailUrl() != null && selectedRegion.getThumbnailUrl() != "null" && selectedRegion.getThumbnailUrl() != "")
            {
                imageView.setImageUrl(selectedRegion.getThumbnailUrl(), RequestQueueSingleton.getInstance().getImageLoader());
            }
            final ImageView toggleSubscribedImage = (ImageView) rowView.findViewById(R.id.subscribeToRegionToggleButton);
            if(!selectedRegion.canUnsubscribe()) {
                toggleSubscribedImage.setImageDrawable(null);
                toggleSubscribedImage.setClickable(false);
            }
            else if(Reverb.getInstance().getRegionManager().isRegionSubscribed(selectedRegion.getRegionId()))
            {
                selectedRegion.subscribe();
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
                    int rId = -1;
                    if(selectedTab == TabType.SUBSCRIBED)
                    {
                        rId = Reverb.getInstance().getRegionManager().getSubscribedRegions().get(position).getRegionId();
                    }
                    else
                    {
                        rId = Reverb.getInstance().getRegionManager().getNearbyRegions().get(position).getRegionId();
                    }
                    Intent intent = new Intent(getActivity(), CreateRegionActivity.class);
                    intent.putExtra("SELECTED_REGION_ID", rId);
                    startActivity(intent);
                }
            });
            return rowView;
        }
    }

    @Override
    public void onOpenLogoutEditOverlayClick()
    {
        standardOnOpenLogoutEditOverlayClick(R.id.overlayRegionFeedLayoutContainer);
    }

    @Override
    public void onEditUserInfoOverlayClick()
    {
        standardOnEditUserInfoOverlayClick(R.id.overlayRegionFeedLayoutContainer);
    }


    @Override
    public void onRefresh()
    {
        Reverb.getInstance().getRegionManager().updateRegionLists();
        adapter.notifyDataSetChanged();
        Log.d("Reverb","Regions refresh");
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);

    }


    public void switchUIToAnonymous()
    {
        if (createRegionButton != null)
        {
            createRegionButton.setImageResource(R.drawable.create_region_image_dark);
        }
        setTabUI(true);

    }

    public void switchUIToPublic()
    {
        if (createRegionButton != null)
        {
            createRegionButton.setImageResource(R.drawable.create_region_image);
        }
        setTabUI(false);
    }

    private void setTabUI(boolean anon)
    {

        if(subscribedTab!=null && nearbyTab != null)
        {
            if (selectedTab == TabType.SUBSCRIBED)
            {
                if (!anon)
                {
                    subscribedTab.setBackgroundResource(R.drawable.selected_tab);
                    nearbyTab.setBackgroundColor(getResources().getColor(R.color.reverb_blue_1));
                } else
                {
                    subscribedTab.setBackgroundResource(R.drawable.selected_tab_anon);
                    nearbyTab.setBackgroundColor(getResources().getColor(R.color.anonymous_background));
                }
            } else if (selectedTab == TabType.NEARBY)
            {
                if (!anon)
                {
                    nearbyTab.setBackgroundResource(R.drawable.selected_tab);
                    subscribedTab.setBackgroundColor(getResources().getColor(R.color.reverb_blue_1));
                } else
                {
                    nearbyTab.setBackgroundResource(R.drawable.selected_tab_anon);
                    subscribedTab.setBackgroundColor(getResources().getColor(R.color.anonymous_background));
                }
            }
        }
    }
}
