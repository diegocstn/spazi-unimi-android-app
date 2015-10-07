package it.unimi.unimiplaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

    final static String LOG_TAG = "APIMANAGER";

    public APIManager(Context context, APIAsyncTask asyncTask){
        this.apiFactory     = new APIFactory();
        this.context        = context;
        this.progressDialog = new ProgressDialog( this.context , ProgressDialog.STYLE_SPINNER );
        this.progressDialog.setMessage(this.context.getString(R.string.progress_loading));
        this.asyncTask      = asyncTask;
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

    private void requestExecuted(String result,APIRequest.APIRequestIdentifier requestIdentifier){

        /* 404 */
        if( result == null ){
            this.delegate.apiRequestError();
            return;
        }

        List<BaseEntity> entities;
        switch ( requestIdentifier ){
            case BUILDINGS_BY_SERVICES:
            case BUILDINGS:
                entities = this.apiFactory.makeBuildingsFromJSON(result);
                if( entities == null ){
                    this.delegate.apiRequestError();
                }
                this.delegate.apiRequestEnd(entities);
                this.progressDialog.hide();
                break;
            case AVAILABLE_SERVICES:
                APIDelegateInterfaceExtended extendedDelegate = (APIDelegateInterfaceExtended) this.delegate;
                entities = this.apiFactory.makeAvailableServicesFromJSON(result);
                if( entities == null ){
                 extendedDelegate.apiRequestError();
                }
                extendedDelegate.apiServiceAvailableRequestEnd(entities);
                break;
        }
    }

    public void buildings(APIDelegateInterface delegate){
        Log.v(LOG_TAG,"Building API request");
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
        this.executeAPIRequest("buildings/?service="+service.key, APIRequest.APIRequestIdentifier.BUILDINGS_BY_SERVICES,true);
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
