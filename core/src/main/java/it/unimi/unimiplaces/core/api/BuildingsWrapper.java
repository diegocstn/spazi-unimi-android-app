package it.unimi.unimiplaces.core.api;

import java.util.List;

import it.unimi.unimiplaces.core.model.Building;

/**
 * Buildings JSON Adapter
 */
public class BuildingsWrapper {
    public List<Building> buildings;

    public BuildingsWrapper(List<Building> buildings){
        this.buildings = buildings;
    }
}
