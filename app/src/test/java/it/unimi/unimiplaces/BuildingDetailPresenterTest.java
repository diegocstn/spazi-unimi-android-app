package it.unimi.unimiplaces;

import junit.framework.Assert;

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.presenters.BuildingDetailPresenter;
import it.unimi.unimiplaces.views.BuildingDetailViewInterface;


/**
 * BuildingDetailPresenter test class
 */
@RunWith(MockitoJUnitRunner.class)
public class BuildingDetailPresenterTest {

    @Mock
    BuildingDetailViewInterface view;

    @Mock
    APIManager apiManager;

    APIFactory factory;

    @Captor
    ArgumentCaptor<LinkedHashMap<String,List<String>>> floorsDetail;

    public String jsonBuilding(){
        String json = "";
        json = "{\"address\":\"via Festa del Perdono, 3, Milano, 20122\",\"b_id\":\"11020\",\"building_name\":\"Festa Del Perdono\",\n" +
                "\"floors\":[\n" +
                "\t{\"f_id\":\"-10\",\"floor_name\":\"Primo Interrato\", \"rooms\":[{\"accessibility\":\"0\",\"capacity\":\"63\",\"cat_name\":\"Aula Informatica\",\"equipments\":[],\"r_id\":\"1I178\",\"room_name\":\"Aula Bron Lab\"},\n" +
                "{\"accessibility\":\"0\",\"capacity\":\"52\",\"cat_name\":\"Aula Informatica\",\"equipments\":[],\"r_id\":\"1I096\",\"room_name\":\"Aula Manhattan\"}\n" +
                "]},\n" +
                "{\"f_id\":\"0\",\"floor_name\":\"Piano Terra\", \"rooms\":[\n" +
                "{\"accessibility\":\"0\",\"capacity\":\"63\",\"cat_name\":\"Aula Informatica\",\"equipments\":[],\"r_id\":\"1I178\",\"room_name\":\"Aula 1\"},\n" +
                "{\"accessibility\":\"0\",\"capacity\":\"52\",\"cat_name\":\"Aula Informatica\",\"equipments\":[],\"r_id\":\"1I096\",\"room_name\":\"Aula 2\"}\n" +
                "]}\n" +
                "]}";
        return json;
    }

    @Before
    public void setUp(){
        factory = new APIFactory();
    }

    @Test
    public void presenterShouldSetModel(){
        final BuildingDetailPresenter presenter = new BuildingDetailPresenter(apiManager,view);
        final List<BaseEntity> model = new ArrayList<>();
        final Building building = new Building("123","Celoria21","via Celoria 21");
        building.setCoordinates(123.0,321.0);
        model.add(building);

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRequestEnd(model);
                return null;
            }
        }).when(apiManager).buildingByBID(presenter, "123");

        presenter.init("123");

        Mockito.verify(apiManager).buildingByBID(presenter,"123");
        Mockito.verify(view).setBuildingName("Celoria21");
        Mockito.verify(view).setBuildingAddress("via Celoria 21");
    }

    @Test
    public void presenterShouldShowErrorMessage(){
        final BuildingDetailPresenter presenter = new BuildingDetailPresenter(apiManager,view);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRequestEnd(null);
                return null;
            }
        }).when(apiManager).buildingByBID(presenter, "123");

        presenter.init("123");

        Mockito.verify(apiManager).buildingByBID(presenter, "123");
        Mockito.verify(view).showNoResultsMessage();
    }

    @Test
    public void presenterShouldPrepareFloorsList(){
        final BuildingDetailPresenter presenter = new BuildingDetailPresenter(apiManager,view);
        final Building building = (Building) factory.makeBuildingFromJSON(this.jsonBuilding());

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ArrayList<BaseEntity> model = new ArrayList<BaseEntity>();
                model.add(building);
                presenter.apiRequestEnd(model);
                return null;
            }
        }).when(apiManager).buildingByBID(presenter, "123");


        presenter.init("123");

        Mockito.verify(view).setFloorsDetailModel(floorsDetail.capture());

        HashMap<String,List<String>> expected = new HashMap<>();
        ArrayList<String> f1 = new ArrayList<>();
        f1.add("Aula Bron Lab");
        f1.add("Aula Manhattan");
        expected.put("Primo Interrato", f1);
        ArrayList<String> f2 = new ArrayList<>();
        f2.add("Aula 1");
        f2.add("Aula 2");
        expected.put("Piano Terra", f2);

        List<String> expectedHeaders = new ArrayList<>();
        expectedHeaders.add("Primo Interrato");
        expectedHeaders.add("Piano Terra");

        LinkedHashMap<String,List<String>> actual = floorsDetail.getValue();
        Assert.assertEquals(expectedHeaders.get(0),actual.keySet().toArray()[0]);
        Assert.assertEquals(expectedHeaders.get(1),actual.keySet().toArray()[1]);
        Assert.assertEquals(expected,actual);

    }

    @Test
    public void testBuildingDetailPayload(){
        final BuildingDetailPresenter presenter = new BuildingDetailPresenter(apiManager,view);
        final Building building = (Building) factory.makeBuildingFromJSON(this.jsonBuilding());

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ArrayList<BaseEntity> model = new ArrayList<BaseEntity>();
                model.add(building);
                presenter.apiRequestEnd(model);
                return null;
            }
        }).when(apiManager).buildingByBID(presenter, "123");


        presenter.init("123");

        Room r = new Room("1I096","Aula 2","Aula Informatica");
        Assert.assertEquals(r,presenter.payloadForDetailAtIndex(1,1));

    }

}
