import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import it.unimi.unimiplaces.api.APIFactory;
import it.unimi.unimiplaces.model.Building;


/**
 * Test APIFactory class.
 */
public class APIFactoryTest {

    APIFactory apiFactory;
    final static String fixtureDataFolder = "src/test/data";

    @Before
    public void setUp(){
        this.apiFactory = new APIFactory();
    }

    public String readFixtureDataFromFile(String filename){
        String contents = null;
        File f = new File(fixtureDataFolder,filename);

        try{
            contents = FileUtils.readFileToString(f);
        }catch (IOException e){
            System.out.print(e);
        }

        return contents;
    }

    @Test
    public void TestSingleBuildingFactory(){
        String jsonBuilding = readFixtureDataFromFile("building_single.json");
        Building building = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        building.setCoordinates(9.194568,45.460998);
        org.junit.Assert.assertSame(building, this.apiFactory.makeBuildingFromJSON(jsonBuilding));
    }
}
