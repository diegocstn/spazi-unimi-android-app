import org.junit.Test;
import org.junit.Assert;

import it.unimi.unimiplaces.core.model.Coordinates;

/**
 * Coordinates Test class
 */
public class CoordinatesTest {

    @Test
    public void coordinatesEqual(){
        Coordinates c1 = new Coordinates(12.0,13.0);
        Coordinates c2 = new Coordinates(12.0,13.0);
        Assert.assertEquals(c1, c2);
    }
}
