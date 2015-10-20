package it.unimi.unimiplaces.core.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * APIRequest
 */

public class APIRequest {

    public enum APIRequestIdentifier{
        BUILDINGS,
        BUILDING_BY_BID,
        AVAILABLE_SERVICES,
        BUILDINGS_BY_SERVICES,
        ROOM_BY_ID,
        ROOM_TIMETABLE,
        FLOOR_MAP
    }

    private boolean useCache;
    private URL url;
    private String response;
    public APIRequestIdentifier requestType;

    public APIRequest(String url,APIRequestIdentifier identifier){

        this.response       = null;
        this.requestType    = identifier;
        this.useCache       = false;
        try {
            this.url = new URL(url);
        }catch (MalformedURLException e){
            System.out.println(e);
        }
    }

    public void exec(){
        /*
         * use StringBuilder and an auxiliary String to
         * optimize performance of BufferedReader because String are immutable
         * so a clone of the String is make at every iteration
         * */
        String aux              = null;
        StringBuilder builder   = new StringBuilder();

        /* delete old response */
        this.response = null;

        try {
            HttpURLConnection  urlConnection = (HttpURLConnection) this.url.openConnection();
            urlConnection.setUseCaches(this.useCache);
            urlConnection.setDefaultUseCaches(this.useCache);

            if( urlConnection.getResponseCode() > HttpURLConnection.HTTP_ACCEPTED){
                this.response = null;
                return;
            }

            BufferedReader in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));
            while( (aux = in.readLine())!=null ){
                builder.append(aux);
            }

            this.response = builder.toString();
            urlConnection.disconnect();
            in.close();

        }catch (IOException e){
            this.response = null;

        }
    }

    public String getResponse(){
        if( this.response==null ) {
            this.exec();
        }
        return this.response;
    }

    public boolean is404(){
        return this.response==null;
    }

    public void setUseCache(boolean use){
        this.useCache = use;
    }
}
