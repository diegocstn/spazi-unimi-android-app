package it.unimi.unimiplaces;

import android.app.Fragment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import it.unimi.unimiplaces.activities.TestActivity;


@RunWith(AndroidJUnit4.class)
class FragmentTest extends ActivityInstrumentationTestCase2<TestActivity> {

    TestActivity activity;

    public FragmentTest(){
        super(TestActivity.class);
    }

    public void prepareActivityWithFragment(Fragment f){
        this.activity.setFragmentForTest(f);
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}