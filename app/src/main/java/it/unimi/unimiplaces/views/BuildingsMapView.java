package it.unimi.unimiplaces.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
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

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.PresenterViewInterface;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;

/**
 * BuildingsFragment child view used for map-mode representation of buildings
 */
public class BuildingsMapView extends RelativeLayout implements PresenterViewInterface,OnMapReadyCallback {

    private Context context;
    private GoogleMap map;
    private List<BaseEntity> model;
    private List<Marker> markers;

    /* Map init configuration */
    private int mapZoomDefault          = 13;
    private double mapCenterLatitude    = 9.1843412;
    private double mapCenterLongitude   = 45.4687535;

    public BuildingsMapView(Context context) {
        super(context);
        this.context = context;
        this.init();
    }

    public BuildingsMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.init();
    }


    private void init(){
        inflate(getContext(), R.layout.view_buildings_map, this);
        MapFragment mapFragment = (MapFragment)((Activity)this.context).getFragmentManager().findFragmentById(R.id.buildings_map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if( model!=null ){
            this.placeBuildingsMarker();
        }


        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder markerBounds = new LatLngBounds.Builder();
                for (Marker marker : markers) {
                    markerBounds.include(marker.getPosition());
                }
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(markerBounds.build(), 10));
            }
        });
    }


    private MarkerOptions markerOptionsForBuilding(Building building){
        MarkerOptions markerOptions;
        markerOptions = new MarkerOptions();
        markerOptions.position( new LatLng(building.coordinates.lat,building.coordinates.lng));
        markerOptions.title(building.building_name);
        markerOptions.snippet(building.address);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_building_marker));

        return markerOptions;
    }

    private void placeBuildingsMarker(){

        /* remove all marker if needed */
        if( markers != null ){
            for (Marker marker : markers){
                marker.remove();
            }
            markers.clear();
        }else{
            markers = new ArrayList<>();
        }

        for (BaseEntity entity : this.model) {
            Building building = (Building) entity;
            markers.add(map.addMarker(markerOptionsForBuilding(building)));
        }
    }

    @Override
    public void setModel(List<BaseEntity> model){
        this.model = model;
        if( this.map != null ){
            this.placeBuildingsMarker();
        }
    }
}
