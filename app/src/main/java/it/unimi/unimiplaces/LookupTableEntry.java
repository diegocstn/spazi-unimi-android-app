package it.unimi.unimiplaces;

/**
 * LookupTableEntry
 */
public class LookupTableEntry {
    int id;
    public String buildingId;
    public String buildingName;
    public String floorId;
    public String floorName;
    public String roomId;
    public String roomName;

    public LookupTableEntry(){}

    public LookupTableEntry(int id){
        this.id = id;
    }

    public LookupTableEntry(String buildingName,String roomName){
        this.buildingName   = buildingName;
        this.roomName       = roomName;
    }

    public LookupTableEntry(int id, String buildingId, String buildingName, String floorId, String floorName, String roomId, String roomName){
        this.id             = id;
        this.buildingId     = buildingId;
        this.buildingName   = buildingName;
        this.floorId        = floorId;
        this.floorName      = floorName;
        this.roomId         = roomId;
        this.roomName       = roomName;
    }
}
