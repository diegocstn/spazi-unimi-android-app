package it.unimi.unimiplaces.api;

import com.squareup.moshi.FromJson;

import it.unimi.unimiplaces.model.Coordinates;

/**
 * Buildings JSOn Adapter
 */
public class BuildingsJSONAdapter {
    @FromJson
    Coordinates fromJson(double[] coordinates){
        return new Coordinates(coordinates[0],coordinates[1]);
    }
}
