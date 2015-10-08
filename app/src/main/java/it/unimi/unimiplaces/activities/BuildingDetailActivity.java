package it.unimi.unimiplaces.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.presenters.BuildingDetailPresenter;
import it.unimi.unimiplaces.presenters.Presenter;
import it.unimi.unimiplaces.views.BuildingDetailViewInterface;

public class BuildingDetailActivity extends AppDetailSectionActivity implements BuildingDetailViewInterface {

    private Presenter presenter;
    TextView buildingNameTextView;
    TextView buildingAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_detail);

        buildingNameTextView    = (TextView) findViewById(R.id.building_name);
        buildingAddressTextView = (TextView) findViewById(R.id.building_address);

        Intent intent = getIntent();
        presenter = new BuildingDetailPresenter(APIManager.APIManagerFactory.createAPIManager(this),this);
        presenter.init( intent.getStringExtra("b_id") );
        this.setUpDetailActivity( intent.getStringExtra("building_name") );
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

    }
}
