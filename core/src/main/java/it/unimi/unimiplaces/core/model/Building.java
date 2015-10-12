package it.unimi.unimiplaces.core.model;

import java.util.ArrayList;
import java.util.List;

public class Building extends BaseEntity implements LocalizableEntity {
    public String b_id;
    public String building_name;
    public String address;
    public Coordinates coordinates;
    private List<Floor> floors;

    public static final String MODEL_KEY        = "b_id";
    public static final String MODEL_NAME_KEY   = "building_name";

    public Building(String b_id, String building_name, String address){
        this.b_id           = b_id;
        this.building_name  = building_name;
        this.address        = address;
        this.floors         = new ArrayList<Floor>();
    }

    @Override
    public void setCoordinates(double lat, double lng){
        this.coordinates = new Coordinates(lat,lng);
    }

    @Override
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public int numOfFloors(){
        if( this.floors != null ) {
            return this.floors.size();
        }else{
            return 0;
        }
    }

    public Floor floorAtIndex(int index){
        if( index>=this.floors.size() ){
            return null;
        }
        return this.floors.get(index);
    }

    public List<Floor> getFloors(){
        return this.floors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;

        Building building = (Building) o;

        if (b_id != null ? !b_id.equals(building.b_id) : building.b_id != null) return false;
        if (building_name != null ? !building_name.equals(building.building_name) : building.building_name != null)
            return false;
        if (address != null ? !address.equals(building.address) : building.address != null)
            return false;
        return !(coordinates != null ? !coordinates.equals(building.coordinates) : building.coordinates != null);

    }

    @Override
    public int hashCode() {
        int result = b_id != null ? b_id.hashCode() : 0;
        result = 31 * result + (building_name != null ? building_name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        return result;
    }
}
