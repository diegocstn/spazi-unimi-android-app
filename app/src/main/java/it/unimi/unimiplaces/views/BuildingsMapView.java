package it.unimi.unimiplaces.views;

import android.app.Activity;
import android.content.Context;
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

    private Context context;
    private GoogleMap map;
    private List<BaseEntity> model;
    private HashMap<Marker,Integer> markers;
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
        map = googleMap;
        if( model!=null ){
            this.placeBuildingsMarker();
        }

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder markerBounds = new LatLngBounds.Builder();
                for (Marker marker : markers.keySet()) {
                    markerBounds.include(marker.getPosition());
                }
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(markerBounds.build(), 10));
            }
        });

        map.setOnInfoWindowClickListener(this);

    }


    private MarkerOptions markerOptionsForBuilding(LocalizableEntity entity){
        MarkerOptions markerOptions;
        Coordinates coordinates = entity.getCoordinates();
        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(coordinates.lat, coordinates.lng));
        markerOptions.title(entity.getLocalizableTitle());
        markerOptions.snippet(entity.getLocalizableAddress());
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_building_marker));

        return markerOptions;
    }

    private void placeBuildingsMarker(){

        /* remove all marker if needed */
        if( markers != null ){
            for (Marker marker : markers.keySet()){
                marker.remove();
            }
            markers.clear();
        }else{
            markers = new HashMap<>();
        }

        for (int i=0;i<this.model.size();i++) {
            LocalizableEntity building = (LocalizableEntity) this.model.get(i);
            markers.put(map.addMarker(markerOptionsForBuilding(building)),i);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        this.parentPresenter.onDetailActionListener(markers.get(marker));
    }

    @Override
    public void setModel(List<BaseEntity> model){
        this.model = model;
        if( this.map != null ){
            this.placeBuildingsMarker();
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

}
