package it.unimi.unimiplaces.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.unimi.unimiplaces.PresenterViewInterface;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;

/**
 * A fragment representing a list of buildings.
 *
 */
public class BuildingsListFragment extends Fragment implements PresenterViewInterface,AbsListView.OnItemClickListener {

    private AbsListView mListView;
    private List<BaseEntity> model;
    private BuildingsListAdapter buildingsListAdapter;
    private TextView emptyText;

    public static BuildingsListFragment newInstance() {
        BuildingsListFragment fragment = new BuildingsListFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BuildingsListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buildings_list,container,false);

        // Set the adapter
        mListView   = (AbsListView) view.findViewById(android.R.id.list);
        emptyText   = (TextView) view.findViewById(android.R.id.empty);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        if( this.buildingsListAdapter != null ){
            mListView.setAdapter(this.buildingsListAdapter);
        }

        return view;
    }

    @Override
    public void setModel(Activity activity, List<BaseEntity> model){
        this.model = model;
        if( this.buildingsListAdapter == null ){
            this.buildingsListAdapter = new BuildingsListAdapter(activity, model);
            mListView.setAdapter(this.buildingsListAdapter);
        }else{
            this.buildingsListAdapter.clear();
            this.buildingsListAdapter.addAll(model);
            this.buildingsListAdapter.notifyDataSetChanged();
        }

        if( this.model.size() == 0 ){
            this.mListView.setVisibility(View.INVISIBLE);
            this.emptyText.setText(R.string.no_results);
            this.emptyText.setVisibility(View.VISIBLE);
        }else{
            this.mListView.setVisibility(View.VISIBLE);
            this.emptyText.setText("");
            this.emptyText.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }



    /**
     * Custom list adapter for buildings list
     *
     */

    private class BuildingsListAdapter extends ArrayAdapter<BaseEntity> {
        private final Activity context;
        private final List<BaseEntity> buildings;

        private class BuildingViewHolder{
            public TextView buildingName;
            public TextView buildingAddress;
        }

        public BuildingsListAdapter(Activity context,List<BaseEntity> buildings){
            super( context, R.layout.buildings_list_item , buildings );
            this.context    = context;
            this.buildings  = buildings;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group){
            View buildingRowView = convertView;

            // reuse view
            if( buildingRowView==null ){
                LayoutInflater inflater = context.getLayoutInflater();
                buildingRowView = inflater.inflate(R.layout.buildings_list_item,null);

                // create BuildingViewHolder in order to avoid searching
                // resources within XML and speeds up rendering
                BuildingViewHolder holder = new BuildingViewHolder();
                holder.buildingName     = (TextView) buildingRowView.findViewById(R.id.building_name);
                holder.buildingAddress  = (TextView) buildingRowView.findViewById(R.id.building_address);

                buildingRowView.setTag(holder);
            }

            // fill with building data
            BuildingViewHolder holder = (BuildingViewHolder) buildingRowView.getTag();
            Building building = (Building) this.buildings.get(position);
            holder.buildingName.setText(building.building_name);
            holder.buildingAddress.setText(building.address);


            return buildingRowView;
        }
    }

}
