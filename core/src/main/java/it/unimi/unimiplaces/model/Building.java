package it.unimi.unimiplaces.model;

import java.util.ArrayList;
import java.util.List;

public class Building extends BaseEntity {
    public String b_id;
    public String building_name;
    public String address;
    public Coordinates coordinates;
    private List<Floor> floors;

    public Building(String b_id, String building_name, String address){
        this.b_id           = b_id;
        this.building_name  = building_name;
        this.address        = address;
        this.floors         = new ArrayList<Floor>();
    }


    public void setCoordinates(double lat, double lng){
        this.coordinates = new Coordinates(lat,lng);
    }

    public int numOfFloors(){
        if( this.floors != null ) {
            return this.floors.size();
        }else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        if (!b_id.equals(building.b_id)) return false;
        if (building_name != null ? !building_name.equals(building.building_name) : building.building_name != null)
            return false;
        if (address != null ? !address.equals(building.address) : building.address != null)
            return false;
        return !(coordinates != null ? !coordinates.equals(building.coordinates) : building.coordinates != null);

    }

    @Override
    public int hashCode() {
        int result = b_id.hashCode();
        result = 31 * result + (building_name != null ? building_name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
}
