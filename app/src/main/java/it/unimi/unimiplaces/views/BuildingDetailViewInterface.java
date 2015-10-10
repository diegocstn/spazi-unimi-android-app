package it.unimi.unimiplaces.views;

import java.util.HashMap;
import java.util.List;

/**
 * Building detail view interface
 */
public interface BuildingDetailViewInterface {
    void setBuildingName(String buildingName);
    void setBuildingAddress(String buildingAddress);
    void setFloorsDetailModel(HashMap<String,List<String>> data);
    void showNoResultsMessage();
}
