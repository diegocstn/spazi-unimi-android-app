package it.unimi.unimiplaces.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.Bookmark;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.views.BookmarksViewInterface;

/**
 * A fragment representing a list of Bookmarks.
 *
 */
public class BookmarksFragment extends Fragment implements BookmarksViewInterface {

    private final String LOG_TAG = "BOOKMARKS";
    private View view;
    private ListView listView;
    private BookmarksListAdapter arrayAdapter;

    public static BookmarksFragment newInstance() {
        BookmarksFragment fragment = new BookmarksFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookmarksFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.fragment_buildings, container, false);
        }catch (InflateException e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.bookmarks_list);
        listView.setAdapter(this.arrayAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.arrayAdapter = new BookmarksListAdapter(getActivity(),new ArrayList<Bookmark>());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }*/

    @Override
    public void setModel(List<Bookmark> bookmarks) {
        this.arrayAdapter.clear();
        this.arrayAdapter.addAll(bookmarks);
        this.arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBookmarksList() {
        this.listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoResults() {

    }

    private class BookmarksListAdapter extends ArrayAdapter<Bookmark>{
        private final Context context;
        private final List<Bookmark> bookmarks;

        private class BookmarkViewHolder{
            public TextView bookmarkTitle;
        }

        public BookmarksListAdapter(Context context,List<Bookmark> bookmarks){
            super( context, R.layout.bookmarks_list_item , bookmarks );
            this.context    = context;
            this.bookmarks  = bookmarks;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            View bookmarkView = convertView;

            // reuse view
            if( bookmarkView==null ){
                LayoutInflater inflater = LayoutInflater.from(context);
                bookmarkView = inflater.inflate(R.layout.bookmarks_list_item,null);

                // create BookmarkViewHolder in order to avoid searching
                // resources within XML and speeds up rendering
                BookmarkViewHolder holder = new BookmarkViewHolder();
                holder.bookmarkTitle     = (TextView) bookmarkView.findViewById(R.id.bookmark_title);
                bookmarkView.setTag(holder);
            }

            // fill with building data
            BookmarkViewHolder holder = (BookmarkViewHolder) bookmarkView.getTag();
            Bookmark bookmark = this.bookmarks.get(position);
            holder.bookmarkTitle.setText(bookmark.title);

            return bookmarkView;
        }
    }
}
