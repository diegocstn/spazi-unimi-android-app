package it.unimi.unimiplaces.activities;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.LocalizableEntity;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.presenters.RoomDetailMapPresenter;
import it.unimi.unimiplaces.views.BuildingsMapView;
import it.unimi.unimiplaces.views.RoomDetailMapViewInterface;

public class RoomDetailMapActivity extends AppDetailSectionActivity implements RoomDetailMapViewInterface {

    RoomDetailMapPresenter presenter;
    BuildingsMapView mapView;
    String room_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail_map);
        mapView = (BuildingsMapView) findViewById(R.id.room_map_view);

        Intent intent = getIntent();
        this.room_id        = intent.getStringExtra(Room.MODEL_KEY);
        this.setUpDetailActivity(intent.getStringExtra(Room.MODEL_NAME_KEY));

        this.presenter = new RoomDetailMapPresenter(this);
        this.presenter.init(
                intent.getStringExtra(LocalizableEntity.TITLE_KEY),
                intent.getStringExtra(LocalizableEntity.ADDRESS_KEY),
                intent.getDoubleExtra(LocalizableEntity.COORDINATES_LAT_KEY,0.0),
                intent.getDoubleExtra(LocalizableEntity.COORDINATES_LNG_KEY,0.0));
    }

    @Override
    public void placeRoomOnMap(List<BaseEntity> entities) {
        this.mapView.setModel(entities);
    }
}
