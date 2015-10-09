package it.unimi.unimiplaces.core.model;

import java.util.List;

/**
 * Room class
 */
public class Room extends BaseEntity implements LocalizableEntity {

    public String r_id;
    public String room_name;
    public String f_id;
    private String b_id;
    public String building_name;
    public String building_address;
    private Coordinates building_coordinates;
    private List<String> equipments;
    public String accessibility;
    public String cat_name;


    public Room(String r_id,String name,String cat){
        this.r_id       = r_id;
        this.room_name  = name;
        this.cat_name   = cat;
    }

    public void setBuildingAttributes(Building building){
        this.building_coordinates   = building.getCoordinates();
        this.building_address       = building.address;
        this.building_name          = building.building_name;
    }


    public void setEquipments(List<String> equipments){
        this.equipments = equipments;
    }

    @Override
    public Coordinates getCoordinates() {
        return this.building_coordinates;
    }

    @Override
    public void setCoordinates(double lat, double lng) {
        this.building_coordinates = new Coordinates(lat,lng);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;

        Room room = (Room) o;

        if (r_id != null ? !r_id.equals(room.r_id) : room.r_id != null) return false;
        if (room_name != null ? !room_name.equals(room.room_name) : room.room_name != null)
            return false;
        if (f_id != null ? !f_id.equals(room.f_id) : room.f_id != null) return false;
        if (b_id != null ? !b_id.equals(room.b_id) : room.b_id != null) return false;
        if (building_name != null ? !building_name.equals(room.building_name) : room.building_name != null)
            return false;
        if (building_address != null ? !building_address.equals(room.building_address) : room.building_address != null)
            return false;
        if (building_coordinates != null ? !building_coordinates.equals(room.building_coordinates) : room.building_coordinates != null)
            return false;
        if (equipments != null ? !equipments.equals(room.equipments) : room.equipments != null)
            return false;
        if (accessibility != null ? !accessibility.equals(room.accessibility) : room.accessibility != null)
            return false;
        return !(cat_name != null ? !cat_name.equals(room.cat_name) : room.cat_name != null);

    }

    @Override
    public int hashCode() {
        int result = r_id != null ? r_id.hashCode() : 0;
        result = 31 * result + (room_name != null ? room_name.hashCode() : 0);
        result = 31 * result + (f_id != null ? f_id.hashCode() : 0);
        result = 31 * result + (b_id != null ? b_id.hashCode() : 0);
        result = 31 * result + (building_name != null ? building_name.hashCode() : 0);
        result = 31 * result + (building_address != null ? building_address.hashCode() : 0);
        result = 31 * result + (building_coordinates != null ? building_coordinates.hashCode() : 0);
        result = 31 * result + (equipments != null ? equipments.hashCode() : 0);
        result = 31 * result + (accessibility != null ? accessibility.hashCode() : 0);
        result = 31 * result + (cat_name != null ? cat_name.hashCode() : 0);
        return result;
    }
}
