package it.unimi.unimiplaces.core.model;

import java.util.List;

/**
 * BookmarkableEntity defines the methods that
 * a class which could be bookmarked should implements
 */
public interface BookmarkableEntity {
    String getBookmarkableType();
    String getBookmarkableObjectIdentifier();
    String getBookmarkableObjectTitle();
    List<String> getIdentifierFromBookmarkData(String data);
    enum BOOKMARK_TYPE{
        ROOM,
        BUILDING
    };
}
