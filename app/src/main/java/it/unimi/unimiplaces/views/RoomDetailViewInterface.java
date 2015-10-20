package it.unimi.unimiplaces.views;

import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * Room detail view interface
 */
public interface RoomDetailViewInterface {
    void setRoomFloor(String roomFloor);
    void setBuildingName(String buildingName);
    void setBuildingAddress(String buildingAddress);
    void setRoomCategory(String roomCategory);
    void hideRoomEquipments();
    void setRoomEquipments(String equipments);
    void setFloorMapForRoom(String SVGdata);
    void showNoResultsMessage();
    void setDisplayAddBookmarksButton(boolean show);
    void onSuccessBookmarkSaved();
    void onErrorBookmarkSaved();
    void showRoomTimetableButton();
    void setRoomTimetableEvents(List<BaseEntity> events);
}
