package it.unimi.unimiplaces.presenters;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Coordinates;
import it.unimi.unimiplaces.core.model.LocalizableEntity;
import it.unimi.unimiplaces.views.RoomDetailMapViewInterface;

/**
 * RoomDetailMapPresenter
 */
public class RoomDetailMapPresenter implements Presenter {

    String room_id;
    RoomDetailMapViewInterface view;

    public RoomDetailMapPresenter(RoomDetailMapViewInterface view){
        this.view = view;
    }

    @Override
    public void init(String id) {
        this.room_id = id;
    }

    public void init(String title, String address, double lat, double lng){
        Location roomLocation = new Location(title,address,lat,lng);
        List<BaseEntity> entities = new ArrayList<>();
        entities.add(roomLocation);
        this.view.placeRoomOnMap(entities);
    }

    @Override
    public void filterModelWithFilterAtIndex(int index) {

    }

    @Override
    public BaseEntity payloadForDetailAtIndex(int index) {
        return null;
    }

    private class Location extends BaseEntity implements LocalizableEntity{

        Coordinates coordinates;
        String title;
        String address;

        public Location(String title,String address, double lat, double lng){
            this.title      = title;
            this.address    = address;
            this.setCoordinates(lat,lng);
        }

        @Override
        public void setCoordinates(double lat, double lng) {
            this.coordinates = new Coordinates(lat,lng);
        }

        @Override
        public String getLocalizableTitle() {
            return this.title;
        }

        @Override
        public String getLocalizableAddress() {
            return this.address;
        }

        @Override
        public Coordinates getCoordinates() {
            return this.coordinates;
        }
    }
}
