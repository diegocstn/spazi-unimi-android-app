package it.unimi.unimiplaces;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import it.unimi.unimiplaces.core.api.APIDelegateInterface;
import it.unimi.unimiplaces.core.model.BaseEntity;

public class BuildingsFragment extends Fragment implements APIDelegateInterface {

    private List<BaseEntity> model;
    private BuildingsListFragment buildingsListFragment;
    private APIManager apiManager;

    final static String MODEL_KEY = "model";

    public BuildingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.apiManager = new APIManager(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buildings, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buildingsListFragment = (BuildingsListFragment) getFragmentManager().findFragmentById(R.id.buildings_list_fragment);

        if( savedInstanceState != null ) {
            this.model = (List) savedInstanceState.getSerializable(MODEL_KEY);
            if (this.model == null) {
                apiManager.buildings(this);
            }else{
                buildingsListFragment.setModel(this.model);
            }
        }else{
            apiManager.buildings(this);
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MODEL_KEY,(Serializable)this.model);
        super.onSaveInstanceState(outState);
    }


    /* APIManagerDelegate methods */
    @Override
    public void apiRequestStart() {

    }

    @Override
    public void apiRequestEnd(List<BaseEntity> results) {
        this.model = results;
        buildingsListFragment.setModel(this.model);
    }

}
