package com.re.reverb.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.re.reverb.R;
import com.re.reverb.androidBackend.Location;
import com.re.reverb.androidBackend.Reverb;
import com.re.reverb.androidBackend.regions.CircleRegionShape;
import com.re.reverb.androidBackend.regions.CommonsRegion;
import com.re.reverb.androidBackend.regions.RectangleRegionShape;
import com.re.reverb.androidBackend.regions.Region;
import com.re.reverb.androidBackend.regions.RegionFactory;
import com.re.reverb.androidBackend.regions.RegionImageUrlFactory;
import com.re.reverb.androidBackend.regions.RegionShape;
import com.re.reverb.androidBackend.regions.dto.GetRegionByIdDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionCircleDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionDto;
import com.re.reverb.androidBackend.regions.dto.ReceiveRegionRectangleDto;
import com.re.reverb.androidBackend.utils.SuccessStatus;
import com.re.reverb.ui.shapeWrappers.Shape;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CreateRegionActivity extends ActionBarActivity
{

    private enum ShapeType{
        None,
        Circle,
        Rectangle,
        Polygon
    }

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ShapeType selectedShapeType = ShapeType.None;
    private Stack<RegionShape> regionShapes = new Stack<RegionShape>();
    private View currentOverlay = null;
    private Region region;
    private boolean editingToolsOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_region);
        setUpMapIfNeeded();
        closeEditingTools();

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getInt("SELECTED_REGION_ID") > -1) {
            fetchRegionDetails(extras.getInt("SELECTED_REGION_ID"));
        }
        else
        {
            this.region = new Region();
            showEditRegionDetailsOverlay();
            Toast.makeText(this, "Creating new region", Toast.LENGTH_SHORT).show();
        }
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
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longi), 15.0f);
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
        region.editShapes(regionShapes);
        SuccessStatus status = region.saveRegion();
//        RegionImageUrlFactory.createFromRegion(region);
        if(!status.success()) {
            Toast.makeText(this, status.reason(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Region \""+region.getName()+"\" created.", Toast.LENGTH_SHORT).show();
        }
        Log.d("Reverb",status.reason());
    }


    public void onClearRegionClick(View view){
        if(region.canEdit().success())
        {
            region.beginEditing();
            mMap.clear();
            this.regionShapes.clear();
        }
        else
        {
            Toast.makeText(this, region.canEdit().reason(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onSelectCircle(View view) {
        selectShapeToDraw(ShapeType.Circle, R.id.editRegionCircle, R.id.drawCircleLayout, R.layout.draw_circle_layout, R.id.drawCircleView);
    }
    public void onSelectRectangle(View view) {
        selectShapeToDraw(ShapeType.Rectangle, R.id.editRegionSquare, R.id.drawRectLayout, R.layout.draw_rect_layout, R.id.drawRectView);
    }

    private void selectShapeToDraw(ShapeType shapeType, int selectButtonId, int layoutId, int layoutResource, int viewId){
        if(region.canEdit().success())
        {
            if (selectedShapeType != shapeType)
            {
                removeOverlays();
                removeAllShapeButtonColours();
                setSelectShapeButtonColourSelected(selectButtonId);
                selectedShapeType = shapeType;
                mMap.getUiSettings().setZoomControlsEnabled(false);
                View drawCircleLayout = findViewById(layoutId);
                if (drawCircleLayout == null)
                {
                    displayOverlay(layoutResource);
                    DrawMapShapeOverlayView view = (DrawMapShapeOverlayView) findViewById(viewId);
                    view.attachShapeAddedToOverlayListener(new ShapeAddedToOverlayListener()
                    {
                        @Override
                        public void shapeAdded(Shape s)
                        {
                            addRegionShape(s);
                        }
                    });
                }
            } else
            {
                selectedShapeType = ShapeType.None;
                setSelectShapeButtonColourDeselected(selectButtonId);
                DrawMapShapeOverlayView overlayView = (DrawMapShapeOverlayView) findViewById(viewId);
                removeOverlays();
            }
        }
        else
        {
            Toast.makeText(this, region.canEdit().reason(), Toast.LENGTH_SHORT).show();
        }
    }

    private void removeAllShapeButtonColours()
    {
        setSelectShapeButtonColourDeselected(R.id.editRegionSquare);
        setSelectShapeButtonColourDeselected(R.id.editRegionCircle);
    }

    private void setSelectShapeButtonColourSelected(int buttonId){
        View button = findViewById(buttonId);
        button.setBackgroundColor(getResources().getColor(R.color.dark_grey));
    }

    private void setSelectShapeButtonColourDeselected(int buttonId){
        View button = findViewById(buttonId);
        button.setBackgroundColor(Color.TRANSPARENT);
    }

    public void showEditRegionDetailsOverlay(View view){
        if(region.canEdit().success())
        {
            this.showEditRegionDetailsOverlay();
        }
        else
        {
            this.showRegionDetailsOverlay();
        }
    }

    private void showEditRegionDetailsOverlay(){
        final Activity activity = this;
        displayOverlay(R.layout.edit_region_details_overlay_layout);
        region.beginEditing();
        Button b = (Button)findViewById(R.id.saveRegionDetailsButton);
        View.OnClickListener listener = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(region.getName() == null && region.getDescription() == null)
                {
                    Toast.makeText(activity, "You must provide a name and description for this region.",Toast.LENGTH_SHORT).show();
                } else if(region.getName() == null)
                {
                    Toast.makeText(activity, "You must provide a name for this region.",Toast.LENGTH_SHORT).show();
                } else if(region.getDescription() == null)
                {
                    Toast.makeText(activity, "You must provide a description for this region.",Toast.LENGTH_SHORT).show();
                } else
                {
                    openEditingTools();
                    removeOverlays();
                }
            }
        };
        b.setOnClickListener(listener);
        Button b2 = (Button)findViewById(R.id.discardChangesButton);
        b2.setOnClickListener(listener);
        EditText nameExitText = (EditText)findViewById(R.id.editRegionName);
//        nameExitText.setText(region.getName() == null ? "Region Name" : region.getName());
        nameExitText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                region.editName(s.toString());
            }
        });
        EditText descriptionEditText = (EditText)findViewById(R.id.editRegionDescription);
