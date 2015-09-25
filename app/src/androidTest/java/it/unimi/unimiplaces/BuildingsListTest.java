package it.unimi.unimiplaces;

import android.widget.ListView;

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

    private ListView listView;
    private BuildingsListFragment buildingsListFragment;

    public void setBuildingsAdapter(List<BaseEntity> buildings){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<BaseEntity> buildings = new ArrayList<>();
                buildings.add(new Building("0001", "Building1", "Building address 1"));
                buildings.add(new Building("0002", "Building2", "Building address 2"));
                buildings.add(new Building("0003", "Building3", "Building address 3"));
                buildingsListFragment.setModel(activity, buildings);
            }
        });
        getInstrumentation().waitForIdleSync();
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

        getInstrumentation().waitForIdleSync();
        listView = (ListView) buildingsListFragment.getView().findViewById(android.R.id.list);
        assertEquals(3, listView.getAdapter().getCount());
    }
    

}
