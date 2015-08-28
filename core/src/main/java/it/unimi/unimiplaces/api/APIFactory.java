package it.unimi.unimiplaces.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import it.unimi.unimiplaces.model.BaseEntity;
import it.unimi.unimiplaces.model.Building;

/**
 * API object factory, provides an unique access point for BaseEntity objects creation
 */
public class APIFactory {

    Moshi moshiBuilder;

    public APIFactory(){
        this.moshiBuilder = new Moshi.Builder().build();
    }

    public BaseEntity makeBuildingFromJSON(String json){
        BaseEntity building = null;
        JsonAdapter<Building> jsonAdapter = moshiBuilder.adapter(Building.class);
        try {
            building = jsonAdapter.fromJson(json);
        }catch (IOException e){
            System.out.println(e);
        }
        return building;
    }
}
