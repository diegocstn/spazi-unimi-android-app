import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;


/**
 * Test APIFactory class.
 */
public class APIFactoryTest {

    APIFactory apiFactory;


    @Before
    public void setUp(){
        this.apiFactory = new APIFactory();
    }


    @Test
    public void TestSingleBuildingFactory(){
        String jsonBuilding = TestHelpers.readFixtureDataFromFile("building_single.json");
        Building actual = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        actual.setCoordinates(9.194568, 45.460998);

        Building other = (Building) apiFactory.makeBuildingFromJSON(jsonBuilding);

        Assert.assertNotNull(other);
        Assert.assertEquals( actual,other );
    }

    @Test
    public void TestNullBuildingFactory(){
        String jsonBuilding = "{ buildings:[] }";
        List<BaseEntity> actualBuildings = apiFactory.makeBuildingsFromJSON(jsonBuilding);
        Assert.assertNull(actualBuildings);
    }

    @Test
    public void TestFactoryWithOneBuilding(){
        String jsonBuilding = TestHelpers.readFixtureDataFromFile("buildings_1.json");
        List<BaseEntity> expectedBuildings = new ArrayList();
        Building b1 = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        b1.setCoordinates(9.194568, 45.460998);
        expectedBuildings.add(b1);

        List<BaseEntity> actualBuildings = apiFactory.makeBuildingsFromJSON(jsonBuilding);
        Assert.assertNotNull(actualBuildings);
        Assert.assertEquals(expectedBuildings, actualBuildings);
    }

    @Test
    public void TestFactoryWithThreeBuildings(){
        String jsonBuilding = TestHelpers.readFixtureDataFromFile("buildings_3.json");
        List<BaseEntity> expectedBuildings = new ArrayList();

        Building b1 = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        b1.setCoordinates(9.194568, 45.460998);

        Building b2 = new Building("11120","Sant'Antonio","via Sant'Antonio, 5, Milano, 20122");
        b2.setCoordinates(9.193712, 45.461615);

        Building b3 = new Building("11220","Santa Sofia","Via Santa Sofia, 9/11, Milano, 20122");
        b3.setCoordinates(9.191074, 45.456215);

        expectedBuildings.add(b1);
        expectedBuildings.add(b2);
        expectedBuildings.add(b3);

        List<BaseEntity> actualBuildings = apiFactory.makeBuildingsFromJSON(jsonBuilding);
        Assert.assertNotNull(actualBuildings);
        Assert.assertEquals(expectedBuildings,actualBuildings);
    }

    @Test
    public void TestFactoryWithTwentyBuildings() {
        String jsonBuilding = TestHelpers.readFixtureDataFromFile("buildings_20.json");
        Assert.assertEquals(20,apiFactory.makeBuildingsFromJSON(jsonBuilding).size());
    }

    @Test
    public void TestFactoryAvailableServiceSingle(){
        String jsonBuilding         = TestHelpers.readFixtureDataFromFile("services_single.json");
        List<BaseEntity> services   = apiFactory.makeAvailableServicesFromJSON(jsonBuilding);
        AvailableService service    = (AvailableService) services.get(0);
        Assert.assertEquals(1,services.size());
        Assert.assertEquals(service.key,"Aula Seminari - Magna - Lauree");
        Assert.assertEquals(service.label,"Aula Seminari - Magna - Lauree");
    }

    @Test
    public void TestFactoryAvailableServices(){
        String jsonBuilding = TestHelpers.readFixtureDataFromFile("services.json");
        Assert.assertEquals(39,apiFactory.makeAvailableServicesFromJSON(jsonBuilding).size());
    }
}
