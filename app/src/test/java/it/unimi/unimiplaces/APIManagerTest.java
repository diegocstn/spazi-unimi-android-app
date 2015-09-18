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
import it.unimi.unimiplaces.core.api.APIRequest;
import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;

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
        apiManager              = new APIManager(context,apiAsyncTask);
        apiAsyncTask.apiManager = apiManager;
        Mockito.when(context.getString(R.string.progress_loading)).thenReturn("Loading");

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
}