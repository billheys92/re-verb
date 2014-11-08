package com.re.reverb.ui;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;

import java.util.ArrayList;

public class CreateRegionActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private int numMarkers = 0;
    private ArrayList<LatLng> polyPoints = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_region);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        double lat = Reverb.getInstance().locationManager.getLatitude();
        double longi = Reverb.getInstance().locationManager.getLongitude();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 16.0f);
        mMap.moveCamera(update);
        mMap.setOnMapClickListener(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mapType = sharedPrefs.getString("pref_map_type","roadmap");
        if(mapType.equals("roadmap")) { mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); }
        else if(mapType.equals("satellite")) { mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); }
        else if(mapType.equals("hybrid")) { mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); }
        else if(mapType.equals("terrain")) { mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); }
        else { mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("point #"+(++numMarkers)));
        polyPoints.add(latLng);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        PolygonOptions options = new PolygonOptions();
        for(LatLng coord: polyPoints) {
            options.add(coord);
        }
        options.strokeColor(R.color.reverb_blue_1);
        options.fillColor(R.color.reverb_blue_1);
        Polygon polygon = mMap.addPolygon(options);
    }

    public void onSaveRegionClick(View view) {
        if(polyPoints.size() > 2) {
            PolygonOptions options = new PolygonOptions();
            for (LatLng coord : polyPoints) {
                options.add(coord);
            }
            options.strokeColor(R.color.reverb_blue_1);
            options.fillColor(R.color.reverb_blue_1);
            Polygon polygon = mMap.addPolygon(options);
        } else {
            Toast.makeText(this, "Sorry, you need to put in at least 3 points!", Toast.LENGTH_SHORT);
        }
    }

    public void onSelectCircle(View view) {
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }
    public void onSelectRectangle(View view) {
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }
    public void onSelectPolygon(View view) {
        mMap.getUiSettings().setScrollGesturesEnabled(true);

    }
}
