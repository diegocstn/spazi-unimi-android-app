package it.unimi.unimiplaces.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Coordinates;
import it.unimi.unimiplaces.core.model.LocalizableEntity;

/**
 * BuildingsFragment child view used for map-mode representation of buildings
 */
public class BuildingsMapView extends RelativeLayout implements
        PresenterViewInterface,
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener{

    boolean mapInitialized;
    boolean mapLoaded;
    boolean modelInitialized;
    private Context context;
    private GoogleMap map;
    private ClusterManager<ClusteredMarker> clusterManager;
    private List<BaseEntity> model;
    private List<ClusteredMarker> clusteredMarkers;
    private HashMap<String,Integer> markers;
    private PresenterViewInterface parentPresenter;

    private final String LOG_TAG = "BUILDINGSMAPVIEW";

    public BuildingsMapView(Context context) {
        super(context);
        this.context = context;
        this.init();
    }

    public BuildingsMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.mapInitialized     = false;
        this.modelInitialized   = false;
        this.init();
    }


    private void init(){
        try {
            inflate(getContext(), R.layout.view_buildings_map, this);
        }catch (InflateException e){
            Log.e(LOG_TAG,e.getMessage());
        }

        MapFragment mapFragment = (MapFragment)((Activity)this.context).getFragmentManager().findFragmentById(R.id.buildings_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.mapInitialized = true;
        map = googleMap;
        clusterManager = new ClusterManager<>(getContext(),map);
        clusterManager.setRenderer(new ClusterCustomRenderer(getContext(), map, clusterManager));
        map.setOnCameraChangeListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);

        if( this.modelInitialized ){
            this.placeMarkers();
        }

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mapLoaded = true;
                if( !modelInitialized ){
                    return;
                }
                adjustZoomLevelToMarkers();
            }
        });

        map.setOnInfoWindowClickListener(this);

    }

    private void adjustZoomLevelToMarkers(){
        if (markers.size() == 1) {
            ClusteredMarker marker = (ClusteredMarker) ((clusteredMarkers.toArray())[0]);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 18f));
            return;
        }
        LatLngBounds.Builder markerBounds = new LatLngBounds.Builder();
        for (ClusteredMarker marker : clusteredMarkers) {
            markerBounds.include(marker.getPosition());
        }
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(markerBounds.build(), 10));
    }

    private MarkerOptions markerOptionsForEntity(LocalizableEntity entity){
        MarkerOptions markerOptions;
        Coordinates coordinates = entity.getCoordinates();
        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(coordinates.lat, coordinates.lng));
        markerOptions.title(entity.getLocalizableTitle());
        markerOptions.snippet(entity.getLocalizableAddress());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_building_marker));

        return markerOptions;
    }

    private void placeMarkers(){
        Log.v(LOG_TAG,"Placing markers");
        /* remove all marker if needed */
        if( markers != null ){
            clusterManager.clearItems();
            markers.clear();
            clusteredMarkers.clear();
        }else{
            markers             = new HashMap<>();
            clusteredMarkers    = new ArrayList<>();
        }

        for (int i=0;i<this.model.size();i++) {
            LocalizableEntity building  = (LocalizableEntity) this.model.get(i);
            ClusteredMarker marker      = new ClusteredMarker(building);
            clusterManager.addItem(marker);
            clusteredMarkers.add(marker);
            markers.put(marker.getEntity().getLocalizableTitle(),i);
        }

        clusterManager.cluster();

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if( this.parentPresenter!=null ) {
            this.parentPresenter.onDetailActionListener(markers.get(marker.getTitle()));
        }else{
            // open map app
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+marker.getPosition().latitude+","+marker.getPosition().longitude));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            this.context.startActivity(intent);
        }
    }

    @Override
    public void setModel(List<BaseEntity> model){
        this.model              = model;
        this.modelInitialized   = true;

        if( this.mapInitialized ){
            this.placeMarkers();
        }

        if( this.mapLoaded ){
            this.adjustZoomLevelToMarkers();
        }
    }

    @Override
    public void clearListeners(){
        map.setOnMapLoadedCallback(null);
        map.setOnInfoWindowClickListener(null);
        this.parentPresenter = null;
    }

    @Override
    public void setDetailActionListener(PresenterViewInterface listener){
        this.parentPresenter = listener;
    }
    @Override
    public void onDetailActionListener(int i) {}


    /* Clustering class */
    private class ClusteredMarker implements ClusterItem {
        private final LocalizableEntity entity;
        private final LatLng position;

        public ClusteredMarker(LocalizableEntity entity){
            this.entity     = entity;
            this.position   = new LatLng(entity.getCoordinates().lat,entity.getCoordinates().lng);
        }

        public LocalizableEntity getEntity(){
            return this.entity;
        }

        @Override
        public LatLng getPosition() {
            return this.position;
        }
    }

    /* cluster render class */
    private class ClusterCustomRenderer extends DefaultClusterRenderer<ClusteredMarker>{
        public ClusterCustomRenderer(Context context, GoogleMap map, ClusterManager<ClusteredMarker> clusterManager){
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ClusteredMarker item, MarkerOptions markerOptions) {
            MarkerOptions customMarkerOptions = markerOptionsForEntity(item.getEntity());
            markerOptions.title(customMarkerOptions.getTitle());
            markerOptions.snippet(customMarkerOptions.getSnippet());
            markerOptions.icon(customMarkerOptions.getIcon());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }


    }

}
