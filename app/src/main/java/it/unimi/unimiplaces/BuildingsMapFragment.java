package it.unimi.unimiplaces;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.unimi.unimiplaces.core.model.BaseEntity;


/**
 * 
 */
public class BuildingsMapFragment extends Fragment implements PresenterInterface {

    public BuildingsMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void setModel(List<BaseEntity> model){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buildings_map, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
