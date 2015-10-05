package it.unimi.unimiplaces;

import android.app.Activity;

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


    @Test
    public void fetchBuildings(){

        List<BaseEntity> model = new ArrayList<>();
        presenter = new BuildingsPresenter(apiManager,buildingsListFragment,buildingsMapFragment);

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


}
