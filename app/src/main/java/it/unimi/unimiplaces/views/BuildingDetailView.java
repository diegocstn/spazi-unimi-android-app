package it.unimi.unimiplaces.views;

/**
 * Building detail view interface
 */
public interface BuildingDetailView {
    void setBuildingName(String buildingName);
    void setBuildingAddress(String buildingAddress);
    void showNoResultsMessage();
}
