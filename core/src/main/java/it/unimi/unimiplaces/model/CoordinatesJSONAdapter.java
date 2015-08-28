package it.unimi.unimiplaces.model;

import com.squareup.moshi.FromJson;

/**
 * JSON Adapter used by Moshi to convert coordinates object from JSON
 */
public class CoordinatesJSONAdapter {
    @FromJson
    Coordinates fromJson(double[] coordinates){
        return new Coordinates(coordinates[0],coordinates[1]);
    }
}
