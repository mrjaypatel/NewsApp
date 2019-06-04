package com.example.asthanewsbeta2.DataManager;

import android.content.Context;
import android.database.SQLException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asthanewsbeta2.Modules.GetMenu;
import com.example.asthanewsbeta2.Modules.GetPost;
import com.example.asthanewsbeta2.Modules.MngData;
import com.example.asthanewsbeta2.Modules.MyGlobal;
import com.example.asthanewsbeta2.OfflineDataManager.SQLHelper;
import com.example.asthanewsbeta2.Services.WebServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApiDataGrabber {
    private static String TAG = "apiGrabber";




    public static String checkDataChange(final Context context, final String Table) {
        String CHECK_API = "http://durgaplacements.com/Api/checkUpdate.php?key=madHash456@@&table=" + Table;
        final MyGlobal mg = (MyGlobal) context;
        StringRequest checkDataChange = new StringRequest(Request.Method.GET, CHECK_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("checkcount");
                    String data;
                    JSONObject o = array.getJSONObject(0);
                    data = o.getString("check");
                    switch (Table) {
                        case "mainfeed":
                            mg.setCheckPostCount(data);
                            Log.d("checkMe", "Post Count: " + data);
                            break;
                        case "menuitems":
                            mg.setCheckMenuCount(data);
                            Log.d("checkMe", "Menu Count: " + data);
                            break;
                        case "feed":
                            mg.setCheckFeedCount(data);
                            Log.d("checkMe", "Feed Post Count: " + data);
                            break;
                        default:
                            mg.setCheckPostCount("1");
                            mg.setCheckFeedCount("1");
                            mg.setCheckMenuCount("1");
                            Log.d("checkMe", "Post Count: No Table Found");
                            Log.d("checkMe", "Menu Count: No Table Found");
                            break;

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("checkMe", "onResponse: Post/Menu Count Exception!: " + e);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        WebServiceProvider.getInstace(context).addToRequestQueue(checkDataChange);


        switch (Table) {
            case "mainfeed":
                return mg.getCheckPostCount();
            case "menuitems":
                return mg.getCheckMenuCount();
            case "feed":
                return mg.getCheckFeedCount();
            default:
                return "No Table Found!";
        }

    }

    //Get online file data
    public static void storeFeedOfline(final Context context, final String FEED_API, final String post_lng) {
        StringRequest checkDataChange = new StringRequest(Request.Method.GET, FEED_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("feed");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        final GetFeed item;
                        item = new GetFeed(
                                o.getString("id"),
                                o.getString("title_gu"),
                                o.getString("details_gu"),
                                o.getString("date_time"),
                                o.getString("imgUrl"),
                                o.getString("views"),
                                o.getString("postcode")
                        );

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FileControl fc = new FileControl();
                                String title = fc.getFileFromUrl(item.getTitle_gu());
                                String details = fc.getFileFromUrl(item.getDetails_gu());

                                SQLHelper sqlHelper = new SQLHelper(context);
                                if (!sqlHelper.findPost(item.getId(), "gu")) {
                                    Log.d(TAG, "onResponse: Data Inserted! " + item.getId());
                                    String id = item.getId();
                                    String imgUrl = item.getImgUrl();
                                    String date = item.getDate_time();
                                    String views = item.getViews();
                                    String postCode = item.getPostcode();

                                    sqlHelper.addOfflinePost(Integer.parseInt(id),
                                            title,
                                            imgUrl,
                                            details,
                                            date,
                                            views,
                                            postCode,
                                            "gu");
                                } else {
                                    Log.d(TAG, "run:Apigrabber Post Entry Skiped");
                                }
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("MYFILE", "onResponse: Post/Menu Count Exception!: " + e);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYFILE", "onResponse: Post/Menu Count Exception!: " + error);
            }
        });

        WebServiceProvider.getInstace(context).addToRequestQueue(checkDataChange);

    }


    //Get online file data
    public static void storeMenuOffline(final Context context, final String MENU_API) {
        StringRequest menuGetRequest = new StringRequest(Request.Method.GET, MENU_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("menuitems");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        final GetMenu item;
                        item = new GetMenu(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("cat")
                        );

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                FileControl fc = new FileControl();
                                String title = fc.getFileFromUrl(item.getTitle());
                                Log.d(TAG, "run: Converted Menu Title: " + title);
                                SQLHelper sqlHelper = new SQLHelper(context);
                                if (!sqlHelper.findMenu(item.getId())) {
                                    Log.d(TAG, "onResponse: Data Inserted! " + item.getId());
                                    String id = item.getId();

                                    sqlHelper.addOfflineMenu(Integer.parseInt(id),
                                            title,
                                            item.getCat(),
                                            "gu");
                                } else {
                                    Log.d(TAG, "run:Apigrabber Menu Entry Skiped");
                                }
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("MYFILE", "onResponse: Post/Menu Count Exception!: " + e);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("MYFILE", "onResponse: Post/Menu Count Exception!: " + error);
            }
        });

        WebServiceProvider.getInstace(context).addToRequestQueue(menuGetRequest);

    }


}
