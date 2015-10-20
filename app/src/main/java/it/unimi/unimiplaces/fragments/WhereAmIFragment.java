package it.unimi.unimiplaces.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimi.unimiplaces.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhereAmIFragment extends Fragment {


    public WhereAmIFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_where_am_i, container, false);
    }


}
