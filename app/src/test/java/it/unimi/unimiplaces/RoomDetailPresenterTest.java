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
import it.unimi.unimiplaces.core.model.RoomEvent;
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

    @Mock
    BookmarksDataSource bookmarksDataSource;

    RoomDetailPresenter presenter;

    @Before
    public void setUp(){
        presenter = new RoomDetailPresenter(apiManager,view,bookmarksDataSource);
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
        Mockito.verify(view).setRoomFloor(room.floor);
        Mockito.verify(view).setRoomCategory(room.cat_name);
        Mockito.verify(view).setBuildingName(building.building_name);
        Mockito.verify(view).setBuildingAddress(building.address);

    }

    @Test
    public void shouldShowErrorMessageIfNoRoomProvided(){
        Mockito.doAnswer(mockAPIAnswerWithData(null)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        presenter.init("123", "000");
        Mockito.verify(apiManager).roomByRIDAndBID(presenter, "123", "000");
        Mockito.verify(view).showNoResultsMessage();
    }

    @Test
    public void testSetRoomEquipments(){
        List<BaseEntity> entities = new ArrayList<>();
        Room room               = new Room("123","Aula 1","Aula informatica");
        List<String> equipments = new ArrayList<>();
        equipments.add("projector");
        equipments.add("desks");
        equipments.add("knife");
        room.setEquipments(equipments);
        entities.add(room);
        Mockito.doAnswer(mockAPIAnswerWithData(entities)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        presenter.init("123", "000");

        Mockito.verify(apiManager).roomByRIDAndBID(presenter, "123", "000");
        Mockito.verify(view).setRoomEquipments("projector, desks, knife");
    }

    @Test
    public void testHideRoomEquipments(){
        List<BaseEntity> entities = new ArrayList<>();
        Room room               = new Room("123","Aula 1","Aula informatica");
        List<String> equipments = new ArrayList<>();
        room.setEquipments(equipments);
        entities.add(room);
        Mockito.doAnswer(mockAPIAnswerWithData(entities)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        presenter.init("123", "000");

        Mockito.verify(apiManager).roomByRIDAndBID(presenter, "123", "000");
        Mockito.verify(view).hideRoomEquipments();
    }

    @Test
    public void testHideRoomEquipmentsWhenNull(){
        List<BaseEntity> entities = new ArrayList<>();
        Room room                 = new Room("123","Aula 1","Aula informatica");
        room.setEquipments(null);
        entities.add(room);
        Mockito.doAnswer(mockAPIAnswerWithData(entities)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        presenter.init("123", "000");

        Mockito.verify(apiManager).roomByRIDAndBID(presenter, "123", "000");
        Mockito.verify(view).hideRoomEquipments();
    }

    @Test
    public void shouldHideBookmarksFab(){
        Mockito.doAnswer(mockAPIAnswerWithData(null)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        Mockito.when(bookmarksDataSource.entityIsBookmarked(Mockito.any(Room.class))).thenReturn(true);

        presenter.init("123", "000");
        Mockito.verify(view).setDisplayAddBookmarksButton(false);
    }

    @Test
    public void shouldShowBookmarksFab(){
        Mockito.doAnswer(mockAPIAnswerWithData(null)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        Mockito.when(bookmarksDataSource.entityIsBookmarked(Mockito.any(Room.class))).thenReturn(false);

        presenter.init("123","000");
        Mockito.verify(view).setDisplayAddBookmarksButton(true);
    }

    @Test
    public void shouldGetMapSVG(){
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<BaseEntity> entities = new ArrayList<>();
                Room room                 = new Room("123","Aula 1","Aula informatica");
                entities.add(room);
                room.map = "http://mapurl.com";
                presenter.apiRequestEnd(entities);
                return null;
            }
        }).when(apiManager).roomByRIDAndBID(presenter, "123", "000");
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiFloorMapAtURLEnd("<svg></svg>");
                return null;
            }
        }).when(apiManager).floorMapAtURL(presenter, "http://mapurl.com");

        presenter.init("123", "000");

        Mockito.verify(view).setFloorMapForRoom("<svg></svg>");
    }

    @Test
    public void checkRoomTimeTable(){
        List<BaseEntity> entities = new ArrayList<>();
        Room room                 = new Room("123","Aula 1","Aula informatica");
        entities.add(room);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRoomTimeTableEnd(null);
                return null;
            }
        }).when(apiManager).timetableForRoom(presenter, "123", "000");
        presenter.init("123", "000");
    }

    @Test
    public void checkRoomTimeTableExists(){
        List<BaseEntity> entities = new ArrayList<>();
        Room room                 = new Room("123","Aula 1","Aula informatica");
        entities.add(room);
        Mockito.doAnswer(mockAPIAnswerWithData(entities)).when(apiManager)
                .roomByRIDAndBID(presenter, "123", "000");

        final List<BaseEntity> events = new ArrayList<>();
        events.add(new RoomEvent("Lez1","2016-05-14","08:30:00","12:30:00"));
        events.add(new RoomEvent("Lez2", "2016-05-14", "14:30:00", "17:30:00"));
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                presenter.apiRoomTimeTableEnd(events);
                return null;
            }
        }).when(apiManager).timetableForRoom(presenter, "123", "000");
        presenter.init("123", "000");
        Mockito.verify(view).showRoomTimetableButton();
        Mockito.verify(view).setRoomTimetableEvents(events);
    }
}
