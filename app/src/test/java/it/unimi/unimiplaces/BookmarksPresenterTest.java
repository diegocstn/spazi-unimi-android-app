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

import it.unimi.unimiplaces.presenters.BookmarksPresenter;
import it.unimi.unimiplaces.views.BookmarksViewInterface;

/**
 * BookmarksPresenter test class
 */
@RunWith(MockitoJUnitRunner.class)
public class BookmarksPresenterTest {

    BookmarksPresenter presenter;

    @Mock
    BookmarkDataSource bookmarkDataSource;

    @Mock
    BookmarksViewInterface view;

    @Test
    public void testEmptyModel(){
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return new ArrayList<Bookmark>();
            }
        }).when(bookmarkDataSource).allBookmarks();

        presenter = new BookmarksPresenter(bookmarkDataSource,view);
        presenter.init();

        Mockito.verify(view).setModel(new ArrayList<Bookmark>());
        Mockito.verify(view).showNoResults();
    }

    @Test
    public void testModel(){
        final List<Bookmark> res = new ArrayList<>();
        res.add( new Bookmark("ROOM","123;000","Room 1") );
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return res;
            }
        }).when(bookmarkDataSource).allBookmarks();

        presenter = new BookmarksPresenter(bookmarkDataSource,view);
        presenter.init();

        Mockito.verify(view).setModel(res);
        Mockito.verify(view).showBookmarksList();
    }

}
