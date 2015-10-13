import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.model.Room;

/**
 * BookmarkableEntity class
 */
public class BookmarksTest {

    @Test
    public void roomBookmark(){
        Room r = new Room("111","RoomName","cat");
        r.setB_id("000");
        List<String> expected = new ArrayList<>(2);
        expected.add("111");
        expected.add("000");

        Assert.assertEquals("ROOM",r.getBookmarkableType());
        Assert.assertEquals("111;000",r.getBookmarkableObjectIdentifier());
        Assert.assertEquals(expected,r.getIdentifierFromBookmarkData("111;000"));
    }

    

}
