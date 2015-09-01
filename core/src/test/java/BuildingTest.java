import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.model.Building;

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
}
