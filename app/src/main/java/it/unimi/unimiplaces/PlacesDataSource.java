package it.unimi.unimiplaces;

import java.util.List;

/**
 * PlacesDataSource
 */
public class PlacesDataSource {

    PlacesDb db;
    public PlacesDataSource(PlacesDb db){
        this.db = db;
    }
    public List<LookupTableEntry> getEntriesForKey(String key){
        return this.db.searchPlacesWithKey(key);
    }

    public boolean buildingExists(String buildingId){
        return true;
    }
    public boolean placeExists(String buildingId,String roomId){
        return true;
    }
}
