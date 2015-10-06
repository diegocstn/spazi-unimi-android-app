package it.unimi.unimiplaces.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.BuildingsPresenter;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.views.BuildingsListView;
import it.unimi.unimiplaces.views.BuildingsMapView;

public class BuildingsFragment extends Fragment {

    private BuildingsPresenter presenter;
    private BuildingsListView buildingsListView;
    private BuildingsMapView buildingsMapView;
    private ToggleButton buildingsModeView;
    private Button filterButton;
    private Spinner filterSpinner;
    private final BuildingsModeView defaultBuildingModeView = BuildingsModeView.BUILDINGS_MODE_VIEW_LIST;
    private View view;

    private enum BuildingsModeView{
        BUILDINGS_MODE_VIEW_LIST,
        BUILDINGS_MODE_VIEW_MAP
    }

    public BuildingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buildings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        this.initialize();
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }



    private void initialize(){

        this.buildingsListView = (BuildingsListView) view.findViewById(R.id.buildings_list_view);
        this.buildingsMapView = (BuildingsMapView) view.findViewById(R.id.buildings_map_view);

        presenter = new BuildingsPresenter(
                APIManager.APIManagerFactory.createAPIManager(getActivity()),
                buildingsListView,
                buildingsMapView);

        presenter.initBuildings();

        filterButton    = (Button) view.findViewById(R.id.buildings_filter);
        filterSpinner   = (Spinner) view.findViewById(R.id.buildings_filter_spinner);

        changeViewMode(defaultBuildingModeView);

        /* initialize toggle button */
        buildingsModeView       = (ToggleButton) view.findViewById(R.id.buildings_view_mode);
        buildingsModeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    changeViewMode(BuildingsModeView.BUILDINGS_MODE_VIEW_MAP);
                } else {
                    changeViewMode(BuildingsModeView.BUILDINGS_MODE_VIEW_LIST);

                }
            }
        });
    }


    private void changeViewMode(BuildingsModeView mode){
        switch (mode){
            case BUILDINGS_MODE_VIEW_LIST:
                buildingsListView.setVisibility(View.VISIBLE);
                buildingsMapView.setVisibility(View.INVISIBLE);

                break;
            case BUILDINGS_MODE_VIEW_MAP:
                buildingsMapView.setVisibility(View.VISIBLE);
                buildingsListView.setVisibility(View.INVISIBLE);
                break;
        }

    }

}
