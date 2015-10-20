import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.RoomEvent;

/**
 * RoomEvent test class
 */
public class RoomEventTest {

    APIFactory apiFactory;

    @Before
    public void setUp(){
        apiFactory = new APIFactory();
    }

    @Test
    public void testCreation(){
        String json                         = TestHelpers.readFixtureDataFromFile("room_events.json");
        List<BaseEntity> actualEvents       = apiFactory.makeRoomEventsFromJSON(json);
        List<BaseEntity> expectedEvents     = new ArrayList<>();
        expectedEvents.add(new RoomEvent("Diritto commerciale (of2) Ediz: Cognomi M-Q", "2015-10-19", "08:30:00","10:30:00"));
        expectedEvents.add(new RoomEvent("Diritto costituzionale (of2) Ediz: Cognomi A-C", "2015-10-19", "10:30:00","12:30:00"));
        Assert.assertEquals(expectedEvents,actualEvents);
    }

    @Test
    public void testTime(){
        String json         = TestHelpers.readFixtureDataFromFile("room_event_single.json");
        RoomEvent roomEvent = (RoomEvent)(apiFactory.makeRoomEventsFromJSON(json)).get(0);

        Assert.assertEquals(roomEvent.getTime(),"08:30 - 10:30");
    }

    @Test
    public void testTimeNonLeadingZero(){
        RoomEvent roomEvent = new RoomEvent("Desc","2015-10-19","11:30:00","12:30:00");
        Assert.assertEquals(roomEvent.getTime(),"11:30 - 12:30");
    }

    @Test
    public void testDate(){
        String json         = TestHelpers.readFixtureDataFromFile("room_event_single.json");
        RoomEvent roomEvent = (RoomEvent)(apiFactory.makeRoomEventsFromJSON(json)).get(0);

        Assert.assertEquals(roomEvent.getDate(),"MONDAY (19-10-2015)");
    }

}
