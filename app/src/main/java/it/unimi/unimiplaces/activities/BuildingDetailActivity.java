package it.unimi.unimiplaces.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.presenters.BuildingDetailPresenter;
import it.unimi.unimiplaces.presenters.Presenter;
import it.unimi.unimiplaces.views.BuildingDetailViewInterface;

public class BuildingDetailActivity extends AppDetailSectionActivity implements BuildingDetailViewInterface {

    private Presenter presenter;
    TextView buildingNameTextView;
    TextView buildingAddressTextView;
    TextView buildingNoResults;
    FloorsDetailAdapter floorsDetailAdapter;
    ExpandableListView floorsDetailListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        buildingNameTextView    = (TextView) findViewById(R.id.building_name);
        buildingAddressTextView = (TextView) findViewById(R.id.building_address);
        buildingNoResults       = (TextView) findViewById(R.id.building_no_results);
        floorsDetailListView    = (ExpandableListView) findViewById(R.id.building_detail_rooms);

        Intent intent = getIntent();
        presenter = new BuildingDetailPresenter(APIManager.APIManagerFactory.createAPIManager(this),this);
        presenter.init( intent.getStringExtra("b_id") );
        this.setUpDetailActivity(intent.getStringExtra("building_name"));
    }

    @Override
    public void setBuildingName(String buildingName) {
        buildingNameTextView.setText(buildingName);
    }

    @Override
    public void setBuildingAddress(String buildingAddress) {
        buildingAddressTextView.setText(buildingAddress);
    }

    @Override
    public void showNoResultsMessage() {
        buildingNameTextView.setVisibility(View.INVISIBLE);
        buildingAddressTextView.setVisibility(View.INVISIBLE);
        buildingNoResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFloorsDetailModel(HashMap<String, List<String>> data) {
        floorsDetailAdapter = new FloorsDetailAdapter(this,data);
        this.floorsDetailListView.setAdapter(floorsDetailAdapter);
    }

    /**
     * FloorsDetailAdapter
     */
    private class FloorsDetailAdapter extends BaseExpandableListAdapter{

        private Context context;
        private List<String> headers;
        private HashMap<String,List<String>> data;

        /**
         * Constructor
         * @param data HashMap in which every entry has as key the group name,
         *             and as value the list of entries in that group
         */
        public FloorsDetailAdapter(Context context,HashMap<String,List<String>> data){
            this.context    = context;
            this.data       = data;
            this.headers    = new ArrayList<>();
            this.headers.addAll(data.keySet());
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.headers.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.data.get(this.headers.get(groupPosition)).get(childPosition);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.data.get(this.headers.get(groupPosition)).size();
        }

        @Override
        public int getGroupCount() {
            return this.data.keySet().size();
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String childText        = (String)this.getChild(groupPosition,childPosition);
            TextView childTextView;

            // inflate layout there's not a recycled view
            if( convertView==null ){
                convertView = getLayoutInflater().inflate(R.layout.building_detail_list_item,null);
            }

            childTextView = (TextView) convertView.findViewById(R.id.building_detail_item);
            childTextView.setText(childText);

            return convertView;

        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String groupText    = (String)this.getGroup(groupPosition);
            TextView groupTextView;

            // inflate layout there's not a recycled view
            if( convertView==null ){
                convertView = getLayoutInflater().inflate(R.layout.building_detail_group_list_item,null);
            }

            groupTextView = (TextView) convertView.findViewById(R.id.building_detail_group);
            groupTextView.setText(groupText);

            return convertView;
        }
    }
}
