package it.unimi.unimiplaces.views;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import it.unimi.unimiplaces.R;

/**
 * BookmarksNotificationBar
 */
public class BookmarksNotificationBar {

    Context context;
    View parentView;

    public BookmarksNotificationBar(Context context,View view){
        this.context    = context;
        this.parentView = view;
    }

    public void showSuccessMessage(){
        Snackbar snackbar = Snackbar.make(parentView, context.getString(R.string.bookmarks_bookmark_saved),Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public void showErrorMessage(){
        Snackbar snackbar = Snackbar.make(parentView, context.getString(R.string.bookmarks_bookmark_error),Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
