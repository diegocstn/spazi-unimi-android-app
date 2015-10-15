package it.unimi.unimiplaces.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private ArrayAdapter arrayAdapter;

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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setListAdapter(new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS));*/
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

    }

    @Override
    public void showBookmarksList() {

    }

    @Override
    public void showNoResults() {

    }
}
