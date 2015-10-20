package it.unimi.unimiplaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import it.unimi.unimiplaces.core.api.APIDelegateInterface;
import it.unimi.unimiplaces.core.api.APIDelegateInterfaceExtended;
import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.api.APIRequest;
import it.unimi.unimiplaces.core.model.AvailableService;
import it.unimi.unimiplaces.core.model.BaseEntity;

/**
 * It manages the interaction with the API, providing a meaningful interface.
 * It's the only access-point to the server API
 */
public class APIManager {

    private Context context;
    private APIFactory apiFactory;
    private static final String APIBaseURL = "http://spazi.srv.di.unimi.it/api/v1.0/";
    private ProgressDialog progressDialog;
    private APIDelegateInterface delegate;
    private APIAsyncTask asyncTask;

    final static String LOG_TAG     = "APIMANAGER";


    public APIManager(Context context, APIAsyncTask asyncTask){
        this.apiFactory     = new APIFactory();
        this.context        = context;
        this.progressDialog = new ProgressDialog( this.context , ProgressDialog.STYLE_SPINNER );
        this.progressDialog.setMessage(this.context.getString(R.string.progress_loading));
        this.asyncTask      = asyncTask;

        this.installCache();
    }

    private void installCache(){
        long cacheSize      = 50*10*1024; /* 50 MB */
        File httpCacheDir   = new File(this.context.getCacheDir(),"http");
        try {
            HttpResponseCache.install(httpCacheDir,cacheSize);
        }catch (IOException e){
            Log.e(LOG_TAG,"Cache install: "+e.getMessage());
        }

    }

    private void logCacheStats(){
        HttpResponseCache httpResponseCache = HttpResponseCache.getInstalled();
        Log.i(LOG_TAG,"Cache req count " + httpResponseCache.getRequestCount());
        Log.i(LOG_TAG,"Cache net count "+httpResponseCache.getNetworkCount());
        Log.i(LOG_TAG,"Cache hit count "+httpResponseCache.getHitCount());
        Log.i(LOG_TAG,"Cache size "+httpResponseCache.size());
    }

    private void executeAPIRequest(String endpoint,APIRequest.APIRequestIdentifier identifier,boolean showProgress){
        // show progress dialog and alert the delegate object
        // that the async task is processing in background
        if( showProgress ) {
            this.progressDialog.show();
        }
        try {
            this.delegate.apiRequestStart();
            APIAsyncTask apiAsyncTask = this.asyncTask.buildAPISyncTask(this);
            apiAsyncTask.execute(new APIRequest(APIBaseURL + endpoint,identifier));

        }catch(Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            this.progressDialog.hide();
        }

    }

