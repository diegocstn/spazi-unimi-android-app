package it.unimi.unimiplaces.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import it.unimi.unimiplaces.LookupTableEntry;
import it.unimi.unimiplaces.PlacesDataSource;
import it.unimi.unimiplaces.PlacesDb;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.presenters.PlacesPresenter;
import it.unimi.unimiplaces.views.PlacesViewInterface;


/**
 * Places Fragment
 */
public class PlacesFragment extends Fragment implements PlacesViewInterface,TextWatcher {

    private PlacesPresenter presenter;
    private EditText editTextSearchKey;
    private ListView resultsListView;
    private TextView noResultsTextView;

    private static final String LOG_TAG = "PLACES";

    public static PlacesFragment newInstance() {
        PlacesFragment fragment = new PlacesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = new PlacesPresenter(new PlacesDataSource(new PlacesDb(getActivity())),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.editTextSearchKey.removeTextChangedListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextSearchKey   = (EditText) getActivity().findViewById(R.id.places_search_key);
        resultsListView     = (ListView) getActivity().findViewById(R.id.places_results);
        noResultsTextView   = (TextView) getActivity().findViewById(R.id.places_no_results);

        editTextSearchKey.addTextChangedListener(this);
    }

    /* TextWatcher methods */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.v(LOG_TAG, s.toString());
    }
    @Override
    public void afterTextChanged(Editable s) {}


    /* PlacesViewInterface methods */
    @Override
    public void showNoResults() {
        this.resultsListView.setVisibility(View.GONE);
        this.noResultsTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setResults(List<LookupTableEntry> results) {
        this.resultsListView.setVisibility(View.VISIBLE);
        this.noResultsTextView.setVisibility(View.GONE);

    }



}
