package it.unimi.unimiplaces.activities;

import android.content.Intent;
import android.os.Bundle;

import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.Room;

public class RoomDetailActivity extends AppDetailSectionActivity{

    String room_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        Intent intent = getIntent();
        room_id = intent.getStringExtra(Room.MODEL_KEY);
        this.setUpDetailActivity(intent.getStringExtra(Room.MODEL_NAME_KEY));
    }

}
