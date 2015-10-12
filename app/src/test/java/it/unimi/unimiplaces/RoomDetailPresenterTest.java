package it.unimi.unimiplaces;

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

import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Floor;
import it.unimi.unimiplaces.core.model.Room;
import it.unimi.unimiplaces.presenters.RoomDetailPresenter;
import it.unimi.unimiplaces.views.RoomDetailViewInterface;

/**
 * RoomDetailPresenter test class
 */
@RunWith(MockitoJUnitRunner.class)
public class RoomDetailPresenterTest {

    @Mock
    RoomDetailViewInterface view;

    @Mock
    APIManager apiManager;

    RoomDetailPresenter presenter;

    @Before
    public void setUp(){
        presenter = new RoomDetailPresenter(apiManager,view);
    }

    public Answer mockAPIAnswerWithData(final List<BaseEntity>entities){
        return new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRequestEnd(entities);
                return null;
            }
        };
    }

    @Test
    public void testSetRoomAttributes(){
        List<BaseEntity> entities = new ArrayList<>();
        Room room           = new Room("123","Aula 1","Aula informatica");
        Building building   = new Building("000","Celoria","via Celoria");
        Floor floor         = new Floor("1","Primo");

        room.setBuildingAttributes(building);
        room.setFloorAttributes(floor);
        entities.add(room);

        Mockito.doAnswer(mockAPIAnswerWithData(entities)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        presenter.init("123", "000");

        Mockito.verify(apiManager).roomByRIDAndBID(presenter, "123", "000");
        Mockito.verify(view).setRoomName(room.room_name);
        Mockito.verify(view).setRoomFloor(room.floor);
        Mockito.verify(view).setRoomCategory(room.cat_name);
        Mockito.verify(view).setBuildingName(building.building_name);
        Mockito.verify(view).setBuildingAddress(building.address);

    }
}
