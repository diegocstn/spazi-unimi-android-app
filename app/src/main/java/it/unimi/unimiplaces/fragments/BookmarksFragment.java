package it.unimi.unimiplaces.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.Bookmark;
import it.unimi.unimiplaces.BookmarksDataSource;
import it.unimi.unimiplaces.BookmarksDb;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.activities.BuildingDetailActivity;
import it.unimi.unimiplaces.activities.RoomDetailActivity;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.presenters.BookmarksPresenter;
import it.unimi.unimiplaces.views.BookmarksViewInterface;

/**
 * A fragment representing a list of Bookmarks.
 *
 */
public class BookmarksFragment extends Fragment implements BookmarksViewInterface, AdapterView.OnItemClickListener {

    private final String LOG_TAG = "BOOKMARKS";
    private View view;
    private ListView listView;
    private TextView noResultsTextView;
    private BookmarksListAdapter adapter;
    private BookmarksPresenter presenter;

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
            view = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        }catch (InflateException e){
            Log.e(LOG_TAG, e.getMessage());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noResultsTextView   = (TextView) view.findViewById(R.id.bookmarks_no_results);
        listView            = (ListView) view.findViewById(R.id.bookmarks_list);
        listView.setAdapter(this.adapter);
        listView.setOnItemClickListener(this);

        this.presenter.init();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.adapter    = new BookmarksListAdapter(getActivity(),new ArrayList<Bookmark>());
        this.presenter  = new BookmarksPresenter(new BookmarksDataSource(new BookmarksDb(getActivity())),this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseEntity entity = this.presenter.payloadForDetailAtIndex(position);
        Intent detailIntent;
        if( entity instanceof Room){
            detailIntent = new Intent(getActivity(), RoomDetailActivity.class);
            detailIntent.putExtra(Room.MODEL_KEY,((Room) entity).r_id);
            detailIntent.putExtra(Building.MODEL_KEY,((Room) entity).b_id);
            detailIntent.putExtra(Room.MODEL_NAME_KEY,((Room) entity).getBookmarkableObjectTitle());
            startActivity(detailIntent);
        }
        if( entity instanceof Building){
            detailIntent = new Intent(getActivity(), BuildingDetailActivity.class);
            detailIntent.putExtra(Building.MODEL_KEY,((Building) entity).b_id);
            detailIntent.putExtra(Building.MODEL_NAME_KEY,((Building) entity).getBookmarkableObjectTitle());
            startActivity(detailIntent);
        }

    }

    @Override
    public void setModel(List<Bookmark> bookmarks) {
        this.adapter.clear();
        this.adapter.addAll(bookmarks);
        this.adapter.notifyDataSetChanged();
    }

    @Override
    public void showBookmarksList() {
        this.noResultsTextView.setVisibility(View.GONE);
        this.listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoResults() {
        this.noResultsTextView.setVisibility(View.VISIBLE);
        this.listView.setVisibility(View.GONE);
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