//        descriptionEditText.setText(region.getDescription() == null ? "Description" : region.getDescription());
        descriptionEditText.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                region.editDescription(s.toString());
            }
        });
    }

    private void showRegionDetailsOverlay(){
        displayOverlay(R.layout.display_region_details_overlay_layout);
        final TextView nameTextView = (TextView)findViewById(R.id.displayRegionName);
        nameTextView.setText(region.getName());
        TextView descriptionTextView = (TextView)findViewById(R.id.displayRegionDescription);
        descriptionTextView.setText(region.getDescription());
        Button b2 = (Button)findViewById(R.id.closeDetailsButton);
        b2.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                removeOverlays();
            }
        });
    }

    private View displayOverlay(int resource) {

        removeOverlays();
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(resource, null);
        this.currentOverlay = v;

        // insert into main view
        FrameLayout myLayout = (FrameLayout) findViewById(R.id.overlayContainerLayout);
        myLayout.addView(v);
        return v;

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
    }

    public void addRegionShape(Shape shape){
        RegionShape regionShape = shape.getReverbRegionShape(mMap);
        this.regionShapes.push(regionShape);
        addRegionShapeToMap(regionShape);
    }

    private void drawMapShapes(){
        mMap.clear();
        for(RegionShape s: regionShapes) {
            addRegionShapeToMap(s);
        }
    }

    public void addRegionShapeToMap(RegionShape regionShape) {

        if(regionShape instanceof CircleRegionShape) {
            CircleRegionShape circleRegionShape = (CircleRegionShape) regionShape;
            Log.d("Reverb", "shape has a radius of "+circleRegionShape.getRadius());
            LatLng centre = new LatLng(circleRegionShape.getCentrePoint().getLatitude(),circleRegionShape.getCentrePoint().getLongitude());
            Circle circle = mMap.addCircle(new CircleOptions()
                    .center(centre)
                    .radius(circleRegionShape.getRadius())
                    .strokeWidth(0)
                    .fillColor(R.color.map_shape_color));
        }
        else if (regionShape instanceof RectangleRegionShape) {
            RectangleRegionShape rectRegionShape = (RectangleRegionShape) regionShape;

            List<Location> rectPoints = rectRegionShape.getPoints();
            List<LatLng> rectPointsLatLng = new ArrayList<LatLng>();
            for(Location l: rectPoints) {
                rectPointsLatLng.add(new LatLng(l.getLatitude(), l.getLongitude()));
            }
            Polygon rect = mMap.addPolygon(new PolygonOptions()
                    .addAll(rectPointsLatLng)
                    .strokeWidth(0)
                    .fillColor(R.color.map_shape_color));
        }
    }

    public void undoAddShape(View view)
    {
        this.regionShapes.pop();
        drawMapShapes();
    }

    public void toggleEditingToolsLayout(View view) {
        if(region.canEdit().success() && !editingToolsOpen) {
            openEditingTools();
        }
        else if (editingToolsOpen) {
            closeEditingTools();
        }
        else
        {
            Toast.makeText(this, region.canEdit().reason(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openEditingTools() {
        LinearLayout tools=(LinearLayout)this.findViewById(R.id.editingToolsLayout);
        tools.setVisibility(LinearLayout.VISIBLE);
        editingToolsOpen = true;
    }

    private void closeEditingTools() {
        LinearLayout tools=(LinearLayout)this.findViewById(R.id.editingToolsLayout);
        tools.setVisibility(LinearLayout.GONE);
        editingToolsOpen = false;
    }

    private void fetchRegionDetails(int regionId)
    {
        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {

            @Override
            public void onResponse(JSONObject jsonObject)
            {
                Gson gson = new Gson();
                ReceiveRegionDto regionDto = gson.fromJson(jsonObject.toString(), ReceiveRegionDto.class);

                ArrayList<ReceiveRegionCircleDto> circleDtos = new ArrayList<ReceiveRegionCircleDto>();
                ArrayList<ReceiveRegionRectangleDto> rectangleDtos = new ArrayList<ReceiveRegionRectangleDto>();
                try
                {
                    if(jsonObject.getString("circles") != "null")
                    {
                        JSONArray jsonCircles = jsonObject.getJSONArray("circles");
                        for (int i = 0; i < jsonCircles.length(); i++)
                        {
                            JSONObject obj = jsonCircles.getJSONObject(i);
                            ReceiveRegionCircleDto dto = gson.fromJson(obj.toString(), ReceiveRegionCircleDto.class);
                            circleDtos.add(dto);
                        }
                    }
                    if(jsonObject.getString("rectangles") != "null")
                    {
                        JSONArray jsonRectangles = jsonObject.getJSONArray("rectangles");
                        for (int i = 0; i < jsonRectangles.length(); i++)
                        {
                            JSONObject obj = jsonRectangles.getJSONObject(i);
                            ReceiveRegionRectangleDto dto = gson.fromJson(obj.toString(), ReceiveRegionRectangleDto.class);
                            rectangleDtos.add(dto);
                        }
                    }
                    regionDto.setCirclesDto(circleDtos);
                    regionDto.setRectanglesDto(rectangleDtos);
                } catch (JSONException e)
                {
                    Log.e("Reverb","Error parsing JSON string while fetching Region details");
                    e.printStackTrace();
                }
                region = RegionFactory.createRegionFromDto(regionDto);
                if(region != null)
                {
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.setTitle(region.getName());
                    for (RegionShape shape : region.getShapes())
                    {
                        regionShapes.add(shape);
                    }
                    drawMapShapes();
                }
            }

        };

        if(regionId > 0)
        {
            GetRegionByIdDto dto = new GetRegionByIdDto(regionId);
            com.re.reverb.network.RegionManagerImpl.getRegionById(listener, dto);
        }
        else if (regionId == 0)
        {
            region = new CommonsRegion(Reverb.getInstance().getCurrentLocation());
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(region.getName());
            for (RegionShape shape : region.getShapes())
            {
                regionShapes.add(shape);
            }
            drawMapShapes();
        }

    }
}
