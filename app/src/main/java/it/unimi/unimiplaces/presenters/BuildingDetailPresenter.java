package it.unimi.unimiplaces.presenters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import it.unimi.unimiplaces.APIManager;
import it.unimi.unimiplaces.core.api.APIDelegateInterface;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Floor;
import it.unimi.unimiplaces.views.BuildingDetailViewInterface;

/**
 * BuildingDetailPresenter class. Given an ApiManager object
 * used for retrieving building data from API server, it sets the given view
 */
public class BuildingDetailPresenter implements
        APIDelegateInterface,
        Presenter{

    APIManager apiManager;
    BuildingDetailViewInterface view;
    Building model;

    public BuildingDetailPresenter(APIManager apiManager,BuildingDetailViewInterface detailView){
        this.apiManager = apiManager;
        this.view       = detailView;
    }

    private LinkedHashMap<String,List<String>> prepareFloorsDetailModel(){
        LinkedHashMap<String,List<String>> data = new LinkedHashMap<>();
        String[] rooms;
        for (Floor f:this.model.getFloors()){
            rooms = f.getRoomsNamesList();
            if( rooms != null ){
                data.put(f.floor_name,new ArrayList<>(Arrays.asList(rooms)));
            }else{
                data.put(f.floor_name,new ArrayList());
            }
        }

        return data;
    }

    @Override
    public void init(String bid) {
        this.apiManager.buildingByBID(this,bid);
    }

    @Override
    public void filterModelWithFilterAtIndex(int index) {}

    @Override
    public BaseEntity payloadForDetailAtIndex(int index) { return this.model; }

    public BaseEntity payloadForDetailAtIndex(int floorIndex, int roomIndex) {
        Floor f = this.model.floorAtIndex(floorIndex);
        if( f!=null ){
            return f.roomAtIndex(roomIndex);
        }

        return null;
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

        this.view.setFloorsDetailModel(this.prepareFloorsDetailModel());
    }

    @Override
    public void apiRequestError() {
        this.view.showNoResultsMessage();
    }
}
