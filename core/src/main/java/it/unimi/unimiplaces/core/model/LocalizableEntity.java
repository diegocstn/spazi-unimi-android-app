package it.unimi.unimiplaces.core.model;

/**
 * LocalizableEntity interface, every object that could be
 * geographically localized on map should implement
 * this interface
 */
public interface LocalizableEntity {
    void setCoordinates(double lat, double lng);
    String getLocalizableTitle();
    String getLocalizableAddress();
    Coordinates getCoordinates();
    String TITLE_KEY            = "loc_title";
    String ADDRESS_KEY          = "loc_address";
    String COORDINATES_LAT_KEY  = "loc_lat";
    String COORDINATES_LNG_KEY  = "loc_lng";
}
