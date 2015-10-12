package it.unimi.unimiplaces.views;

/**
 * Room detail view inteface
 */
public interface RoomDetailViewInterface {
    void setRoomName(String roomName);
    void setRoomFloor(String roomFloor);
    void setBuildingName(String buildingName);
    void setBuildingAddress(String buildingAddress);
    void setRoomCategory(String roomCategory);
    void setFloorMapForRoom();
    void showNoResultsMessage();
}
