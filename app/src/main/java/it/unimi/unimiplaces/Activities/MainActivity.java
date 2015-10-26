package it.unimi.unimiplaces.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.fragments.BookmarksFragment;
import it.unimi.unimiplaces.fragments.BuildingsFragment;
import it.unimi.unimiplaces.fragments.PlacesFragment;
import it.unimi.unimiplaces.fragments.WhereAmIFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CustomActionBarDrawerToggle drawerToggle;
    private MenuItem currentAppSection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // replace actionbar with toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // setup navigation drawer
        drawerLayout    = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView  = (NavigationView) findViewById(R.id.drawer_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle    = new CustomActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.setDrawerListener(drawerToggle);
        currentAppSection = getDefaultCheckedMenuItem();
        showFragmentForMenuItem(currentAppSection, true);

    }

    /**
     * getDefaultCheckedMenuItem : get the default checked menu item, marked as "checked"
     * in xml res file menu_main_navigation.xml
     * @return the default checked menu item
     */
    private MenuItem getDefaultCheckedMenuItem(){
        Menu menu       = navigationView.getMenu();
        MenuItem item   = null;
        for(int i=0;i<menu.size();i++){
            if( menu.getItem(i).isChecked() ){
                item = menu.getItem(i);
            }
        }
        return item;
    }

    /**
     * showFragment : replace content fragment with the associated fragment
     * @param menuItem menu item clicked
     * @param initialization if true skip the current fragment check
     */
    private void showFragmentForMenuItem(MenuItem menuItem,boolean initialization){
        Fragment newFragment = null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        /* do nothing if it's the current fragment */
        if( currentAppSection.getItemId() == menuItem.getItemId() && !initialization ){
            drawerLayout.closeDrawers();
            return;
        }

        this.hideKeyboard();

        switch (menuItem.getItemId()){
            case R.id.nav_buildings:
                newFragment = new BuildingsFragment();
                break;
            case R.id.nav_bookmarks:
                newFragment = new BookmarksFragment();
                break;
            case R.id.nav_places:
                newFragment = new PlacesFragment();
                break;
            case R.id.nav_whereami:
                newFragment = new WhereAmIFragment();
                break;
        }

        currentAppSection = menuItem;
        transaction.replace(R.id.frame_content,newFragment);
        transaction.commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawerLayout.closeDrawers();
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        showFragmentForMenuItem(menuItem,false);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Custom ActionBarDrawerToggle just for hiding keyboard when the drawer
     * will be opened
     */
    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle{
        public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            hideKeyboard();
        }
    }



}
