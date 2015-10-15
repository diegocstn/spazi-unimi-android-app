package it.unimi.unimiplaces.presenters;

import java.util.List;

import it.unimi.unimiplaces.Bookmark;
import it.unimi.unimiplaces.BookmarkDataSource;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.views.BookmarksViewInterface;

/**
 * BookmarksPresenter
 */
public class BookmarksPresenter implements Presenter {

    private List<Bookmark> model;
    private BookmarkDataSource dataSource;
    private BookmarksViewInterface view;

    public BookmarksPresenter(BookmarkDataSource source,BookmarksViewInterface view){
        this.dataSource = source;
        this.model      = this.dataSource.allBookmarks();
        this.view       = view;

        this.view.setModel(this.model);
        if( this.model.size()==0 ){
            this.view.showNoResults();
        }else{
            this.view.showBookmarksList();
        }
    }

    @Override
    public void init(String id) {}

    @Override
    public void filterModelWithFilterAtIndex(int index) {

    }

    @Override
    public BaseEntity payloadForDetailAtIndex(int index) {
        return null;
    }
}
