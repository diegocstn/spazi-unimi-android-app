import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.model.BookmarkableEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;

/**
 * BookmarkableEntity class
 */
public class BookmarksTest {

    @Test
    public void roomBookmark(){
        Room r = new Room("111","RoomName","cat");
        Building b = new Building("000","BuildingName","address");
        r.setBuildingAttributes(b);
        List<String> expected = new ArrayList<>(2);
        expected.add("111");
        expected.add("000");

        Assert.assertEquals(BookmarkableEntity.BOOKMARK_TYPE.ROOM, r.getBookmarkableType());
        Assert.assertEquals("111;000",r.getBookmarkableObjectIdentifier());
        Assert.assertEquals(expected,r.getIdentifierFromBookmarkData("111;000"));
        Assert.assertEquals("RoomName - BuildingName",r.getBookmarkableObjectTitle());
    }

    @Test
    public void buildingBookmark(){
        Building b = new Building("000","BuildingName","address");
        List<String> expected = new ArrayList<>(2);
        expected.add("000");
        Assert.assertEquals(BookmarkableEntity.BOOKMARK_TYPE.BUILDING, b.getBookmarkableType());
        Assert.assertEquals("000",b.getBookmarkableObjectIdentifier());
        Assert.assertEquals(expected,b.getIdentifierFromBookmarkData("000"));
        Assert.assertEquals("BuildingName",b.getBookmarkableObjectTitle());
    }



}
