package it.unimi.unimiplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.model.BookmarkableEntity;

/**
 * BookmarksDb class, it manage bookmarks database
 */
public class BookmarksDb extends SQLiteOpenHelper{

    private static final String LOG_TAG = "BookmarksDB";

    private static final String DB_NAME                     = "bookmarks";
    private static final int DB_VERSION                     = 1;
    private static final String TABLE_NAME                  = "bookmarks";
    private static final String FIELD_OBJECT_ID             = "id";
    private static final String FIELD_OBJECT_TYPE           = "type";
    private static final String FIELD_OBJECT_IDENTIFIER     = "identifier";
    private static final String FIELD_OBJECT_TITLE          = "title";

    public BookmarksDb(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(LOG_TAG,"Creating bookmarks db");
        String SQLQuery = "CREATE TABLE bookmarks (" +
                FIELD_OBJECT_ID+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                FIELD_OBJECT_TYPE+" TEXT NOT NULL," +
                FIELD_OBJECT_IDENTIFIER+" TEXT NOT NULL," +
                FIELD_OBJECT_TITLE+" TEXT NOT NULL" +
                ");";
        db.execSQL(SQLQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(LOG_TAG,"Upgrading from v"+oldVersion+" to v"+newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean entityWithIdentifierExists(String identifier){
        String SQLQuery     = "SELECT id from "+TABLE_NAME+" WHERE identifier=?";
        String args[]       = new String[1];
        args[0] = identifier;
        Cursor cursor       = getReadableDatabase().rawQuery(SQLQuery,args);
        boolean res = cursor.getCount()>0;
        cursor.close();

        return res;
    }

    public void deleteBookmarkById(long id){
        String args[]  = new String[1];
        args[0]        = String.valueOf(id);
        getReadableDatabase().delete(TABLE_NAME,FIELD_OBJECT_ID+"=?",args);
    }

    public void deleteBookmarkByIdentifier(String identifier){
        String args[]  = new String[1];
        args[0]        = identifier;
        getReadableDatabase().delete(TABLE_NAME,FIELD_OBJECT_IDENTIFIER+"=?",args);
    }

    public void deleteBookmarkByTile(String title){
        String args[]  = new String[1];
        args[0]        = title;
        getReadableDatabase().delete(TABLE_NAME,FIELD_OBJECT_TITLE+"=?",args);
    }

    public Bookmark saveBookmark(BookmarkableEntity entity){
        ContentValues values    = new ContentValues();
        Bookmark bookmark       = new Bookmark(entity.getBookmarkableType(),entity.getBookmarkableObjectIdentifier(),entity.getBookmarkableObjectTitle());
        values.put(FIELD_OBJECT_TYPE,bookmark.type.toString());
        values.put(FIELD_OBJECT_IDENTIFIER,bookmark.identifier);
        values.put(FIELD_OBJECT_TITLE,bookmark.title);
        long newBookmarkId = getReadableDatabase().insert(TABLE_NAME,null,values);

        if( newBookmarkId>-1 ){
            bookmark.id = newBookmarkId;
            return bookmark;
        }

        return null;
    }

    public List<Bookmark> allBookmarks(){
        String SQLQuery     = "SELECT * from "+TABLE_NAME;
        Cursor cursor       = getReadableDatabase().rawQuery(SQLQuery,null);
        BookmarkableEntity.BOOKMARK_TYPE type;

        /* results list */
        List<Bookmark> res = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if( cursor.getString(1).equals(BookmarkableEntity.BOOKMARK_TYPE.BUILDING.toString()) ){
                type = BookmarkableEntity.BOOKMARK_TYPE.BUILDING;
            }else{
                type = BookmarkableEntity.BOOKMARK_TYPE.ROOM;
            }
            res.add( new Bookmark(cursor.getInt(0),type,cursor.getString(2), cursor.getString(3)) );
            cursor.moveToNext();
        }

        cursor.close();

        return res;
    }
}
