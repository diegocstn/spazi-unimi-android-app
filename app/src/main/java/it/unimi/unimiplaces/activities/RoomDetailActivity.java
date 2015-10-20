package it.unimi.unimiplaces.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.BookmarksDataSource;
import it.unimi.unimiplaces.BookmarksDb;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.LocalizableEntity;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.core.model.RoomEvent;
import it.unimi.unimiplaces.presenters.RoomDetailPresenter;
import it.unimi.unimiplaces.views.BookmarksNotificationBar;
import it.unimi.unimiplaces.views.FloorMapView;
import it.unimi.unimiplaces.views.RoomDetailViewInterface;
import it.unimi.unimiplaces.views.RoomTimetableView;

public class RoomDetailActivity extends AppDetailSectionActivity implements RoomDetailViewInterface{

    RoomDetailPresenter presenter;

    String room_id;
    String building_id;
    TextView textViewRoomCategory;
    TextView textViewFloor;
    TextView textViewBuildingName;
    TextView textViewBuildingAddress;
    TextView textViewEquipments;
    View viewEquipments;
    FloorMapView floorMapView;
    FloatingActionButton bookmarksFab;
    ImageButton routingButton;
    ImageButton timetableButton;
    BookmarksNotificationBar bookmarksNotificationBar;

    AlertDialog roomTimetableDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        Intent intent = getIntent();
        this.room_id        = intent.getStringExtra(Room.MODEL_KEY);
        this.building_id    = intent.getStringExtra(Building.MODEL_KEY);
        this.setUpDetailActivity(intent.getStringExtra(Room.MODEL_NAME_KEY));

        textViewRoomCategory    = (TextView) findViewById(R.id.room_category);
        textViewFloor           = (TextView) findViewById(R.id.floor);
        textViewBuildingName    = (TextView) findViewById(R.id.building_name);
        textViewBuildingAddress = (TextView) findViewById(R.id.building_address);
        textViewEquipments      = (TextView) findViewById(R.id.room_equipments);
        floorMapView            = (FloorMapView) findViewById(R.id.floor_map);
        viewEquipments          = findViewById(R.id.room_equipments_block);

        bookmarksFab            = (FloatingActionButton) findViewById(R.id.fab_add_remove_bookmarks);
        routingButton           = (ImageButton) findViewById(R.id.routing_button);
        timetableButton         = (ImageButton) findViewById(R.id.room_events_button);

        routingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoomRouting();
            }
        });

        // initialize presenter
        this.presenter = new RoomDetailPresenter(
                APIManager.APIManagerFactory.createAPIManager(this),
                this,
                new BookmarksDataSource(new BookmarksDb(this)));
        this.presenter.init(this.room_id, this.building_id);

        this.bookmarksFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.saveBookmark();
            }
        });

    }


    private void showRoomRouting(){
        Intent routingIntent    = new Intent(this,RoomDetailMapActivity.class);
        Room room               = (Room) this.presenter.payloadForDetailAtIndex(0);
        routingIntent.putExtra(Room.MODEL_NAME_KEY,room.room_name);
        routingIntent.putExtra(LocalizableEntity.TITLE_KEY, room.getLocalizableTitle());
        routingIntent.putExtra(LocalizableEntity.ADDRESS_KEY,room.getLocalizableAddress());
        routingIntent.putExtra(LocalizableEntity.COORDINATES_LAT_KEY,room.getCoordinates().lat);
        routingIntent.putExtra(LocalizableEntity.COORDINATES_LNG_KEY,room.getCoordinates().lng);
        startActivity(routingIntent);
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
    public void hideRoomEquipments() {
        this.viewEquipments.setVisibility(View.GONE);
    }

    @Override
    public void setRoomEquipments(String equipments){
        this.textViewEquipments.setText(equipments);
    }

    @Override
    public void setFloorMapForRoom(String SVGdata) {
        this.floorMapView.highlightRoomInMap(SVGdata, this.room_id);
    }

    @Override
    public void showNoResultsMessage() {

    }

    @Override
    public void setDisplayAddBookmarksButton(boolean show){
        if( show ){
            this.bookmarksFab.setVisibility(View.VISIBLE);
            this.bookmarksNotificationBar = new BookmarksNotificationBar(this,findViewById(R.id.room_detail_info));
        }else{
            this.bookmarksFab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessBookmarkSaved(){
        this.bookmarksNotificationBar.showSuccessMessage();
    }

    @Override
    public void onErrorBookmarkSaved(){
        this.bookmarksNotificationBar.showErrorMessage();
    }

    @Override
    public void showRoomTimetableButton() {
        this.timetableButton.setVisibility(View.VISIBLE);

        this.timetableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRoomTimetable();
            }
        });
    }

    @Override
    public void setRoomTimetableEvents(List<BaseEntity> events) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.room_timetable_title));
        List<RoomEvent> roomEvents = new ArrayList<>(events.size());
        for (BaseEntity entity : events){
            roomEvents.add((RoomEvent)entity);
        }
        RoomTimetableView view = new RoomTimetableView(this);
        view.setEventsList( roomEvents );
        builder.setView( view );

        roomTimetableDialog = builder.create();
    }

    private void showRoomTimetable(){
        if( roomTimetableDialog==null ){
            return;
        }
        roomTimetableDialog.show();
    }

}
