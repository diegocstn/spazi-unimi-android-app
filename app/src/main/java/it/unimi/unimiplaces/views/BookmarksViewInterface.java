package it.unimi.unimiplaces.views;

import java.util.List;

import it.unimi.unimiplaces.Bookmark;

/**
 * BookmarksViewInterface
 */
public interface BookmarksViewInterface {
    void setModel(List<Bookmark> bookmarks);
    void showBookmarksList();
    void showNoResults();
}
