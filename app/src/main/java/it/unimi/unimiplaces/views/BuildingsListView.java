package it.unimi.unimiplaces.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import it.unimi.unimiplaces.PresenterViewInterface;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;

/**
 * BuildingsFragment child view used for list-mode representation of buildings
 */
public class BuildingsListView extends RelativeLayout implements
        PresenterViewInterface,
        AdapterView.OnItemClickListener{

    private AbsListView listView;
    private List<BaseEntity> model;
    private BuildingsListAdapter buildingsListAdapter;
    private TextView emptyText;
    private Context context;
    private PresenterViewInterface parentPresenter;

    public BuildingsListView(Context context) {
        super(context);
        this.context = context;
        this.init();
    }

    public BuildingsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.init();
    }

    private void init(){
        inflate(getContext(), R.layout.view_buildings_list, this);
        this.listView = (AbsListView) findViewById(android.R.id.list);
        this.emptyText   = (TextView) findViewById(android.R.id.empty);

        this.listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.onDetailActionListener(position);
    }

    @Override
    public void setModel(List<BaseEntity> model){
        this.model = model;
        if( this.buildingsListAdapter == null ){
            this.buildingsListAdapter = new BuildingsListAdapter(this.context, model);
            listView.setAdapter(this.buildingsListAdapter);
        }else{
            this.buildingsListAdapter.clear();
            this.buildingsListAdapter.addAll(model);
            this.buildingsListAdapter.notifyDataSetChanged();
        }

        if( this.model.size() == 0 ){
            this.listView.setVisibility(View.INVISIBLE);
            this.emptyText.setText(R.string.no_results);
            this.emptyText.setVisibility(View.VISIBLE);
        }else{
            this.listView.setVisibility(View.VISIBLE);
            this.emptyText.setText("");
            this.emptyText.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void clearListeners() {
        this.listView.setOnItemClickListener(null);
        this.parentPresenter = null;
    }

    @Override
    public void setDetailActionListener(PresenterViewInterface listener){
        this.parentPresenter = listener;
    }
    @Override
    public void onDetailActionListener(int i) {
        this.parentPresenter.onDetailActionListener(i);
    }


    /**
     * Custom list adapter for buildings list
     *
     */

    private class BuildingsListAdapter extends ArrayAdapter<BaseEntity> {
        private final Context context;
        private final List<BaseEntity> buildings;

        private class BuildingViewHolder{
            public TextView buildingName;
            public TextView buildingAddress;
        }

        public BuildingsListAdapter(Context context,List<BaseEntity> buildings){
            super( context, R.layout.buildings_list_item , buildings );
            this.context    = context;
            this.buildings  = buildings;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group){
            View buildingRowView = convertView;

            // reuse view
            if( buildingRowView==null ){
                LayoutInflater inflater = LayoutInflater.from(context);
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
