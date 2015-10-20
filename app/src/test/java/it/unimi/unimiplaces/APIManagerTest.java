package it.unimi.unimiplaces;

import android.app.ProgressDialog;
import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import it.unimi.unimiplaces.core.api.APIDelegateInterface;
import it.unimi.unimiplaces.core.api.APIDelegateInterfaceExtended;
import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.api.APIRequest;
import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Floor;
import it.unimi.unimiplaces.core.model.Room;

/**
 * APIManager test class
 */

@RunWith(MockitoJUnitRunner.class)
public class APIManagerTest{

    @Mock
    Context context;

    @Mock
    APIDelegateInterface delegate;
    @Mock
    APIDelegateInterfaceExtended extendedDelegate;

    @Spy
    APIManager.APIAsyncTask apiAsyncTask;

    @Mock
    ProgressDialog dialog;

    @Mock
    NetworkManager networkManager;

    @Captor
    ArgumentCaptor<List<BaseEntity>> entities;

    APIManager apiManager;

    public void mockAsyncTask(APIRequest.APIRequestIdentifier identifier, final String mockedResponse){
        apiAsyncTask.request                = Mockito.mock(APIRequest.class);
        apiAsyncTask.request.requestType    = identifier;
        Mockito.when(apiAsyncTask.buildAPISyncTask(Mockito.any(APIManager.class))).thenReturn(apiAsyncTask);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                APIManager.APIAsyncTask task = (APIManager.APIAsyncTask) invocation.getMock();
                task.onPostExecute(mockedResponse);
                return null;
            }
        }).when(apiAsyncTask).execute(Mockito.any(APIRequest.class));
    }

    @Before
    public void setUp(){
        apiManager              = new APIManager(context,apiAsyncTask,networkManager);
        apiAsyncTask.apiManager = apiManager;
        Mockito.when(context.getString(R.string.progress_loading)).thenReturn("Loading");
        Mockito.doReturn(true).when(networkManager).isNetworkConnected();

    }

    @Test
    public void testBuildings(){
        String json = "{\"buildings\":[{\"b_id\":\"11020\",\"address\":\"via Festa del Perdono, 3, Milano, 20122\",\"building_name\":\"Festa Del Perdono\"}]}";
        mockAsyncTask(APIRequest.APIRequestIdentifier.BUILDINGS,json);

        apiManager.buildings(delegate);

        Mockito.verify(delegate).apiRequestStart();
        Mockito.verify(delegate).apiRequestEnd(entities.capture());

        Building buildingExpected   = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        Building buildingActual     = (Building) entities.getValue().get(0);

        Assert.assertEquals(buildingExpected.building_name,buildingActual.building_name);
        Assert.assertEquals(buildingExpected.address, buildingActual.address);
    }

    @Test
    public void testAvailableServices(){
        String json = "{\"services\":[ " +
                "{\"key\":\"Aula Seminari - Magna - Lauree\"," +
                "\"label\":\"Aula Seminari - Magna - Lauree\"}," +
                "{\"key\":\"Biblioteca - Mediateca\"," +
                "\"label\":\"Biblioteca - Mediateca\"}]}";

        mockAsyncTask(APIRequest.APIRequestIdentifier.AVAILABLE_SERVICES,json);

        apiManager.availableServices(extendedDelegate, "en");

        Mockito.verify(extendedDelegate).apiRequestStart();
        Mockito.verify(extendedDelegate).apiServiceAvailableRequestEnd(entities.capture());

        Assert.assertEquals(
                new AvailableService("Aula Seminari - Magna - Lauree", "Aula Seminari - Magna - Lauree"),
                entities.getValue().get(0)
        );

        Assert.assertEquals(
                new AvailableService("Biblioteca - Mediateca","Biblioteca - Mediateca"),
                entities.getValue().get(1)
        );
    }

    @Test
    public void testAPIonErrorBuildings(){
        String json = " ";
        mockAsyncTask(APIRequest.APIRequestIdentifier.BUILDINGS,json);
        apiManager.buildings(delegate);
        Mockito.verify(delegate).apiRequestStart();
        Mockito.verify(delegate).apiRequestError();
        Mockito.verify(delegate).apiRequestEnd(entities.capture());

        Assert.assertNull(entities.getValue());
    }

    @Test
    public void testAPIonErrorAvailableServices(){
        String json = " ";
        mockAsyncTask(APIRequest.APIRequestIdentifier.AVAILABLE_SERVICES,json);
        apiManager.availableServices(extendedDelegate, "en");
        Mockito.verify(extendedDelegate).apiRequestStart();
        Mockito.verify(extendedDelegate).apiRequestError();
        Mockito.verify(extendedDelegate).apiServiceAvailableRequestEnd(entities.capture());

        Assert.assertNull(entities.getValue());
    }

    @Test
    public void testBuildingsByAvailableServices(){
        String json = "{\"buildings\":[{\"b_id\":\"11020\",\"address\":\"via Festa del Perdono, 3, Milano, 20122\",\"building_name\":\"Festa Del Perdono\"}]}";
        mockAsyncTask(APIRequest.APIRequestIdentifier.BUILDINGS,json);

        apiManager.buildingsByAvailableService(delegate, new AvailableService("key", "value"));

        Mockito.verify(delegate).apiRequestStart();
        Mockito.verify(delegate).apiRequestEnd(entities.capture());

        Building buildingExpected   = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        Building buildingActual     = (Building) entities.getValue().get(0);

        Assert.assertEquals(buildingExpected.building_name, buildingActual.building_name);
        Assert.assertEquals(buildingExpected.address, buildingActual.address);
    }

    @Test
    public void testBuildingByBuildingID(){
        String json = "{\"b_id\":\"11020\",\"address\":\"via Festa del Perdono, 3, Milano, 20122\",\"building_name\":\"Festa Del Perdono\"}";

        mockAsyncTask(APIRequest.APIRequestIdentifier.BUILDING_BY_BID,json);

        apiManager.buildingByBID(delegate, "11020");

        Mockito.verify(delegate).apiRequestStart();
        Mockito.verify(delegate).apiRequestEnd(entities.capture());

        Building buildingExpected   = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        Building buildingActual     = (Building) entities.getValue().get(0);

        Assert.assertEquals(buildingExpected.building_name, buildingActual.building_name);
        Assert.assertEquals(buildingExpected.address, buildingActual.address);
    }

    @Test
    public void testRoomByRoomID(){
        String json = "{\"accessibility\":\"\",\"b_id\":\"11020\",\"building_address\":\"via Festa del Perdono, 3, Milano, 20122\",\"building_coordinates\":[9.194568,45.460998],\"building_name\":\"Festa Del Perdono\",\"capacity\":\"258\",\"cat_name\":\"Aula Magna\",\"equipments\":[],\"f_id\":\"10\",\"floor\":\"Primo Piano\",\"map\":\"http://spazi.srv.di.unimi.it/static-maps/11020/11020_10.svg\",\"r_id\":\"1067\",\"room_name\":\"Galleria\"}";

        mockAsyncTask(APIRequest.APIRequestIdentifier.ROOM_BY_ID, json);

        apiManager.roomByRIDAndBID(delegate, "1067", "11020");

        Mockito.verify(delegate).apiRequestStart();
        Mockito.verify(delegate).apiRequestEnd(entities.capture());

        Room roomExpected   = new Room("1067","Galleria","Aula Magna");
        roomExpected.setBuildingAttributes(new Building("11020", "Festa Del Perdono", "via Festa del Perdono, 3, Milano, 20122"));
        roomExpected.setFloorAttributes(new Floor("10", "Primo Piano"));

        Room actual         = (Room) entities.getValue().get(0);
        Assert.assertEquals(roomExpected.room_name,actual.room_name);
        Assert.assertEquals(roomExpected.r_id,actual.r_id);
        Assert.assertEquals(roomExpected.building_name,actual.building_name);

    }

    @Test
    public void testFloorMap(){
        String svg = "<svg></svg>";
        mockAsyncTask(APIRequest.APIRequestIdentifier.FLOOR_MAP,svg);
        apiManager.floorMapAtURL(extendedDelegate, "http://svgurl");
        Mockito.verify(extendedDelegate).apiRequestStart();
        Mockito.verify(extendedDelegate).apiFloorMapAtURLEnd(svg);
    }

    @Test
    public void testRoomTimetable(){
        String json = "{\"events\":[{\"day\":\"2015-10-19\",\"from\":\"08:30:00\",\"short_description\":\"Desc1\",\"to\":\"10:30:00\"},{\"day\":\"2015-10-19\",\"from\":\"10:30:00\",\"short_description\":\"Desc2\",\"to\":\"12:30:00\"}]}";
        List <BaseEntity> events = (new APIFactory()).makeRoomEventsFromJSON(json);
        mockAsyncTask(APIRequest.APIRequestIdentifier.ROOM_TIMETABLE,json);
        apiManager.timetableForRoom(extendedDelegate, "123","000");
        Mockito.verify(extendedDelegate).apiRequestStart();
        Mockito.verify(extendedDelegate).apiRoomTimeTableEnd(events);
    }
}