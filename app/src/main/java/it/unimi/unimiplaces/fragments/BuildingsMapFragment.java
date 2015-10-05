package it.unimi.unimiplaces.fragments;

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

import it.unimi.unimiplaces.PresenterViewInterface;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;


/**
 * 
 */
public class BuildingsMapFragment extends Fragment implements PresenterViewInterface {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(getFragmentManager().findFragmentById(R.id.buildings_map));
        fragmentTransaction.commit();
    }

    @Override
    public void setModel(List<BaseEntity> model){
        this.model = model;
        if( this.map != null ){
            this.placeBuildingsMarker();
        }
    }

    private void initMap(GoogleMap googleMap){
        map = googleMap;
        if( model!=null ){
            this.placeBuildingsMarker();
        }


        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder markerBounds = new LatLngBounds.Builder();
                for( Marker marker:markers ){
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
}
