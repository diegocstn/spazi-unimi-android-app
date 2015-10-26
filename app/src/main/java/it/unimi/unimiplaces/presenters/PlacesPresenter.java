package it.unimi.unimiplaces.presenters;

import java.util.List;

import it.unimi.unimiplaces.LookupTableEntry;
import it.unimi.unimiplaces.PlacesDataSource;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.views.PlacesViewInterface;

/**
 * PlacesPresenter
 */
public class PlacesPresenter implements Presenter {

    PlacesDataSource dataSource;
    PlacesViewInterface view;
    List<LookupTableEntry> model;

    public PlacesPresenter(PlacesDataSource dataSource,PlacesViewInterface view){
        this.dataSource = dataSource;
        this.view       = view;
    }

    public void searchPlacesWithKey(String key){

        if( key.equals("") || key.length()==0){
            this.view.clearResults();
            return;
        }

        this.model = this.dataSource.getEntriesForKey(key);

        /* no results */
        if( this.model==null ){
            this.view.showNoResults();
            return;
        }
        this.view.setResults(this.model);
    }

    @Override
    public void init(String id) {}

    @Override
    public void filterModelWithFilterAtIndex(int index) {}

    @Override
    public BaseEntity payloadForDetailAtIndex(int index) {
        if( index>this.model.size() ){
            return null;
        }
        LookupTableEntry entry = this.model.get(index);
        Room room = new Room(entry.roomId,entry.roomName,"");
        room.setB_id(entry.buildingId);
        return room;
    }
}
