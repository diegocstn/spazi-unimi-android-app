package it.unimi.unimiplaces;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import it.unimi.unimiplaces.presenters.WhereAmIPresenter;
import it.unimi.unimiplaces.views.WhereAmIViewInterface;

/**
 * WhereAmIPresenter test class
 */

@RunWith(MockitoJUnitRunner.class)
public class WhereAmIPresenterTest {

    WhereAmIPresenter presenter;

    @Mock
    WhereAmIViewInterface view;

    @Mock
    PlacesDataSource dataSource;

    @Before
    public void setUp(){
        presenter = new WhereAmIPresenter(dataSource,view);
    }

    @Test
    public void testSuccessBuildingDecodeURL(){
        String buildingId = "R123";
        Mockito.doReturn(true).when(dataSource).buildingExists(buildingId);
        presenter.decodeData("unimiplaces://building/R123/");
        Mockito.verify(view).onBuildingScanSuccess(buildingId);
    }

    @Test
    public void testSuccessBuildingDecodeURL2(){
        String buildingId = "888313";
        Mockito.doReturn(true).when(dataSource).buildingExists(buildingId);
        presenter.decodeData("unimiplaces://building/888313/");
        Mockito.verify(view).onBuildingScanSuccess(buildingId);
    }

    @Test
    public void testSuccessBuildingDecodeURL3(){
        String buildingId = "AAAAB";
        Mockito.doReturn(true).when(dataSource).buildingExists(buildingId);
        presenter.decodeData("unimiplaces://building/AAAAB/");
        Mockito.verify(view).onBuildingScanSuccess(buildingId);
    }

    @Test
    public void testErrorBuildingDecodeURL1(){
        presenter.decodeData("unimiplaces://buildings/R123/");
        Mockito.verify(view).onScanError();
    }

    @Test
    public void testErrorBuildingDecodeURL2(){
        presenter.decodeData("adsdada://dsada/AAA/");
        Mockito.verify(view).onScanError();
    }


    @Test
    public void testSuccessRoomDecodeURL(){
        String buildingId   = "0000";
        String roomId       = "123";
        Mockito.doReturn(true).when(dataSource).placeExists(buildingId,roomId);
        presenter.decodeData("unimiplaces://room/0000/123/");
        Mockito.verify(view).onRoomScanSuccess(buildingId,roomId);
    }

    @Test
    public void testSuccessRoomDecodeURL2(){
        String buildingId   = "AAA";
        String roomId       = "123";
        Mockito.doReturn(true).when(dataSource).placeExists(buildingId,roomId);
        presenter.decodeData("unimiplaces://room/AAA/123/");
        Mockito.verify(view).onRoomScanSuccess(buildingId,roomId);
    }

    @Test
    public void testSuccessRoomDecodeURL3(){
        String buildingId   = "AA1233";
        String roomId       = "R123";
        Mockito.doReturn(true).when(dataSource).placeExists(buildingId,roomId);
        presenter.decodeData("unimiplaces://room/AA1233/R123/");
        Mockito.verify(view).onRoomScanSuccess(buildingId,roomId);
    }

    @Test
    public void testErrorRoomDecodeURL4(){
        presenter.decodeData("unimiplaces://room/AA1233/");
        Mockito.verify(view).onScanError();
    }

    @Test
    public void testErrorRoomDecodeURL5(){
        presenter.decodeData("adsdaa");
        Mockito.verify(view).onScanError();
    }

    @Test
    public void testRoomNotInDb(){
        String buildingId   = "000";
        String roomId       = "123";
        Mockito.doReturn(false).when(dataSource).placeExists(buildingId,roomId);
        presenter.decodeData("unimiplaces://room/000/123/");
        Mockito.verify(view).onScanError();
    }

    @Test
    public void testBuildingNotInDb(){
        String buildingId   = "000";
        Mockito.doReturn(false).when(dataSource).buildingExists(buildingId);
        presenter.decodeData("unimiplaces://building/000/");
        Mockito.verify(view).onScanError();
    }

}
