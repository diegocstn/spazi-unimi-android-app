package it.unimi.unimiplaces.core.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.List;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;

/**
 * API object factory, provides an unique access point for BaseEntity objects creation
 */
public class APIFactory {

    Moshi moshiBuilder;

    public APIFactory(){
        this.moshiBuilder = new Moshi.Builder()
                .add(new CoordinatesJSONAdapter())
                .build();

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

    public List<BaseEntity> makeBuildingsFromJSON(String json){
        List<? extends BaseEntity> buildings;
        List<BaseEntity> res = null;
        JsonAdapter<BuildingsWrapper> jsonAdapter = moshiBuilder.adapter(BuildingsWrapper.class);
        try {
            buildings = (jsonAdapter.fromJson(json)).buildings;
            res = (List<BaseEntity>)buildings;
        }catch (IOException e){
            System.out.println(e);
        }
        return res;
    }
}
