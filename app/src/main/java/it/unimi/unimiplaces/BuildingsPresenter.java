package it.unimi.unimiplaces;

import java.util.List;

import it.unimi.unimiplaces.core.api.APIDelegateInterfaceExtended;
import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * BuildingsPresenter class
 */
public class BuildingsPresenter implements APIDelegateInterfaceExtended{

    APIManager apiManager;
    PresenterViewInterface listView;
    PresenterViewInterface mapView;

    public BuildingsPresenter(APIManager apiManager,PresenterViewInterface list, PresenterViewInterface map){
        this.apiManager = apiManager;
        this.listView   = list;
        this.mapView    = map;
    }

    public void initBuildings(){
        apiManager.buildings(this);
    }

    @Override
    public void apiRequestError() {

    }

    @Override
    public void apiRequestEnd(List<BaseEntity> results) {
        this.mapView.setModel(results);
        this.listView.setModel(results);
    }

    @Override
    public void apiRequestStart() {

    }

    @Override
    public void apiServiceAvailableRequestEnd(List<BaseEntity> results) {

    }
}
