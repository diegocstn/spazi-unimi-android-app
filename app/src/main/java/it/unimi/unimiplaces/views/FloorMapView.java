package it.unimi.unimiplaces.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import it.unimi.unimiplaces.R;

/**
 * FlorMapView
 */
public class FloorMapView extends RelativeLayout {

    private Context context;
    private TextView loadingTextView;
    private WebView webView;
    private final String LOG_TAG        = "FLOORMAPVIEW";
    private final String HTMLFileName   = "html/floormap.html";
    private final String HTMLBaseURL    = "file:///android_asset/html/";

    public FloorMapView(Context context){
        super(context);
        this.context = context;
        this.init();
    }

    public FloorMapView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(){
        inflate(getContext(), R.layout.view_floor_map,this);

        this.loadingTextView    = (TextView) findViewById(R.id.webview_loading);
        this.webView            = (WebView) findViewById(R.id.webview);

        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().getAllowFileAccessFromFileURLs();
        this.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        this.webView.getSettings().getAllowContentAccess();
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(false);
        WebView.setWebContentsDebuggingEnabled(true);

    }

    private String injectSVGInHTMLSource(String src,String svg){
        return src.replace("@@##SVG_HERE##@@",svg);
    }

    private String loadHTMLSourceAsString(){
        StringBuilder builder;
        try {
            BufferedReader in   = new BufferedReader(new InputStreamReader(context.getAssets().open(HTMLFileName)));
            builder             = new StringBuilder();
            String aux;
            while( (aux = in.readLine())!=null ){
                builder.append(aux);
            }
            return builder.toString();

        }catch (Exception e){
            Log.e(LOG_TAG,e.getMessage());
            return null;
        }
    }

    private void pageLoadingFinished(){
        this.loadingTextView.setVisibility(GONE);
        this.webView.setVisibility(VISIBLE);
    }

    public void highlightRoomInMap(String svgData,String roomId){
        /* extract color from res, remove alpha component and convert in hex rgb form */
        int colorInt = ContextCompat.getColor(getContext(), R.color.colorAccent);
        String color = "#"+Integer.toHexString(colorInt & 0x00ffffff);

        /* load HTML content */
        this.webView.loadDataWithBaseURL(HTMLBaseURL,injectSVGInHTMLSource(loadHTMLSourceAsString(),svgData),"text/html","utf-8",null);

        /* JS script */
        final String script = String.format("svgFloor.selectRoom(\"%s\",\"%s\")",roomId,color);
        this.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webView.evaluateJavascript(script, null);
                pageLoadingFinished();
            }
        });

    }

}
