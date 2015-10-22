package it.unimi.unimiplaces.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimi.unimiplaces.PlacesDataSource;
import it.unimi.unimiplaces.PlacesDb;
import it.unimi.unimiplaces.presenters.WhereAmIPresenter;
import it.unimi.unimiplaces.views.WhereAmIViewInterface;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WhereAmIFragment extends Fragment implements ZBarScannerView.ResultHandler,WhereAmIViewInterface {

    private WhereAmIPresenter presenter;
    private ZBarScannerView scannerView;
    private final static String LOG_TAG = "WHEREAMI";

    public WhereAmIFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter   = new WhereAmIPresenter(new PlacesDataSource(new PlacesDb(getActivity())),this);
        scannerView = new ZBarScannerView(getActivity());
        scannerView.setResultHandler(this);
        scannerView.startCamera();
        scannerView.setAutoFocus(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return scannerView;
    }

    @Override
    public void onScanError() {
        Log.e(LOG_TAG, "Error");
    }

    @Override
    public void onBuildingScanSuccess(String b_id) {
        Log.v(LOG_TAG, "Building: "+b_id);
    }

    @Override
    public void onRoomScanSuccess(String b_id, String r_id) {
        Log.v(LOG_TAG, "Room: "+b_id+" "+r_id);
    }

    @Override
    public void handleResult(Result result) {
        Log.v(LOG_TAG, result.getContents());
        presenter.decodeData(result.getContents());
    }
}
