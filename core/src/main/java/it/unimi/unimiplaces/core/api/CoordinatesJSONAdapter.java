package it.unimi.unimiplaces.core.api;

import com.squareup.moshi.FromJson;

import it.unimi.unimiplaces.core.model.Coordinates;

/**
 * JSON Adapter used by Moshi to convert coordinates object from JSON
 */
public class CoordinatesJSONAdapter {
    @FromJson
    Coordinates fromJson(double[] coordinates){
        return new Coordinates(coordinates[1],coordinates[0]);
    }
}
