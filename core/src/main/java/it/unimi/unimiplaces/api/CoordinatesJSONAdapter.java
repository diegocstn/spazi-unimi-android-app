package it.unimi.unimiplaces.api;

import com.squareup.moshi.FromJson;

import it.unimi.unimiplaces.model.Coordinates;

/**
 * JSON Adapter used by Moshi to convert coordinates object from JSON
 */
public class CoordinatesJSONAdapter {
    @FromJson
    Coordinates fromJson(double[] coordinates){
        return new Coordinates(coordinates[0],coordinates[1]);
    }
}
