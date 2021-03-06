package it.unimi.unimiplaces.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import it.unimi.unimiplaces.R;

/*
* Activity for unit test fragment
* */

public class TestActivity extends AppCompatActivity {

    Fragment fragment;
    View view;

    public void setFragmentForTest(Fragment f){
        fragment = f;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.test_activity_container,f);
        ft.commit();
    }

    public void setViewForTest(View v){
        view = v;
        RelativeLayout viewContainer = (RelativeLayout) findViewById(R.id.test_activity_view_container);
        viewContainer.addView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
