package it.unimi.unimiplaces.views;

/**
 * Room detail view interface
 */
public interface RoomDetailViewInterface {
    void setRoomName(String roomName);
    void setRoomFloor(String roomFloor);
    void setBuildingName(String buildingName);
    void setBuildingAddress(String buildingAddress);
    void setRoomCategory(String roomCategory);
    void hideRoomEquipments();
    void setRoomEquipments(String equipments);
    void setFloorMapForRoom(String svgURl);
    void showNoResultsMessage();
}
