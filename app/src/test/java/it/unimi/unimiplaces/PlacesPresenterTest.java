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

import it.unimi.unimiplaces.presenters.PlacesPresenter;
import it.unimi.unimiplaces.views.PlacesViewInterface;

/**
 * PlacesPresenterTest test class
 */

@RunWith(MockitoJUnitRunner.class)
public class PlacesPresenterTest {

    PlacesPresenter presenter;

    @Mock
    PlacesViewInterface view;

    @Mock
    PlacesDataSource placesDataSource;


    public Answer mockDataSourceAnswer(final List<LookupTableEntry> entries){
        if( entries==null ){
            return new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    return null;
                }
            };
        }
        return new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return entries;
            }
        };
    }

    @Test
    public void testShowResults(){
        String key      = "key";
        List<LookupTableEntry> entries = new ArrayList<>(2);
        entries.add(new LookupTableEntry("Building1","Room1"));
        entries.add(new LookupTableEntry("Building2","Room2"));

        Mockito.doAnswer(mockDataSourceAnswer(entries)).when(placesDataSource).getEntriesForKey(key);
        presenter = new PlacesPresenter(placesDataSource,view);
        presenter.searchPlacesWithKey(key);

        Mockito.verify(placesDataSource).getEntriesForKey(key);
        Mockito.verify(view).setResults(entries);
    }

    @Test
    public void testShowNoResults(){
        String key      = "key";
        Mockito.doAnswer(mockDataSourceAnswer(null)).when(placesDataSource).getEntriesForKey(key);
        presenter = new PlacesPresenter(placesDataSource,view);
        presenter.searchPlacesWithKey(key);

        Mockito.verify(placesDataSource).getEntriesForKey(key);
        Mockito.verify(view).showNoResults();
    }

    @Test
    public void clearResults(){
        String key = "";
        presenter = new PlacesPresenter(placesDataSource,view);
        presenter.searchPlacesWithKey(key);
        Mockito.verify(placesDataSource,Mockito.never()).buildingExists(key);
        Mockito.verify(view).clearResults();
    }
}
