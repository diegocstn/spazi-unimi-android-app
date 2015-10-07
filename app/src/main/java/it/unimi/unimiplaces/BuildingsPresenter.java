package it.unimi.unimiplaces;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.api.APIDelegateInterfaceExtended;
import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * BuildingsPresenter class
 */
public class BuildingsPresenter implements APIDelegateInterfaceExtended, Presenter {

    APIManager apiManager;
    PresenterViewBuildings view;

    public static int SERVICES_ALL_INDEX = 0;
    public static String SERVICES_ALL_KEY = "ALL";

    private List<BaseEntity> model;
    private List<BaseEntity> modelFiltered;
    private List<BaseEntity> availableServices;

    public BuildingsPresenter(APIManager apiManager,PresenterViewBuildings view){
        this.apiManager = apiManager;
        this.view       = view;
    }

    public void initBuildings(){
        apiManager.buildings(this);
    }


    public void initAvailableServices(String lang){
        apiManager.availableServices(this, lang);
    }

    public void buildingsByAvailableService(AvailableService service){
        if( service.key == SERVICES_ALL_KEY && this.model!=null ){
            this.view.setModel(this.model);
        }
        apiManager.buildingsByAvailableService(this, service);
    }

    public String[] getAvailableServicesLabels(){
        String[] servicesLabel = new String[this.availableServices.size()];
        AvailableService service;
        for(int i=0; i<this.availableServices.size();i++){
            service             = (AvailableService) this.availableServices.get(i);
            servicesLabel[i]    = service.label;
        }

        return servicesLabel;
    }

    /* API Delegate methods */

    @Override
    public void apiRequestError() {

    }

    @Override
    public void apiRequestEnd(List<BaseEntity> results) {
        /* model initialization*/
        if( this.model==null ){
            this.model = results;
        }

        this.modelFiltered  = results;

        this.view.setModel(this.modelFiltered);
    }

    @Override
    public void apiRequestStart() {

    }

    @Override
    public void apiServiceAvailableRequestEnd(List<BaseEntity> results) {
        this.availableServices = new ArrayList<>();
        /* add "All" service value */
        this.availableServices.add(SERVICES_ALL_INDEX,new AvailableService(SERVICES_ALL_KEY,"All"));
        /* add other services */
        this.availableServices.addAll(results);
        this.view.setAvailableServices(this.getAvailableServicesLabels());
    }

    /* Presenter methods */

    @Override
    public void init(String lang) {
        if( this.model==null ) {
            this.initBuildings();
        }
        if( this.availableServices==null ) {
            this.initAvailableServices(lang);
        }
    }

    @Override
    public void showDetailAtIndex(int index) {

    }

    @Override
    public void filterModelWithFilterAtIndex(int index) {
        this.buildingsByAvailableService((AvailableService)this.availableServices.get(index));
    }
}
