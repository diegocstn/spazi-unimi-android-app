package it.unimi.unimiplaces.core.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.List;

import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Floor;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.core.model.RoomEvent;

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

    private static class BuildingsWrapper {
        public List<Building> buildings;
    }

    private static class RoomEventsWrapper{
        public List<RoomEvent> events;
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

    private static class BuildingsAvailableServices{
        List <AvailableService> services;
    }

    public List<BaseEntity> makeAvailableServicesFromJSON(String json){
        List<? extends BaseEntity> services = null;
        JsonAdapter<BuildingsAvailableServices> jsonAdapter = moshiBuilder.adapter(BuildingsAvailableServices.class);
        try{
            services = (jsonAdapter.fromJson(json)).services;
        }catch (IOException e){
            System.out.println(e);
        }
        return (List<BaseEntity>)services;
    }

    public Room makeRoomFromJSON(String json){
        Room room = null;
        JsonAdapter<Room> jsonAdapter = moshiBuilder.adapter(Room.class);
        try {
            room = jsonAdapter.fromJson(json);
        }catch (IOException e){
            System.out.println(e);
        }

        return room;
    }

    public Floor makeFloorFromJSON(String json){
        Floor floor = null;
        JsonAdapter<Floor> jsonAdapter = moshiBuilder.adapter(Floor.class);
        try {
            floor = jsonAdapter.fromJson(json);
        }catch (IOException e){
            System.out.println(e);
        }
        return floor;
    }

    public List<BaseEntity> makeRoomEventsFromJSON(String json){
        List<? extends BaseEntity> events = null;
        JsonAdapter<RoomEventsWrapper> jsonAdapter = moshiBuilder.adapter(RoomEventsWrapper.class);
        try {
            events = (jsonAdapter.fromJson(json)).events;
        }catch (IOException e){
            System.out.println(e);
        }
        return (List<BaseEntity>)events;
    }
}

