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
        return null;
    }
}
