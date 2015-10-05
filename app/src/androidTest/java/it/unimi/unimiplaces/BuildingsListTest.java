package it.unimi.unimiplaces;

import android.widget.ListView;
import android.widget.TextView;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.fragments.BuildingsListFragment;

/**
 * BuildingsListFragment test class
 */

public class BuildingsListTest extends FragmentTest{

    private BuildingsListFragment buildingsListFragment;

    public void setBuildingsAdapter(final List<BaseEntity> buildings){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                buildingsListFragment.setModel(buildings);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public ListView getListView(){
        return (ListView) buildingsListFragment.getView().findViewById(android.R.id.list);
    }

    public TextView getEmptyListText(){
        return (TextView) buildingsListFragment.getView().findViewById(android.R.id.empty);
    }

    @Test
    public void testThreeBuildings(){
        buildingsListFragment = new BuildingsListFragment();
        this.prepareActivityWithFragment(buildingsListFragment);

        List<BaseEntity> buildings = new ArrayList<>();
        buildings.add(new Building("0001", "Building1", "Building address 1"));
        buildings.add(new Building("0002", "Building2", "Building address 2"));
        buildings.add(new Building("0003", "Building3", "Building address 3"));

        setBuildingsAdapter(buildings);

        ListView listView = getListView();
        assertNotNull(listView);
        assertEquals(3, listView.getAdapter().getCount());
    }

    @Test
    public void testNoBuildings(){
        buildingsListFragment = new BuildingsListFragment();
        this.prepareActivityWithFragment(buildingsListFragment);
        List<BaseEntity> buildings = new ArrayList<>();
        setBuildingsAdapter(buildings);

        ListView listView = getListView();
        TextView textView = getEmptyListText();
        assertNotNull(listView);
        assertNotNull(textView);

        assertEquals("Ops, no results :(",textView.getText());
        assertEquals(0, listView.getAdapter().getCount());

    }

}
