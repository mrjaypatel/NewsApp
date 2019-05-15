package com.example.asthanewsbeta2.Services;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GsonRequest<T> extends JsonRequest<T> {

    private Gson gson = new Gson();
    private Map<String, String> headers;
    private Class<T> clazz;
    private Response.Listener<T> listener;
    private Response.Listener<String> listeners;


    private GsonRequest(int method, String url, String requestBody, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.headers = headers;
        this.clazz = clazz;
        this.listener = listener;

    }

    public static <T> GsonRequest<T> getGsonRequest(int method, String url, String requestBody, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {

        String API_URL = url;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        GsonRequest<T> gsonRequest = new GsonRequest<>(method, API_URL, requestBody, clazz, header, listener, errorListener);
        return gsonRequest;

    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
        if (cacheEntry == null) {
            cacheEntry = new Cache.Entry();
        }

        final long cacheHitButRefreshed = 3 * 60 * 1000; // 3min Cache hit and refresh in background
        final long cacheExpire = 24 * 60 * 60 * 1000; //Expires in 24 hours
        long now = System.currentTimeMillis();
        final long softExpire = now + cacheHitButRefreshed; //Sum of time and refresh rate
        final long ttl = now + cacheExpire; //Sum of time and cache Expire


        cacheEntry.data = response.data;
        cacheEntry.softTtl = softExpire;
        cacheEntry.ttl = ttl;

        String headerValue;
        headerValue = response.headers.get("Date");
        if (headerValue != null) {
            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        headerValue = response.headers.get("Last-Modified");
        if (headerValue != null) {
            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        cacheEntry.responseHeaders = response.headers;


        try {
            String json = new String(cacheEntry.data, HttpHeaderParser.parseCharset(response.headers));
            return (Response<T>) Response.success(new String(json), HttpHeaderParser.parseCacheHeaders(response));
            //return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }

    }


    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return super.parseNetworkError(volleyError);
    }

    @Override
    protected void deliverResponse(T response) {

        listener.onResponse(response);
    }


}
