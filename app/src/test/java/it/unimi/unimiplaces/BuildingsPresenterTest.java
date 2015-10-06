package it.unimi.unimiplaces;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import it.unimi.unimiplaces.fragments.BuildingsListFragment;
import it.unimi.unimiplaces.fragments.BuildingsMapFragment;

/**
 * BuildingsPresenter class
 */

@RunWith(MockitoJUnitRunner.class)
public class BuildingsPresenterTest {

    @Mock
    APIManager apiManager;

    @Mock
    BuildingsListFragment buildingsListFragment;

    @Mock
    BuildingsMapFragment buildingsMapFragment;

    @Mock
    Activity activity;

    BuildingsPresenter presenter;


    @Before
    public void setUpTest(){
        presenter = new BuildingsPresenter(apiManager,buildingsListFragment,buildingsMapFragment);
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
        Mockito.verify(buildingsMapFragment).setModel(model);
        Mockito.verify(buildingsListFragment).setModel(model);

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
        Mockito.verify(apiManager).availableServices(presenter,"en");

    }

    @Test
    public void testFilterBuildingsByService(){

        /* Available services */
        AvailableService serviceAll    = new AvailableService(BuildingsPresenter.BUILDINGS_ALL_KEY,"service1");
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
        Mockito.verify(buildingsListFragment).setModel(entitiesFiltered);
        Mockito.verify(buildingsMapFragment).setModel(entitiesFiltered);

        presenter.buildingsByAvailableService(serviceAll);
        Mockito.verify(buildingsListFragment).setModel(entitiesAll);
        Mockito.verify(buildingsMapFragment).setModel(entitiesAll);
    }


}
