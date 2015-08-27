import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import it.unimi.unimiplaces.model.Building;


/**
 * Test APIFactory class.
 */
public class APIFactoryTest {

    public String readFixtureDataFromFile(String filename){
        String contents = null;
        try{
            contents = FileUtils.readFileToString(new File(filename));
        }catch (IOException e){
            System.out.print(e);
        }

        return contents;
    }

    @Test
    public void TestSingleBuildingFactory(){
        String jsonBuilding = readFixtureDataFromFile("data/building_single.json");
        Building building = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        building.setCoordinates(9.194568,45.460998);

    }
}
