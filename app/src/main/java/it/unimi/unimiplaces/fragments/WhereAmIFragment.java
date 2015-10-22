package it.unimi.unimiplaces.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimi.unimiplaces.PlacesDataSource;
import it.unimi.unimiplaces.PlacesDb;
import it.unimi.unimiplaces.activities.BuildingDetailActivity;
import it.unimi.unimiplaces.activities.RoomDetailActivity;
import it.unimi.unimiplaces.core.model.Building;
import it.unimi.unimiplaces.core.model.Room;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return scannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.startCamera();
        scannerView.setAutoFocus(true);
    }

    @Override
    public void onScanError() {
        Log.e(LOG_TAG, "Error");
    }

    @Override
    public void onBuildingScanSuccess(String b_id) {
        Log.v(LOG_TAG, "Building: "+b_id);
        Intent buildingIntent = new Intent(this.getActivity(),BuildingDetailActivity.class);
        buildingIntent.putExtra(Building.MODEL_KEY,b_id);
        startActivity(buildingIntent);
    }

    @Override
    public void onRoomScanSuccess(String b_id, String r_id) {
        Log.v(LOG_TAG, "Room: "+b_id+" "+r_id);
        Intent roomIntent = new Intent(this.getActivity(),RoomDetailActivity.class);
        roomIntent.putExtra(Building.MODEL_KEY, b_id);
        roomIntent.putExtra(Room.MODEL_KEY,r_id);
        startActivity(roomIntent);
    }

    @Override
    public void handleResult(Result result) {
        Log.v(LOG_TAG, result.getContents());
        presenter.decodeData(result.getContents());
    }
}
