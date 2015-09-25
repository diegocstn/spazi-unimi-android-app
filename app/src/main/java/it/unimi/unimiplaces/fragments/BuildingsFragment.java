package it.unimi.unimiplaces.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.api.APIDelegateInterfaceExtended;
import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;

public class BuildingsFragment extends Fragment implements APIDelegateInterfaceExtended {

    private List<BaseEntity> model;
    private List<BaseEntity> filteredModel;
    private List<BaseEntity> availableServices;
    private BuildingsListFragment buildingsListFragment;
    private BuildingsMapFragment buildingsMapFragment;
    private ToggleButton buildingsModeView;
    private Button filterButton;
    private Spinner filterSpinner;
    private APIManager apiManager;
    private final BuildingsModeView defaultBuildingModeView = BuildingsModeView.BUILDINGS_MODE_VIEW_LIST;
    private final int AVAILABLE_SERVICES_ALL = 0;

    private enum BuildingsModeView{
        BUILDINGS_MODE_VIEW_LIST,
        BUILDINGS_MODE_VIEW_MAP
    }

    final static String MODEL_KEY = "model";
    final static String AVAILABLE_SERVICE_KEY = "services";

    public BuildingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.apiManager = APIManager.APIManagerFactory.createAPIManager(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buildings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.initialize(view, savedInstanceState);
    }



    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MODEL_KEY, (Serializable) this.model);
        outState.putSerializable(AVAILABLE_SERVICE_KEY,(Serializable)this.availableServices);
        super.onSaveInstanceState(outState);
    }

    public List<BaseEntity> getCurrentModel(){
        if( this.filteredModel != null ){
            return this.filteredModel;
        }else{
            return this.model;
        }
    }

    private void initialize(View view, Bundle savedInstanceState){

        buildingsMapFragment    = new BuildingsMapFragment();
        buildingsListFragment   = new BuildingsListFragment();

        filterButton    = (Button) view.findViewById(R.id.buildings_filter);
        filterSpinner   = (Spinner) view.findViewById(R.id.buildings_filter_spinner);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.buildings_container,buildingsMapFragment);
        fragmentTransaction.add(R.id.buildings_container,buildingsListFragment);
        fragmentTransaction.commit();

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

        /* initialize filter button and spinner */
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( filterSpinner.getVisibility()==View.GONE ){
                    filterSpinner.performClick();
                }else{
                    filterSpinner.setVisibility(View.GONE);
                }
            }
        });

        if( savedInstanceState != null ) {
            this.model              = (List) savedInstanceState.getSerializable(MODEL_KEY);
            this.availableServices  = (List) savedInstanceState.getSerializable(AVAILABLE_SERVICE_KEY);

            /* init model */
            if (this.model == null) {
                apiManager.buildings(this);
            }else{
                buildingsListFragment.setModel(getActivity(), this.model);
                buildingsMapFragment.setModel(getActivity(),this.model);
            }

            /* init available services */
            if( this.availableServices == null ){
                this.startAvailableServicesRequest();
            }else{
                this.initAvailableServices();
            }


        }else{
            apiManager.buildings(this);
            this.startAvailableServicesRequest();
        }
    }


    private void changeViewMode(BuildingsModeView mode){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        switch (mode){
            case BUILDINGS_MODE_VIEW_LIST:
                fragmentTransaction.detach(buildingsMapFragment);
                fragmentTransaction.attach(buildingsListFragment);
                break;
            case BUILDINGS_MODE_VIEW_MAP:
                fragmentTransaction.detach(buildingsListFragment);
                fragmentTransaction.attach(buildingsMapFragment);
                break;
        }

        fragmentTransaction.commit();
    }

    private void startAvailableServicesRequest(){
        String lang = Locale.getDefault().getLanguage();
        this.apiManager.availableServices(this, lang);
    }

    private void initAvailableServices(){
        List <String> servicesLabel = new ArrayList<>();

        servicesLabel.add(getString(R.string.available_service_all));

        for (BaseEntity service : this.availableServices){
            servicesLabel.add( ((AvailableService)service).key );
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.support_simple_spinner_dropdown_item,
                servicesLabel);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        this.filterSpinner.setAdapter(adapter);
    }

    public void filterBuildingsByService(int index){
        this.apiManager.buildingsByAvailableService(this, (AvailableService) this.availableServices.get(index));
    }


    /* APIManagerDelegate methods */
    @Override
    public void apiRequestStart() {

    }

    @Override
    public void apiRequestEnd(List<BaseEntity> results) {

        if( this.model == null ){
            this.model = results;
        }else{
            this.filteredModel = results;
        }

        buildingsListFragment.setModel(getActivity(),this.getCurrentModel());
        buildingsMapFragment.setModel(getActivity(),this.getCurrentModel());
    }

    @Override
    public void apiServiceAvailableRequestEnd(List<BaseEntity> results){
        this.availableServices = results;
        this.initAvailableServices();
    }

    @Override
    public void apiRequestError() {

    }
}
