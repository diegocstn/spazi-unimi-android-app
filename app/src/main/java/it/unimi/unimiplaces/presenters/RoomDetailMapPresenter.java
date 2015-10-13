package it.unimi.unimiplaces.presenters;

import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.views.RoomDetailMapViewInterface;

/**
 * RoomDetailMapPresenter
 */
public class RoomDetailMapPresenter implements Presenter {

    String room_id;
    RoomDetailMapViewInterface view;

    @Override
    public void init(String id) {
        this.room_id = id;
    }

    public void init(String title, String address, float lat, float lng){

    }

    @Override
    public void filterModelWithFilterAtIndex(int index) {

    }

    @Override
    public BaseEntity payloadForDetailAtIndex(int index) {
        return null;
    }
}
