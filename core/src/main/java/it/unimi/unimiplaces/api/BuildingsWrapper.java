package it.unimi.unimiplaces.api;

import java.util.List;

import it.unimi.unimiplaces.model.Building;

/**
 * Buildings JSON Adapter
 */
public class BuildingsWrapper {
    public List<Building> buildings;

    public BuildingsWrapper(List<Building> buildings){
        this.buildings = buildings;
    }
}
