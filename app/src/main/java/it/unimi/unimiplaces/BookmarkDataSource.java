package it.unimi.unimiplaces;

import java.util.List;

import it.unimi.unimiplaces.core.model.BookmarkableEntity;

/**
 * BookmarkDataSource
 */
public class BookmarkDataSource {

    BookmarksDb db;

    public BookmarkDataSource(BookmarksDb db){
        this.db = db;
    }

    public boolean entitiesIsBookmarked( BookmarkableEntity entity ){
        return this.db.entityWithIdentifierExists(entity.getBookmarkableObjectIdentifier());
    }

    public List<Bookmark> allBookmarks(){
        return this.db.allBookmarks();
    }

    public Bookmark saveBookmark(BookmarkableEntity entity){
        return this.db.saveBookmark(entity);
    }

}
