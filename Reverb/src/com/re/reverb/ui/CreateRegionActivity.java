package com.re.reverb.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CreateRegionActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{

    private enum ShapeType{
        None,
        Circle,
        Rectangle,
        Polygon
    }

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<LatLng> polyPoints = new ArrayList<LatLng>();
    private ArrayList<LatLng> rectPoints = new ArrayList<LatLng>();
    private ShapeType selectedShapeType = ShapeType.None;

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
        double lat = Reverb.getInstance().locationManager.getCurrentLatitude();
        double longi = Reverb.getInstance().locationManager.getCurrentLongitude();
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
        Log.d("Reverb","Map touched at ["+latLng.latitude+","+latLng.longitude+"]");
//        switch(selectedShapeType) {
//            case Polygon:
//                polyPoints.add(latLng);
//                drawPolygon();
//                break;
//            case Rectangle:
//                rectPoints.add(latLng);
//                drawRectangle();
//                break;
//            case Circle:
//                break;
//
//
//        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    public void onSaveRegionClick(View view) {
        drawPolygon();
    }

    public void onClearRegionClick(View view){
        mMap.clear();
        polyPoints.clear();
    }

    private boolean drawPolygon() {
        sortPointsClockwise();
        mMap.clear();
        for(LatLng point: polyPoints) {
            mMap.addMarker(new MarkerOptions()
                    .position(point));
        }
        if(polyPoints.size() > 2) {
            PolygonOptions options = new PolygonOptions();
            for (LatLng coord : polyPoints) {
                options.add(coord);
            }
            options.strokeColor(R.color.reverb_blue_1);
            options.fillColor(R.color.reverb_blue_1);
            Polygon polygon = mMap.addPolygon(options);
            return true;
        }
        else {
            return false;
        }
    }


    private boolean drawRectangle() {
        for(int i = 0; i < rectPoints.size(); i+=2){
            if(i+1 < rectPoints.size()) {
                LatLng firstPoint = rectPoints.get(i);
                LatLng secondPoint = rectPoints.get(i+1);
                LatLng thirdPoint = new LatLng(firstPoint.latitude, secondPoint.longitude);
                LatLng fourthPoint = new LatLng(secondPoint.latitude, firstPoint.longitude);
                PolygonOptions options = new PolygonOptions();
                options.add(firstPoint);
                options.add(secondPoint);
                options.add(thirdPoint);
                options.add(fourthPoint);
                options.strokeColor(R.color.reverb_blue_1);
                options.fillColor(R.color.reverb_blue_1);
                Polygon polygon = mMap.addPolygon(options);
            }
        }
        return true;
    }

    public void onSelectCircle(View view) {
        removeOverlays();
        if(selectedShapeType != ShapeType.Circle) {
            View circleButton = findViewById(R.id.editRegionCircle);
            circleButton.setBackgroundColor(getResources().getColor(R.color.reverb_blue_4));
            selectedShapeType = ShapeType.Circle;
            mMap.getUiSettings().setZoomControlsEnabled(false);
            FrameLayout drawCircleLayout = (FrameLayout) findViewById(R.id.drawCircleLayout);
            if (drawCircleLayout == null) {
                FrameLayout myLayout = (FrameLayout) findViewById(R.id.overlayContainerLayout);
                View drawCircleView = getLayoutInflater().inflate(R.layout.draw_circle_layout, myLayout, false);
                myLayout.addView(drawCircleView);
            }
        } else {
            selectedShapeType = ShapeType.None;
        }
    }
    public void onSelectRectangle(View view) {
        removeOverlays();
        if(selectedShapeType != ShapeType.Rectangle) {
            View rectButton = findViewById(R.id.editRegionSquare);
            rectButton.setBackgroundColor(getResources().getColor(R.color.reverb_blue_4));
            mMap.getUiSettings().setZoomControlsEnabled(false);
            selectedShapeType = ShapeType.Rectangle;
            FrameLayout drawRectLayout = (FrameLayout) findViewById(R.id.drawRectLayout);
            if (drawRectLayout == null) {
                FrameLayout myLayout = (FrameLayout) findViewById(R.id.overlayContainerLayout);
                View drawRectView = getLayoutInflater().inflate(R.layout.draw_rect_layout, myLayout, false);
                myLayout.addView(drawRectView);
            }
        } else {
            selectedShapeType = ShapeType.None;
        }
    }

    private void removeOverlays(){
        mMap.getUiSettings().setZoomControlsEnabled(true);
        View view = findViewById(R.id.drawCircleLayout);
        if(view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        view = findViewById(R.id.drawRectLayout);
        if(view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            parent.removeView(view);
        }
        View circleButton = findViewById(R.id.editRegionCircle);
        circleButton.setBackgroundColor(Color.TRANSPARENT);
        View rectButton = findViewById(R.id.editRegionSquare);
        rectButton.setBackgroundColor(Color.TRANSPARENT);
    }

    private void sortPointsClockwise() {
        if(polyPoints.size() > 1) {
            double latMean = 0;
            double longMean = 0;
            for (LatLng point : polyPoints) {
                latMean += point.latitude;
                longMean += point.longitude;
            }
            latMean /= polyPoints.size();
            longMean /= polyPoints.size();
            ArrayList<RegionPoint> regionPoints = new ArrayList<RegionPoint>(polyPoints.size());
            for (int i = 0; i < polyPoints.size(); i++) {
                LatLng latLng = polyPoints.get(i);
                regionPoints.add(i, new RegionPoint(latLng, new LatLng(latMean, longMean)));
            }
            Collections.sort(regionPoints);
            polyPoints.clear();
            for (RegionPoint p : regionPoints) {
                polyPoints.add(p.getLatLng());
            }
        }
    }

    private class RegionPoint implements Comparable<RegionPoint>{
        private Double angleFromMiddle;
        private LatLng latLng;
        public RegionPoint(LatLng latLng, LatLng meanLatLng) {
            this.latLng = latLng;
            angleFromMiddle = new Double(Math.atan2(latLng.latitude - meanLatLng.latitude,latLng.longitude - meanLatLng.longitude));
        }

        public Double getAngleFromMiddle() {
            return angleFromMiddle;
        }

        public LatLng getLatLng() {

            return latLng;
        }

        @Override
        public int compareTo(RegionPoint another) {
            if(another.getAngleFromMiddle() > this.getAngleFromMiddle()) return -1;
            else if(another.getAngleFromMiddle() < this.getAngleFromMiddle()) return 1;
            else return 0;
        }
    }
}
