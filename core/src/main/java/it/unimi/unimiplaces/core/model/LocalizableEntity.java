package it.unimi.unimiplaces.core.model;

/**
 * LocalizableEntity interface, every object that could be
 * geographically localized on map should implement
 * this interface
 */
public interface LocalizableEntity {
    void setCoordinates(double lat, double lng);
    Coordinates getCoordinates();
}
