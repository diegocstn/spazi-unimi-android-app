package it.unimi.unimiplaces;

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

        Mockito.verify(apiManager).buildingByBID(presenter,"123");
        Mockito.verify(view).showNoResultsMessage();
    }
}
