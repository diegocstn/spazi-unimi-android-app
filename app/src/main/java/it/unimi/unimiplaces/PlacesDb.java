package it.unimi.unimiplaces;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * PlacesDb
 */
public class PlacesDb extends SQLiteAssetHelper{

    private static final String DB_NAME = "rooms_lookup.sqlite";
    private static final int DB_VERSION = 1;

    public PlacesDb(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }
}
