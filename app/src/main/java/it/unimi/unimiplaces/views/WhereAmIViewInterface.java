package it.unimi.unimiplaces.views;

/**
 * WhereAmIViewInterface
 */
public interface WhereAmIViewInterface {
    void onScanError();
    void onBuildingScanSuccess(String b_id);
    void onRoomScanSuccess(String b_id, String r_id);
}
