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
    PresenterViewInterface listView;
    PresenterViewInterface mapView;

    public static String BUILDINGS_ALL_KEY = "ALL";

    private List<BaseEntity> model;
    private List<BaseEntity> modelFiltered;
    private List<BaseEntity> availableServices;

    public BuildingsPresenter(APIManager apiManager,PresenterViewInterface list, PresenterViewInterface map){
        this.apiManager = apiManager;
        this.listView   = list;
        this.mapView    = map;
    }

    public void initBuildings(){
        apiManager.buildings(this);
    }


    public void initAvailableServices(String lang){
        apiManager.availableServices(this, lang);
    }

    public void buildingsByAvailableService(AvailableService service){
        if( service.key == BUILDINGS_ALL_KEY && this.model!=null ){
            this.mapView.setModel(this.model);
            this.listView.setModel(this.model);
        }
        apiManager.buildingsByAvailableService(this,service);
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

        this.mapView.setModel(this.modelFiltered);
        this.listView.setModel(this.modelFiltered);
    }

    @Override
    public void apiRequestStart() {

    }

    @Override
    public void apiServiceAvailableRequestEnd(List<BaseEntity> results) {
        this.availableServices = results;
    }
}
