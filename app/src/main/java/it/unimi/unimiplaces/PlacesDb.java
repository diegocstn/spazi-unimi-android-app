package it.unimi.unimiplaces;

import android.content.Context;
import android.database.Cursor;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * PlacesDb
 */
public class PlacesDb extends SQLiteAssetHelper{

    private static final String DB_NAME = "rooms_lookup.sqlite";
    private static final int DB_VERSION = 1;

    public PlacesDb(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    private LookupTableEntry entryForCursorValue(Cursor cursor){
        return new LookupTableEntry(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
    }

    public List<LookupTableEntry> searchPlacesWithKey(String key){
        String query = "SELECT DISTINCT * FROM lookup WHERE lookup.building_name LIKE \"%%s%\" OR lookup.room_name LIKE \"%%s%%\"";
        Cursor cursor = this.getReadableDatabase().rawQuery(String.format(query,key),null);

        if( cursor.getCount()==0 ){
            return null;
        }
        List<LookupTableEntry> results = new ArrayList<>(cursor.getCount());
        cursor.moveToFirst();
        while ( !cursor.isAfterLast() ){
            results.add(entryForCursorValue(cursor));
            cursor.moveToNext();
        }
        return results;
    }
}
