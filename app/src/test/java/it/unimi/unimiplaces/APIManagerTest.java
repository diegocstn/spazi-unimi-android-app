package it.unimi.unimiplaces;

import android.app.ProgressDialog;
import android.content.Context;

import org.junit.Assert;
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
import it.unimi.unimiplaces.core.api.APIRequest;
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

    @Spy
    APIManager.APIAsyncTask apiAsyncTask;

    @Mock
    ProgressDialog dialog;

    @Captor
    ArgumentCaptor<List<BaseEntity>> entities;


    @Test
    public void testBuildings(){
        APIManager apiManager   = new APIManager(context,apiAsyncTask);
        apiAsyncTask.apiManager = apiManager;
        apiAsyncTask.request    = Mockito.mock(APIRequest.class);
        apiAsyncTask.request.requestType = APIRequest.APIRequestIdentifier.BUILDINGS;


        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                APIManager.APIAsyncTask task = (APIManager.APIAsyncTask) invocation.getMock();
                task.onPostExecute("{\"buildings\":[{\"b_id\":\"11020\",\"address\":\"via Festa del Perdono, 3, Milano, 20122\",\"building_name\":\"Festa Del Perdono\"}]}");
                return null;
            }
        }).when(apiAsyncTask).execute(Mockito.any(APIRequest.class));

        Mockito.when(context.getString(R.string.progress_loading)).thenReturn("Loading");
        Mockito.when(apiAsyncTask.buildAPISyncTask(Mockito.any(APIManager.class))).thenReturn(apiAsyncTask);

        apiManager.buildings(delegate);

        Mockito.verify(delegate).apiRequestStart();
        Mockito.verify(delegate).apiRequestEnd(entities.capture());

        Building buildingExpected   = new Building("11020","Festa Del Perdono","via Festa del Perdono, 3, Milano, 20122");
        Building buildingActual     = (Building) entities.getValue().get(0);

        Assert.assertEquals(buildingExpected.building_name,buildingActual.building_name);
        Assert.assertEquals(buildingExpected.address,buildingActual.address);
    }

    @Test
    public void testAvailableServices(){}
}