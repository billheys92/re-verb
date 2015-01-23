package com.re.reverb.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.regions.CircleRegionShape;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionShape;
import com.re.reverb.ui.shapeWrappers.Shape;

import java.util.Stack;

public class CreateRegionActivity extends FragmentActivity{

    private enum ShapeType{
        None,
        Circle,
        Rectangle,
        Polygon
    }

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ShapeType selectedShapeType = ShapeType.None;
    private Stack<Shape> regionShapes = new Stack<Shape>();
    private View currentOverlay = null;
    private Region region;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_region);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getInt("SELECTED_REGION_ID") > -1) {
            this.region = Reverb.getInstance().getRegionManager().getNearbyRegions().get(extras.getInt("SELECTED_REGION_ID"));
            for(RegionShape shape: this.region.getShapes()) {
//                regionShapes.add(new Shape(shape));
            }
//            drawMapShapes();
            Toast.makeText(this, "Opened region: "+this.region.getName(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            showEditRegionDetailsOverlay();
            Toast.makeText(this, "Creating new region", Toast.LENGTH_SHORT).show();
        }

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
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        double lat = Reverb.getInstance().locationManager.getCurrentLatitude();
        double longi = Reverb.getInstance().locationManager.getCurrentLongitude();
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 16.0f);
        mMap.moveCamera(update);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String mapType = sharedPrefs.getString("pref_map_type","roadmap");
        if(mapType.equals("roadmap")) { mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); }
        else if(mapType.equals("satellite")) { mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); }
        else if(mapType.equals("hybrid")) { mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); }
        else if(mapType.equals("terrain")) { mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); }
        else { mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); }
    }

    public void onSaveRegionClick(View view) {
    }

    public void onClearRegionClick(View view){
        mMap.clear();
    }

    public void onSelectCircle(View view) {
        selectShapeToDraw(ShapeType.Circle, R.id.editRegionCircle, R.id.drawCircleLayout, R.layout.draw_circle_layout, R.id.drawCircleView);
    }
    public void onSelectRectangle(View view) {
        selectShapeToDraw(ShapeType.Rectangle, R.id.editRegionSquare, R.id.drawRectLayout, R.layout.draw_rect_layout, R.id.drawRectView);
    }

    private void selectShapeToDraw(ShapeType shapeType, int selectButtonId, int layoutId, int layoutResource, int viewId){
        if(selectedShapeType != shapeType) {
            removeOverlays();
            setSelectShapeButtonColourSelected(selectButtonId);
            selectedShapeType = shapeType;
            mMap.getUiSettings().setZoomControlsEnabled(false);
            View drawCircleLayout = findViewById(layoutId);
            if (drawCircleLayout == null) {
                displayOverlay(layoutResource);
            }
        } else {
            selectedShapeType = ShapeType.None;
            setSelectShapeButtonColourDeselected(selectButtonId);
            DrawMapShapeOverlayView overlayView = (DrawMapShapeOverlayView)findViewById(viewId);
            for(Shape shape: overlayView.shapeStack){
                addRegionShape(shape);
            }
            removeOverlays();
        }
    }

    private void setSelectShapeButtonColourSelected(int buttonId){
        View button = findViewById(buttonId);
        button.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setSelectShapeButtonColourDeselected(int buttonId){
        View button = findViewById(buttonId);
        button.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showEditRegionDetailsOverlay(){
        displayOverlay(R.layout.region_details_overlay_layout);
        Button b = (Button)findViewById(R.id.saveRegionDetailsButton);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                removeOverlays();
            }
        });
    }

    private void showRegionDetailsOverlay(){

    }

    private void displayOverlay(int resource) {

        removeOverlays();
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(resource, null);
        this.currentOverlay = v;

        // insert into main view
        FrameLayout myLayout = (FrameLayout) findViewById(R.id.overlayContainerLayout);
        myLayout.addView(v);

    }

    private void removeOverlays(){
        if(mMap != null)
        {
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        if(this.currentOverlay != null) {
            ViewGroup parent = (ViewGroup) this.currentOverlay.getParent();
            parent.removeView(this.currentOverlay);
            this.currentOverlay = null;
        }
//        View view = findViewById(R.id.drawCircleLayout);
//        if(view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            parent.removeView(view);
//        }
//        view = findViewById(R.id.drawRectLayout);
//        if(view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            parent.removeView(view);
//        }
//        view = findViewById(R.id.regionOverlayLayout);
//        if(view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            parent.removeView(view);
//        }
//        View circleButton = findViewById(R.id.editRegionCircle);
//        circleButton.setBackgroundColor(Color.TRANSPARENT);
//        View rectButton = findViewById(R.id.editRegionSquare);
//        rectButton.setBackgroundColor(Color.TRANSPARENT);
    }

    public void addRegionShape(Shape shape){
        this.regionShapes.push(shape);
        shape.drawOnMap(mMap);
    }

    private void drawMapShapes(){
        mMap.clear();
        for(Shape s: regionShapes) {
            s.drawOnMap(mMap);
        }
    }
}
