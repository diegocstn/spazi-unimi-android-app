import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Floor;
import it.unimi.unimiplaces.core.model.Room;

/**
 * Floor class test
 */
public class FloorTest {

    APIFactory apiFactory;

    @Before
    public void setUp(){
        this.apiFactory = new APIFactory();
    }

    @Test
    public void testFloorErrorJSON(){
        Floor floor = this.apiFactory.makeFloorFromJSON("wtf");
        Assert.assertNull(floor);
    }

    @Test
    public void testFloorName(){
        Floor floor = this.apiFactory.makeFloorFromJSON(TestHelpers.readFixtureDataFromFile("floor.json"));
        Assert.assertEquals(floor.floor_name, "Primo Interrato");

    }

    @Test
    public void testFloorRoomsCount(){
        Floor floor = this.apiFactory.makeFloorFromJSON(TestHelpers.readFixtureDataFromFile("floor.json"));
        Assert.assertEquals(floor.floor_name,"Primo Interrato");
        Assert.assertEquals(floor.roomsCount(),2);
    }

    @Test
    public void testRoomsAtIndex(){
        Floor floor = this.apiFactory.makeFloorFromJSON(TestHelpers.readFixtureDataFromFile("floor.json"));
        Room room   = new Room("1I178","Aula Bron Lab","Aula Informatica");
        Assert.assertEquals(room,floor.roomAtIndex(0));
    }

    @Test
    public void testRoomsAtIndexError(){
        Floor floor = this.apiFactory.makeFloorFromJSON(TestHelpers.readFixtureDataFromFile("floor.json"));
        Assert.assertEquals(null,floor.roomAtIndex(3));
    }

    @Test
    public void testRooms(){
        Floor floor = this.apiFactory.makeFloorFromJSON(TestHelpers.readFixtureDataFromFile("floor.json"));
        List<BaseEntity> rooms = new ArrayList();
        rooms.add(new Room("1I178","Aula Bron Lab","Aula Informatica"));
        rooms.add(new Room("1I096","Aula Manhattan","Aula Informatica"));
        Assert.assertEquals(rooms,floor.getRooms());

    }

    @Test
    public void testNoRooms(){
        Floor floor = this.apiFactory.makeFloorFromJSON(TestHelpers.readFixtureDataFromFile("floor_no_rooms.json"));
        Assert.assertNotNull(floor.getRooms());
        Assert.assertEquals(0,floor.getRooms().size());
    }



}
