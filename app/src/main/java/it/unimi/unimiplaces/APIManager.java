package it.unimi.unimiplaces;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import it.unimi.unimiplaces.core.api.APIDelegateInterface;
import it.unimi.unimiplaces.core.api.APIDelegateInterfaceExtended;
import it.unimi.unimiplaces.core.api.APIFactory;
import it.unimi.unimiplaces.core.api.APIRequest;
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

    public APIManager(Context context){
        this.apiFactory     = new APIFactory();
        this.context        = context;
        this.progressDialog = new ProgressDialog( this.context , ProgressDialog.STYLE_SPINNER );
        this.progressDialog.setMessage(this.context.getResources().getString(R.string.progress_loading));
    }

    private void executeAPIRequest(String endpoint,APIRequest.APIRequestIdentifier identifier,boolean showProgress){
        // show progress dialog and alert the delegate object
        // that the async task is processing in background
        if( showProgress ) {
            this.progressDialog.show();
        }
        this.delegate.apiRequestStart();

        APIAsyncTask apiAsyncTask = new APIAsyncTask();
        apiAsyncTask.execute(new APIRequest(APIBaseURL + endpoint,identifier));
    }

    private void requestExecuted(String result,APIRequest.APIRequestIdentifier requestIdentifier){
        List<BaseEntity> entities = null;

        switch ( requestIdentifier ){
            case BUILDINGS:
                entities = this.apiFactory.makeBuildingsFromJSON(result);
                this.delegate.apiRequestEnd(entities);
                this.progressDialog.hide();
                break;
            case AVAILABLE_SERVICES:
                APIDelegateInterfaceExtended extendedDelegate = (APIDelegateInterfaceExtended) this.delegate;
                extendedDelegate.apiServiceAvailableRequestEnd(this.apiFactory.makeAvailableServicesFromJSON(result));
                break;
        }
    }

    public void buildings(APIDelegateInterface delegate){
        this.delegate           = delegate;
        this.executeAPIRequest("buildings/", APIRequest.APIRequestIdentifier.BUILDINGS,true);
    }

    public void availableServices(APIDelegateInterfaceExtended delegate,String lang){
        this.delegate           = delegate;
        this.executeAPIRequest("available-services/"+lang+"/", APIRequest.APIRequestIdentifier.AVAILABLE_SERVICES,false);
    }


    private class APIAsyncTask extends AsyncTask<it.unimi.unimiplaces.core.api.APIRequest,Void,String>{

        APIRequest request;

        @Override
        protected String doInBackground(APIRequest... requests){
            request = requests[0];
            request.exec();
            return request.getResponse();
        }

        @Override
        protected void onPostExecute(String s) {
            requestExecuted(s,request.requestType);
        }

    }

}
