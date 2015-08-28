import org.apache.commons.io.FileUtils;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;

/**
 * Test helpers class
 */
@Ignore
public class TestHelpers {
    final static String fixtureDataFolder = "src/test/data";

    public static String readFixtureDataFromFile(String filename){
        String contents = null;
        File f = new File(fixtureDataFolder,filename);

        try{
            contents = FileUtils.readFileToString(f);
        }catch (IOException e){
            System.out.print(e);
        }

        return contents;
    }
}
