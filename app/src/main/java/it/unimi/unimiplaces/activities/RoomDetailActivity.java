package it.unimi.unimiplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.presenters.RoomDetailPresenter;
import it.unimi.unimiplaces.views.RoomDetailViewInterface;

public class RoomDetailActivity extends AppDetailSectionActivity implements RoomDetailViewInterface{

    RoomDetailPresenter presenter;

    String room_id;
    String building_id;
    TextView textViewRoomName;
    TextView textViewRoomCategory;
    TextView textViewFloor;
    TextView textViewBuildingName;
    TextView textViewBuildingAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        Intent intent = getIntent();
        this.room_id        = intent.getStringExtra(Room.MODEL_KEY);
        this.building_id    = intent.getStringExtra(Building.MODEL_KEY);
        this.setUpDetailActivity(intent.getStringExtra(Room.MODEL_NAME_KEY));

        textViewRoomName        = (TextView) findViewById(R.id.room_name);
        textViewRoomCategory    = (TextView) findViewById(R.id.room_category);
        textViewFloor           = (TextView) findViewById(R.id.floor);
        textViewBuildingName    = (TextView) findViewById(R.id.building_name);
        textViewBuildingAddress = (TextView) findViewById(R.id.building_address);

        // initialize presenter
        this.presenter = new RoomDetailPresenter(APIManager.APIManagerFactory.createAPIManager(this),this);
        this.presenter.init(this.room_id,this.building_id);
    }

    @Override
    public void setRoomName(String roomName) {
        this.textViewRoomName.setText(roomName);
    }

    @Override
    public void setRoomFloor(String roomFloor) {
        this.textViewFloor.setText(roomFloor);
    }

    @Override
    public void setBuildingName(String buildingName) {
        this.textViewBuildingName.setText(buildingName);
    }

    @Override
    public void setBuildingAddress(String buildingAddress) {
        this.textViewBuildingAddress.setText(buildingAddress);
    }

    @Override
    public void setRoomCategory(String roomCategory) {
        this.textViewRoomCategory.setText(roomCategory);
    }

    @Override
    public void setFloorMapForRoom() {

    }

    @Override
    public void showNoResultsMessage() {

    }
}
