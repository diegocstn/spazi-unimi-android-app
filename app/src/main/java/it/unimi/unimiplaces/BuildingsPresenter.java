package it.unimi.unimiplaces;

import java.util.List;

import it.unimi.unimiplaces.core.api.APIDelegateInterfaceExtended;
import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * BuildingsPresenter class
 */
public class BuildingsPresenter implements APIDelegateInterfaceExtended{

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
        String[] servicesLabel = new String[this.availableServices.size()+1];
        AvailableService service;

        servicesLabel[SERVICES_ALL_INDEX] = "All";
        for(int i=0; i<this.availableServices.size();i++){
            service             = (AvailableService) this.availableServices.get(i);
            servicesLabel[i+1]    = service.label;
        }

        return servicesLabel;
    }

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
        this.availableServices = results;
        this.view.setAvailableServices(this.getAvailableServicesLabels());
    }
}
