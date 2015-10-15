package it.unimi.unimiplaces;

import it.unimi.unimiplaces.core.model.BookmarkableEntity;

/**
 * convenience Bookmark object class
 */
public class Bookmark {
    public long id;
    public BookmarkableEntity.BOOKMARK_TYPE type;
    public String identifier;
    public String title;


    public Bookmark(BookmarkableEntity.BOOKMARK_TYPE type,String identifier,String title){
        this.type       = type;
        this.identifier = identifier;
        this.title      = title;
    }

    public Bookmark(long id, BookmarkableEntity.BOOKMARK_TYPE type,String identifier,String title){
        this.id         = id;
        this.type       = type;
        this.identifier = identifier;
        this.title      = title;
    }
}
