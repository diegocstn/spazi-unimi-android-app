package it.unimi.unimiplaces;

import android.app.Activity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;

/**
 * BuildingsPresenter class
 */

@RunWith(MockitoJUnitRunner.class)
public class BuildingsPresenterTest {

    @Mock
    APIManager apiManager;

    @Mock
    PresenterViewBuildings view;

    @Mock
    Activity activity;

    BuildingsPresenter presenter;

    @Captor
    ArgumentCaptor<List<BaseEntity>> entitiesCaptor;


    @Before
    public void setUpTest(){
        presenter = new BuildingsPresenter(apiManager,view);
    }

    @Test
    public void fetchBuildings(){

        List<BaseEntity> model = new ArrayList<>();
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<BaseEntity> entities = new ArrayList<BaseEntity>();
                presenter.apiRequestEnd(entities);
                return null;
            }
        }).when(apiManager).buildings(presenter);

        presenter.initBuildings();

        Mockito.verify(apiManager).buildings(presenter);
        Mockito.verify(view).setModel(model);

    }

    @Test
    public void initAvailableServices(){
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<BaseEntity> entities = new ArrayList<>();
                presenter.apiServiceAvailableRequestEnd(entities);
                return null;
            }
        }).when(apiManager).buildings(presenter);

        presenter.initAvailableServices("en");
        Mockito.verify(apiManager).availableServices(presenter, "en");

    }

    @Test
    public void availableServicesKeyShouldContainsAllKey(){

        String[] expectedServices = new String[3];
        expectedServices[0] = "All";
        expectedServices[1] = "Service1";
        expectedServices[2] = "Service2";

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<BaseEntity> entities = new ArrayList<>();
                entities.add(new AvailableService("s1","Service1"));
                entities.add(new AvailableService("s2","Service2"));
                presenter.apiServiceAvailableRequestEnd(entities);
                return null;
            }
        }).when(apiManager).availableServices(presenter,"en");

        presenter.initAvailableServices("en");
        Mockito.verify(apiManager).availableServices(presenter, "en");
        Assert.assertArrayEquals(presenter.getAvailableServicesLabels(), expectedServices);
    }

    @Test
    public void testFilterBuildingsByService(){

        /* Available services */
        AvailableService serviceAll    = new AvailableService(BuildingsPresenter.SERVICES_ALL_KEY,"service1");
        AvailableService serviceS1     = new AvailableService("s1","service1");

        /* Buildings */
        final List<BaseEntity> entitiesFiltered = new ArrayList<>();
        entitiesFiltered.add(new Building("b1", "Building 1", "address 1"));
        entitiesFiltered.add(new Building("b2", "Building 2", "address 2"));

        final List<BaseEntity> entitiesAll = new ArrayList<>();
        entitiesFiltered.add(new Building("b1", "Building 1", "address 1"));
        entitiesFiltered.add(new Building("b2", "Building 2", "address 2"));
        entitiesFiltered.add(new Building("b3", "Building 3", "address 3"));

        /* Mocked answer for apiManager */
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRequestEnd(entitiesFiltered);
                return null;
            }
        }).when(apiManager).buildingsByAvailableService(presenter, serviceS1);

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRequestEnd(entitiesAll);
                return null;
            }
        }).when(apiManager).buildingsByAvailableService(presenter, serviceAll);

        /* check filtering */
        presenter.buildingsByAvailableService(serviceS1);
        Mockito.verify(apiManager).buildingsByAvailableService(presenter, serviceS1);
        Mockito.verify(view).setModel(entitiesFiltered);

        presenter.buildingsByAvailableService(serviceAll);
        Mockito.verify(view).setModel(entitiesAll);
    }

    @Test
    public void testDetailPayload(){
        final List<BaseEntity> entities = new ArrayList<>();
        BaseEntity target = new Building("b1", "Building 1", "address 1");
        entities.add(target);
        entities.add(new Building("b2", "Building 2", "address 2"));
        entities.add(new Building("b3", "Building 3", "address 3"));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRequestEnd(entities);
                return null;
            }
        }).when(apiManager).buildings(presenter);

        presenter.initBuildings();
        Mockito.verify(apiManager).buildings(presenter);
        Assert.assertEquals(presenter.payloadForDetailAtIndex(0),target);

    }


}
