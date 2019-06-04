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
import com.example.asthanewsbeta2.Services.GoogleTranslate;
import com.example.asthanewsbeta2.Services.WebServiceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiDataGrabber {
    private static String TAG = "apiGrabber";


    public static void storePostLocal(final Context context, final String POST_API, final String post_lng) {
        final SQLHelper sqlHelper = new SQLHelper(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, POST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("mainfeed");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        final GetPost item;
                        item = new GetPost(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("imgUrl"),
                                o.getString("details"),
                                o.getString("date"),
                                o.getString("views"),
                                o.getString("postcode")
                        );


                        if (post_lng.equals("en")) {
                            if (sqlHelper.findPost(item.getId(), post_lng)) {
                                Log.d(TAG, "onResponse: Insert Skip Cause of duplication! " + item.getId());
                                continue;
                            } else {
                                Log.d(TAG, "onResponse: Data Inserted! " + item.getId());
                                String id = item.getId();
                                String title = item.getTitle();
                                String details = item.getDetails();
                                String imgUrl = item.getImgUrl();
                                String date = item.getDate();
                                String views = item.getViews();
                                String postCode = item.getPostCode();
                                sqlHelper.addOfflinePost(Integer.parseInt(id),
                                        title,
                                        imgUrl,
                                        details,
                                        date,
                                        views,
                                        postCode,
                                        post_lng);
                            }
                        } else {
                            if (sqlHelper.findPost(item.getId(), post_lng)) {
                                Log.d(TAG, "onResponse: Insert Skip Cause of duplication! " + item.getId());
                                continue;
                            } else {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //final String tmp_title = GoogleTranslate.translate(post_lng, item.getTitle());
                                        final String tmp_title = MngData.mngString(context, item.getTitle());
                                        Log.d(TAG, "run: MY_TITLE: " + tmp_title);
                                        //final String tmp_details = GoogleTranslate.translate(post_lng, item.getDetails());
                                        final String tmp_details = MngData.mngString(context, item.getDetails());
                                        Log.d(TAG, "run: MY_DETAILS: " + tmp_details);
                                        sqlHelper.addOfflinePost(Integer.parseInt(
                                                item.getId()),
                                                tmp_title,
                                                item.getImgUrl(),
                                                tmp_details,
                                                item.getDate(),
                                                item.getViews(),
                                                item.getPostCode(),
                                                post_lng);

                                    }
                                }).start();

                            }
                        }
                    }
                } catch (Exception e) {
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


    public static void storeMenuLocal(final Context context, final String MENU_API, final String MENU_LNG) {

        final SQLHelper sqlHelper = new SQLHelper(context);
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, MENU_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("menuitems");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        final GetMenu item = new GetMenu(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("cat")
                        );


                        if (sqlHelper.findMenu(item.getId(), MENU_LNG)) {
                            Log.d(TAG, "onResponse: DATA ENTRY Skip Cause of duplication!");
                            continue;
                        } else {
                            try {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        final String tmp_title;
                                        try {
                                            tmp_title = GoogleTranslate.translate(MENU_LNG, item.getTitle());
                                            sqlHelper.addOfflineMenu(Integer.parseInt(item.getId()), tmp_title, item.getCat(), MENU_LNG);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }).start();

                            } catch (SQLException e) {
                                Log.d(TAG, "onResponse: Something goes Wrong! " + e);
                            }
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
        WebServiceProvider.getInstace(context).addToRequestQueue(stringRequest1);
    }


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
                        default:
                            mg.setCheckPostCount("1");
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
            default:
                return "No Table Found!";


        }

    }


    //Get online file data
    public static void getFeedFromFile(final Context context, final String FEED_API, final String post_lng) {
        final List<String> data = new ArrayList<>();
        final MyGlobal mg = new MyGlobal();
        StringRequest checkDataChange = new StringRequest(Request.Method.GET, FEED_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String title_url = "ram";
                    String details_url = "ram";
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("feed");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        final GetFeed item;
                        item = new GetFeed(
                                o.getString("id"),
                                o.getString("title_en"),
                                o.getString("title_gu"),
                                o.getString("title_hi"),
                                o.getString("details_en"),
                                o.getString("details_gu"),
                                o.getString("details_hi"),
                                o.getString("imgUrl"),
                                o.getString("date_time"),
                                o.getString("views"),
                                o.getString("postcode")

                        );

                        title_url = o.getString("title_gu");
                        details_url = o.getString("details_gu");
                        data.add(0, title_url);
                        data.add(1, details_url);
                    }

                    MngData.setData(context, "tmpString", "title", data.get(0));
                    MngData.setData(context, "tmpString", "details", data.get(1));

                    Log.d("MYFILE", "onResponse: MYGUJ TITLE_URL: " + title_url);
                    Log.d("MYFILE", "onResponse: MYGUJ DETAILS_URL: " + details_url);
                    Log.d("MYFILE", "onResponse: MYGUJ TITLE: " + data.get(0));
                    Log.d("MYFILE", "onResponse: MYGUJ DETAILS: " + data.get(1));
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


}
