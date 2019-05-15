package com.example.asthanewsbeta2.OfflineDataManager;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.asthanewsbeta2.Modules.FeaturePostAdapter;
import com.example.asthanewsbeta2.Modules.GetPost;
import com.example.asthanewsbeta2.R;
import com.example.asthanewsbeta2.Services.CacheRequest;
import com.example.asthanewsbeta2.Services.SQLHelper;
import com.example.asthanewsbeta2.Services.WebServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ApiDataGraber {
    public void setLocalPostData() {

    }


    public static void storePostLocal(final Context context, final String POST_API) {
        final SQLHelper sqlHelper = new SQLHelper(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, POST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("mainfeed");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        GetPost item = new GetPost(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("imgUrl"),
                                o.getString("details"),
                                o.getString("date"),
                                o.getString("views"),
                                o.getString("postcode")
                        );
                        try {
                            sqlHelper.addOfflinePost(Integer.parseInt(item.getId()), item.getTitle(), item.getDetails(), item.getDate(), item.getViews(), item.getPostCode());
                        } catch (SQLiteConstraintException e) {
                            Log.d("SQL", "onResponse: DATA ENTRY Skip Cause of duplication!");
                            continue;

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        WebServiceProvider.getInstace(context).addToRequestQueue(stringRequest);
    }
}
