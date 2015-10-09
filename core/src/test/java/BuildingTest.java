import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Floor;

/**
 * Building class tests
 */
public class BuildingTest {
    APIFactory apiFactory;

    @Before
    public void setUp(){
        this.apiFactory = new APIFactory();
    }

    @Test
    public void testBuildingFloors(){
        Building building = (Building) this.apiFactory.makeBuildingFromJSON(TestHelpers.readFixtureDataFromFile("building_single.json"));
        Assert.assertTrue(building.numOfFloors()==7);
    }

    @Test
    public void testFloors(){
        Building building   = (Building) this.apiFactory.makeBuildingFromJSON(TestHelpers.readFixtureDataFromFile("building_single.json"));
        Floor f0            = building.floorAtIndex(0);
        Floor f3            = building.floorAtIndex(5);
        Assert.assertNotNull(f0);
        Assert.assertNotNull(f3);
        Assert.assertEquals("http://spazi.srv.di.unimi.it/static-maps/11020/11020_-20.svg", f0.map);
        Assert.assertEquals("http://spazi.srv.di.unimi.it/static-maps/11020/11020_30.svg",f3.map);

        Assert.assertNull(building.floorAtIndex(7));
        Assert.assertNull(building.floorAtIndex(11));
    }

}
