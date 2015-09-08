package it.unimi.unimiplaces;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;


/**
 * 
 */
public class BuildingsMapFragment extends Fragment implements PresenterInterface {

    private GoogleMap map;
    private List<BaseEntity> model;
    private List<Marker> markers;

    /* Map init configuration */
    private int mapZoomDefault          = 13;
    private double mapCenterLatitude    = 9.1843412;
    private double mapCenterLongitude   = 45.4687535;

    public BuildingsMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void setModel(Activity activity, List<BaseEntity> model){
        this.model = model;
        if( this.map != null ){
            this.placeBuildingsMarker();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buildings_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.buildings_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                initMap(googleMap);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.buildings_map);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(mapFragment);
        fragmentTransaction.commit();
    }

    private void initMap(GoogleMap googleMap){
        map = googleMap;
        if( model!=null ){
            this.placeBuildingsMarker();
        }

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
        LatLngBounds.Builder markerBounds = new LatLngBounds.Builder();
        Marker marker;
        markers = new ArrayList<>();

        for (BaseEntity entity : this.model) {
            Building building = (Building) entity;
            marker = map.addMarker(markerOptionsForBuilding(building));
            markers.add(marker);

            markerBounds.include(marker.getPosition());
        }

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(markerBounds.build(),10));

    }
}