    private void executeCachedAPIRequest(String fullEndpoint,APIRequest.APIRequestIdentifier identifier){
        try {
            this.delegate.apiRequestStart();
            APIAsyncTask apiAsyncTask   = this.asyncTask.buildAPISyncTask(this);
            APIRequest request          = new APIRequest(fullEndpoint,identifier);
            request.setUseCache(true);
            apiAsyncTask.execute(request);
        }catch (Exception e){
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void requestExecuted(String result,APIRequest.APIRequestIdentifier requestIdentifier){

        APIDelegateInterfaceExtended extendedDelegate;

        /* 404 */
        if( result == null ){
            this.delegate.apiRequestError();
            return;
        }

        List<BaseEntity> entities;
        switch ( requestIdentifier ){
            case BUILDINGS_BY_SERVICES:
                entities = this.apiFactory.makeBuildingsFromJSON(result);
                if( entities == null ){
                    this.delegate.apiRequestError();
                }
                this.delegate.apiRequestEnd(entities);
                this.progressDialog.hide();
                break;
            case BUILDINGS:
                entities = this.apiFactory.makeBuildingsFromJSON(result);
                if( entities == null ){
                    this.delegate.apiRequestError();
                }
                this.delegate.apiRequestEnd(entities);
                this.progressDialog.hide();
                break;
            case AVAILABLE_SERVICES:
                extendedDelegate = (APIDelegateInterfaceExtended) this.delegate;
                entities = this.apiFactory.makeAvailableServicesFromJSON(result);
                if( entities == null ){
                 extendedDelegate.apiRequestError();
                }
                extendedDelegate.apiServiceAvailableRequestEnd(entities);
                break;

            case BUILDING_BY_BID:
                entities = new ArrayList<>();
                BaseEntity building = this.apiFactory.makeBuildingFromJSON(result);
                entities.add( building );
                if( building == null ){
                    this.delegate.apiRequestError();
                }
                this.delegate.apiRequestEnd(entities);
                this.progressDialog.hide();
                break;

            case ROOM_BY_ID:
                entities    = new ArrayList<>();
                BaseEntity room = this.apiFactory.makeRoomFromJSON(result);
                entities.add( room );
                if( room == null ){
                    this.delegate.apiRequestError();
                }
                this.delegate.apiRequestEnd(entities);
                this.progressDialog.hide();
                break;

            case FLOOR_MAP:
                extendedDelegate = (APIDelegateInterfaceExtended) this.delegate;
                extendedDelegate.apiFloorMapAtURLEnd(result);
                /* flush HTTP request response to filesystem */
                HttpResponseCache.getInstalled().flush();
                this.logCacheStats();
                break;

            case ROOM_TIMETABLE:
                extendedDelegate    = (APIDelegateInterfaceExtended) this.delegate;
                entities            = this.apiFactory.makeRoomEventsFromJSON(result);
                if( entities == null ){
                    extendedDelegate.apiRequestError();
                }
                extendedDelegate.apiRoomTimeTableEnd(entities);
                break;
        }
    }

    public void buildings(APIDelegateInterface delegate){
        Log.v(LOG_TAG,"Buildings API request");
        this.delegate           = delegate;
        this.executeAPIRequest("buildings/", APIRequest.APIRequestIdentifier.BUILDINGS,true);
    }

    public void availableServices(APIDelegateInterfaceExtended delegate,String lang){
        Log.v(LOG_TAG,"Available service API request");
        this.delegate           = delegate;
        this.executeAPIRequest("available-services/"+lang+"/", APIRequest.APIRequestIdentifier.AVAILABLE_SERVICES,false);
    }

    public void buildingsByAvailableService(APIDelegateInterface delegate, AvailableService service){
        Log.v(LOG_TAG,"Building by available service API request");
        this.delegate           = delegate;
        try {
            String serviceEncoded = URLEncoder.encode(service.key, "UTF-8");
            this.executeAPIRequest("buildings/?service="+serviceEncoded, APIRequest.APIRequestIdentifier.BUILDINGS_BY_SERVICES, true);
        }catch (UnsupportedEncodingException e){
            Log.e(LOG_TAG,"Error encoding service key");
        }
    }

    public void buildingByBID(APIDelegateInterface delegate,String b_id){
        Log.v(LOG_TAG,"Building:"+b_id+" API request");
        this.delegate = delegate;
        this.executeAPIRequest("buildings/"+b_id+"/", APIRequest.APIRequestIdentifier.BUILDING_BY_BID,true);
    }

    public void roomByRIDAndBID(APIDelegateInterface delegate, String r_id, String b_id){
        Log.v(LOG_TAG,"Room:"+r_id+"in building"+b_id+" API request");
        this.delegate = delegate;
        this.executeAPIRequest("rooms/"+b_id+"/"+r_id+"/", APIRequest.APIRequestIdentifier.ROOM_BY_ID,true);
    }

    public void floorMapAtURL(APIDelegateInterfaceExtended delegate,String URL){
        Log.v(LOG_TAG,"Floor map at:"+URL);
        this.delegate = delegate;
        this.executeCachedAPIRequest(URL, APIRequest.APIRequestIdentifier.FLOOR_MAP);
    }

    public void timetableForRoom(APIDelegateInterfaceExtended delegate,String r_id,String b_id){
        Log.v(LOG_TAG,"Room timetable for room: "+r_id+"@"+b_id);
        this.delegate = delegate;
        this.executeAPIRequest("rooms/timetable/"+b_id+"/"+r_id, APIRequest.APIRequestIdentifier.ROOM_TIMETABLE,false);
    }


    public static class APIManagerFactory{
        public static APIManager createAPIManager(Context context){
            return new APIManager(context,new APIAsyncTask());
        }
    }

    public static class APIAsyncTask extends AsyncTask<it.unimi.unimiplaces.core.api.APIRequest,Void,String>{

        public APIRequest request;
        public APIManager apiManager;

        public APIAsyncTask(){}

        public APIAsyncTask buildAPISyncTask(APIManager apiManager){
            return new APIAsyncTask(apiManager);
        }

        public APIAsyncTask(APIManager apiManager){
            this.apiManager = apiManager;
        }

        @Override
        protected String doInBackground(APIRequest... requests){
            request = requests[0];
            request.exec();
            return request.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            this.apiManager.requestExecuted(s, request.requestType);
        }

    }

}
