package it.unimi.unimiplaces.model;

public class Building extends BaseEntity {
    String b_id;
    String building_name;
    String address;
    double latitude;
    double longitude;

    public Building(String b_id, String building_name, String address){
        this.b_id           = b_id;
        this.building_name  = building_name;
        this.address        = address;
    }

    public void setCoordinates(double latitude, double longitude){
        this.latitude   = latitude;
        this.longitude  = longitude;
    }

}
