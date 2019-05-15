package com.example.asthanewsbeta2.Services;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class WebServiceProvider extends Application {
    public WebServiceProvider(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }



    private RequestQueue requestQueue;
    public static WebServiceProvider instace;


    public WebServiceProvider(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        requestQueue.start();
    }



    public static WebServiceProvider getInstace(Context context) {
        if (instace == null) {
            instace = new WebServiceProvider(context);
        }
        return instace;
    }


    public <T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
