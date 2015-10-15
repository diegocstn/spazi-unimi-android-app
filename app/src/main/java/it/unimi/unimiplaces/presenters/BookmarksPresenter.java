package it.unimi.unimiplaces.presenters;

import java.util.List;

import it.unimi.unimiplaces.Bookmark;
import it.unimi.unimiplaces.BookmarksDataSource;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.BookmarkableEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.views.BookmarksViewInterface;

/**
 * BookmarksPresenter
 */
public class BookmarksPresenter implements Presenter {

    private List<Bookmark> model;
    private BookmarksDataSource dataSource;
    private BookmarksViewInterface view;

    public BookmarksPresenter(BookmarksDataSource source,BookmarksViewInterface view){
        this.dataSource = source;
        this.model      = this.dataSource.allBookmarks();
        this.view       = view;
    }

    public void init(){
        this.view.setModel(this.model);
        if( this.model.size()==0 ){
            this.view.showNoResults();
        }else{
            this.view.showBookmarksList();
        }
    }

    private List<String> getIdentifierFromBookmarkData(Bookmark bookmark){
        List<String> res = null;
        switch ( bookmark.type ){
            case ROOM:
                Room r  = new Room("","","");
                res     = r.getIdentifierFromBookmarkData(bookmark.identifier);
                r       = null;
                break;

            case BUILDING:
                Building b  = new Building("","","");
                res         = b.getIdentifierFromBookmarkData(bookmark.identifier);
                b           = null;
                break;
        }

        return res;
    }

    @Override
    public void init(String id) {
        this.init();
    }

    @Override
    public void filterModelWithFilterAtIndex(int index) {

    }

    @Override
    public BaseEntity payloadForDetailAtIndex(int index) {
        if( index>this.model.size() ){
            return null;
        }

        Bookmark bookmark   = this.model.get(index);
        List<String> args   = this.getIdentifierFromBookmarkData(bookmark);

        /* Room boomark */
        if( bookmark.type.equals(BookmarkableEntity.BOOKMARK_TYPE.ROOM) ){
            Room r = new Room(args.get(0),bookmark.title,"");
            Building b = new Building(args.get(1),"","");
            r.setBuildingAttributes(b);
            return r;
        }

        /* Building bookmark */
        if( bookmark.type.equals(BookmarkableEntity.BOOKMARK_TYPE.BUILDING) ){
            return new Building(args.get(0),bookmark.title,"");
        }

        return null;
    }

    public void deleteBookmarkAtindex(int index){
        Bookmark bookmark = this.model.get(index);
        this.model.remove(index);
        this.dataSource.deleteBookmark(bookmark);
        this.view.setModel(this.model);
    }
}
