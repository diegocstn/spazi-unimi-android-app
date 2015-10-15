package it.unimi.unimiplaces.presenters;

import java.util.List;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.BookmarksDataSource;
import it.unimi.unimiplaces.core.api.APIDelegateInterface;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.views.RoomDetailViewInterface;

/**
 * Room Detail Presenter
 */
public class RoomDetailPresenter implements Presenter, APIDelegateInterface {

    private String roomID;
    private Room room;
    APIManager apiManager;
    RoomDetailViewInterface view;
    BookmarksDataSource bookmarksDataSource;

    public RoomDetailPresenter(APIManager apiManager,RoomDetailViewInterface view, BookmarksDataSource dataSource){
        this.apiManager             = apiManager;
        this.view                   = view;
        this.bookmarksDataSource    = dataSource;
    }

    @Override
    public void apiRequestStart() {

    }

    @Override
    public void apiRequestEnd(List<BaseEntity> results) {

        if( results==null ){
            this.view.showNoResultsMessage();
            return;
        }

        this.room = (Room) results.get(0);
        this.view.setRoomCategory(room.cat_name);
        this.view.setRoomFloor(room.floor);
        this.view.setBuildingName(room.building_name);
        this.view.setBuildingAddress(room.building_address);

        if( this.room.equipments != null && this.room.equipments.size()>0 ){
            StringBuilder builder = new StringBuilder();
            for ( String equipment : this.room.equipments ){
                builder.append(equipment);
                builder.append(", ");
            }
            builder.delete(builder.length()-2,builder.length());
            this.view.setRoomEquipments(builder.toString());
        }else{
            this.view.hideRoomEquipments();
        }

        if( room.map!=null ){
            this.view.setFloorMapForRoom(room.map);
        }
    }

    @Override
    public void apiRequestError() {
        this.view.showNoResultsMessage();
    }

    @Override
    public void init(String id) {
        this.roomID = id;
    }

    public void init(String roomID, String buildingID){
        this.apiManager.roomByRIDAndBID(this,roomID,buildingID);
        Room r = new Room(roomID,"","");
        r.setB_id(buildingID);
        this.view.setDisplayAddBookmarksButton(!this.bookmarksDataSource.entityIsBookmarked(r));
    }

    public void saveBookmark(){
        if( this.bookmarksDataSource.saveBookmark(this.room)!=null ){
            this.view.setDisplayAddBookmarksButton(false);
            this.view.onSuccessBookmarkSaved();
        }else{
            this.view.onErrorBookmarkSaved();
        }
    }


    @Override
    public void filterModelWithFilterAtIndex(int index) {}
    @Override
    public BaseEntity payloadForDetailAtIndex(int index) {
        return this.room;
    }
}
