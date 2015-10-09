import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;

/**
 * Room class tests
 */
public class RoomTest {
    APIFactory apiFactory;

    @Before
    public void setUp(){
        this.apiFactory = new APIFactory();
    }

    @Test
    public void testARoom(){
        String json                     = TestHelpers.readFixtureDataFromFile("room.json");
        Room roomExpected               = new Room("1067","Galleria","Aula Magna");
        Building building               = new Building("11020","Festa del Perdono","via Festa del Perdono, 3, Milano, 20122");
        building.setCoordinates(9.194568,45.460998);
        roomExpected.setBuildingAttributes(building);

        Room roomActual = this.apiFactory.makeRoomFromJSON(json);

        Assert.assertEquals(roomExpected.room_name,roomActual.room_name );
        Assert.assertEquals(roomExpected.getCoordinates().lat,roomActual.getCoordinates().lat,0.1);
        Assert.assertEquals(roomExpected.getCoordinates().lng,roomActual.getCoordinates().lng,0.1);
        Assert.assertEquals(roomExpected.r_id,roomActual.r_id);
    }
}
