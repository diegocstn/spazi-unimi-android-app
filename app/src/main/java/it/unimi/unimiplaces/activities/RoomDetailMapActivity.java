package it.unimi.unimiplaces.activities;

import android.content.Intent;
import android.os.Bundle;

import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.views.BuildingsMapView;

public class RoomDetailMapActivity extends AppDetailSectionActivity {

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
    }
}
