package it.unimi.unimiplaces.presenters;

import java.util.List;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.core.api.APIDelegateInterface;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.views.BuildingDetailView;

/**
 * BuildingDetailPresenter class. It istantiate an ApiManager objects
 * and retrieve building data from API server
 */
public class BuildingDetailPresenter implements
        APIDelegateInterface,
        Presenter{

    APIManager apiManager;
    BuildingDetailView view;
    Building model;

    public BuildingDetailPresenter(APIManager apiManager,BuildingDetailView detailView){
        this.apiManager = apiManager;
        this.view       = detailView;
    }

    @Override
    public void init(String bid) {
        this.apiManager.buildingByBID(this,bid);
    }

    @Override
    public void filterModelWithFilterAtIndex(int index) {}

    @Override
    public BaseEntity payloadForDetailAtIndex(int index) {
        return model;
    }

    @Override
    public void apiRequestStart() {

    }

    @Override
    public void apiRequestEnd(List<BaseEntity> results) {
        if( results==null ){
            this.view.showNoResultsMessage();
            return;
        }

        this.model = (Building) results.get(0);
        this.view.setBuildingName(this.model.building_name);
        this.view.setBuildingAddress(this.model.address);
    }

    @Override
    public void apiRequestError() {
        this.view.showNoResultsMessage();
    }
}
