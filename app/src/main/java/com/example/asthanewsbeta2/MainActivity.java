package com.example.asthanewsbeta2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asthanewsbeta2.Modules.FeaturePostAdapter;
import com.example.asthanewsbeta2.Modules.GetMenu;
import com.example.asthanewsbeta2.Modules.GetPost;
import com.example.asthanewsbeta2.Modules.MainFeedAdapter;
import com.example.asthanewsbeta2.Modules.MenuItemAdapter;
import com.example.asthanewsbeta2.Modules.MngData;
import com.example.asthanewsbeta2.OfflineDataManager.ApiDataGraber;
import com.example.asthanewsbeta2.Services.CacheRequest;
import com.example.asthanewsbeta2.Services.SQLHelper;
import com.example.asthanewsbeta2.Services.UpdateStrings;
import com.example.asthanewsbeta2.Services.WebServiceProvider;
import com.facebook.shimmer.ShimmerFrameLayout;


import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView menuItems;
    private List<GetMenu> menuItemData;
    private List<GetPost> postData;
    private RecyclerView mainFeed;

    private ShimmerFrameLayout shimmerFrameLayout;

    public static String API_URL = "http://durgaplacements.com/Api/mainFeedApi.php?key=madHash456@@&postCode=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SQLHelper sh = new SQLHelper(getApplicationContext());
        sh.addOfflinePost(2,"મારુ નામ જય છે", "This is jay patel Data", "11-Apr-2019","2500","2018");
        for(String data: sh.getOfflinePost(0)){
            Log.d("HOME", "onCreate: postData index_id:"+data);
        }
        for(String data: sh.getOfflinePost(1)){
            Log.d("HOME", "onCreate: postData id:"+data);
        }
        for(String data: sh.getOfflinePost(2)){
            Log.d("HOME", "onCreate: postData title:"+data);
        }


        //ApiDataGraber.storePostLocal(getApplicationContext(), API_URL);

        shimmerFrameLayout = findViewById(R.id.placeholderPost);


        mainFeed = findViewById(R.id.mainFeed);
        mainFeed.setHasFixedSize(true);
        mainFeed.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        postData = new ArrayList<>();
        //  String API_CODE = API_URL+"&postCode="+ MngData.getData(getActivity(),"postCode","code");
        Log.d("Humbingo", "onCreateView: Api String Request: " + API_URL);


        //loadPost(API_URL);
        loadCachePost(API_URL);


        //final Intent updateService = new Intent(getApplicationContext(), UpdateStrings.class);
        //startService(updateService);


        Button guj = findViewById(R.id.guj);
        Button en = findViewById(R.id.en);
        Button hi = findViewById(R.id.hi);


        guj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MngData.setData(getApplicationContext(), "language", "lng", "gu");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MngData.setData(getApplicationContext(), "language", "lng", "en");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MngData.setData(getApplicationContext(), "language", "lng", "hi");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        //Menu Item Setup RecyclerView
        menuItems = findViewById(R.id.menuItems);
        menuItems.setHasFixedSize(true);
        menuItems.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        menuItemData = new ArrayList<>();

        loadData("http://durgaplacements.com/Api/MenuItems.php?key=madHash456@@");

        String data = MngData.mngString(getApplicationContext(), "Jay. Vijay. Ram. Rahim.");
        Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG).show();
        Log.d("OUTPUT", "onCreate:+++++++++++++++++++++++++ " + data);

    }


    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
    }

    private void loadPost(String apiUrl) {
        StringRequest request = new StringRequest(Request.Method.GET,
                apiUrl, new Response.Listener<String>() {
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
                        postData.add(item);
                    }
                    MainFeedAdapter feedAdapter = new MainFeedAdapter(postData, getApplicationContext());
                    mainFeed.setAdapter(feedAdapter);
                    shimmerFrameLayout.setVisibility(View.GONE);
                    mainFeed.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    private void loadData(String API) {
        StringRequest request = new StringRequest(Request.Method.GET,
                API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("menuitems");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        GetMenu item = new GetMenu(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("cat")
                        );
                        menuItemData.add(item);
                    }
                    MenuItemAdapter menuItemAdapter = new MenuItemAdapter(menuItemData, getApplicationContext());
                    menuItems.setAdapter(menuItemAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


    private void loadCachePost(final String fpost_api) {
        postData = new ArrayList<>();
        CacheRequest cacheRequest = new CacheRequest(0, fpost_api, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    try {
                        JSONObject jo = new JSONObject(jsonString);
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
                            postData.add(item);
                        }
                        MainFeedAdapter feedAdapter = new MainFeedAdapter(postData, getApplicationContext());
                        mainFeed.setAdapter(feedAdapter);
                        shimmerFrameLayout.setVisibility(View.GONE);
                        mainFeed.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        WebServiceProvider.getInstace(getApplicationContext()).addToRequestQueue(cacheRequest);


    }


}
