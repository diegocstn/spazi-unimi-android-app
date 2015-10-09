package it.unimi.unimiplaces.core.model;

import java.util.List;

/**
 * Floor Base Class
 */
public class Floor {
    public String f_id;
    public String floor_name;
    public String map;
    public List<String> available_services;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Floor)) return false;

        Floor floor = (Floor) o;

        if (f_id != null ? !f_id.equals(floor.f_id) : floor.f_id != null) return false;
        if (floor_name != null ? !floor_name.equals(floor.floor_name) : floor.floor_name != null)
            return false;
        if (map != null ? !map.equals(floor.map) : floor.map != null) return false;
        return !(available_services != null ? !available_services.equals(floor.available_services) : floor.available_services != null);

    }

    @Override
    public int hashCode() {
        int result = f_id != null ? f_id.hashCode() : 0;
        result = 31 * result + (floor_name != null ? floor_name.hashCode() : 0);
        result = 31 * result + (map != null ? map.hashCode() : 0);
        result = 31 * result + (available_services != null ? available_services.hashCode() : 0);
        return result;
    }
}






