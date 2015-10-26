package it.unimi.unimiplaces.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.LookupTableEntry;
import it.unimi.unimiplaces.PlacesDataSource;
import it.unimi.unimiplaces.PlacesDb;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.activities.RoomDetailActivity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.presenters.PlacesPresenter;
import it.unimi.unimiplaces.views.PlacesViewInterface;


/**
 * Places Fragment
 */
public class PlacesFragment extends Fragment implements PlacesViewInterface,TextWatcher,AdapterView.OnItemClickListener {

    private PlacesPresenter presenter;
    private EditText editTextSearchKey;
    private ListView resultsListView;
    private TextView noResultsTextView;
    private PlacesListAdapter resultsAdapter;

    private static final String LOG_TAG = "PLACES";

    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter          = new PlacesPresenter(new PlacesDataSource(new PlacesDb(getActivity())),this);
        this.resultsAdapter     = new PlacesListAdapter(getActivity(),new ArrayList<LookupTableEntry>());
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

        this.resultsListView.setAdapter(this.resultsAdapter);
        this.resultsListView.setOnItemClickListener(this);
    }

    /* TextWatcher methods */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.v(LOG_TAG, s.toString());
        presenter.searchPlacesWithKey(s.toString());
    }
    @Override
    public void afterTextChanged(Editable s) {}

    /* OnItemListener */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Room room = (Room) this.presenter.payloadForDetailAtIndex(position);
        Intent detailIntent = new Intent(getActivity(), RoomDetailActivity.class);
        detailIntent.putExtra(Room.MODEL_KEY, room.r_id);
        detailIntent.putExtra(Building.MODEL_KEY, room.b_id);
        detailIntent.putExtra(Room.MODEL_NAME_KEY, room.room_name);
        startActivity(detailIntent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

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
        this.resultsAdapter.clear();
        this.resultsAdapter.addAll(results);
        this.resultsAdapter.notifyDataSetChanged();

    }

    @Override
    public void clearResults() {
        this.resultsAdapter.clear();
        this.resultsAdapter.notifyDataSetChanged();
    }

    private class PlacesListAdapter extends ArrayAdapter<LookupTableEntry> {
        private final Context context;
        private final List<LookupTableEntry> entries;

        private class PlaceViewHolder{
            public TextView entryName;
            public TextView entryAddress;
        }

        public PlacesListAdapter(Context context,List<LookupTableEntry> entries){
            super( context, R.layout.buildings_list_item , entries );
            this.context    = context;
            this.entries    = entries;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group){
            View entryRowView = convertView;

            // reuse view
            if( entryRowView==null ){
                LayoutInflater inflater = LayoutInflater.from(context);
                entryRowView = inflater.inflate(R.layout.buildings_list_item,group,false);

                // create BuildingViewHolder in order to avoid searching
                // resources within XML and speeds up rendering
                PlaceViewHolder holder = new PlaceViewHolder();
                holder.entryName     = (TextView) entryRowView.findViewById(R.id.building_name);
                holder.entryAddress  = (TextView) entryRowView.findViewById(R.id.building_address);

                entryRowView.setTag(holder);
            }

            // fill with building data
            PlaceViewHolder holder = (PlaceViewHolder) entryRowView.getTag();
            LookupTableEntry entry = this.entries.get(position);
            holder.entryName.setText(entry.roomName);
            holder.entryAddress.setText(entry.buildingName);


            return entryRowView;
        }
    }


}
