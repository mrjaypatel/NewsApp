package com.example.asthanewsbeta2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.asthanewsbeta2.DataManager.ApiDataGrabber;
import com.example.asthanewsbeta2.Modules.GetMenu;
import com.example.asthanewsbeta2.Modules.GetPostFromLocal;
import com.example.asthanewsbeta2.Modules.MainFeedAdapter;
import com.example.asthanewsbeta2.Modules.MenuItemAdapter;
import com.example.asthanewsbeta2.Modules.MngData;
import com.example.asthanewsbeta2.OfflineDataManager.SQLHelper;
import com.example.asthanewsbeta2.DataManager.FileControl;
import com.example.asthanewsbeta2.Services.UpdateStrings;
import com.example.asthanewsbeta2.Services.WebServiceProvider;
import com.facebook.shimmer.ShimmerFrameLayout;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private RecyclerView menuItems;
    private List<GetMenu> menuItemData;
    private List<GetPostFromLocal> postData;
    private List<GetMenu> menuData;
    public RecyclerView mainFeed;

    private ShimmerFrameLayout shimmerFrameLayout;

    public static String POST_API = "http://durgaplacements.com/Api/mainFeedApi.php?key=madHash456@@&postCode=1";
    public static String MENU_API = "http://durgaplacements.com/Api/MenuItems.php?key=madHash456@@";
    public static String ALLPOST_API = "http://durgaplacements.com/Api/allPost.php?key=madHash456@@";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLHelper sqlHelper = new SQLHelper(getApplicationContext());
       /* sqlHelper.emptyTable("OFFLINE_POST");
        sqlHelper.emptyTable("OFFLINE_MENU");*/

        //Check Post code and language if not seted for start
        checkPostConfig();


        //Facebook preload for application main feed!
        shimmerFrameLayout = findViewById(R.id.placeholderPost);


        //Menu Item Setup RecyclerView
        menuItems = findViewById(R.id.menuItems);
        menuItems.setHasFixedSize(true);
        menuItems.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        menuItemData = new ArrayList<>();
        //Loads Menu Items From The Web API
        loadOfflineMenu();

        //Mainfeed recycler View Handler
        mainFeed = findViewById(R.id.mainFeed);
        mainFeed.setHasFixedSize(true);
        mainFeed.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //Loading Offline Post from SQLITE database
        loadOfflinePost();


        final Intent updateService = new Intent(getApplicationContext(), UpdateStrings.class);
        startService(updateService);


        //Actionbar language Menu btn
        Button menuLng = findViewById(R.id.languageMenu);
        menuLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                FileControl fc = new FileControl();
                String FEEDAPI = "http://durgaplacements.com/Api/feed.php?key=madHash456@@&postCode=1";
               List<String> test =new ArrayList<>();
               test = ApiDataGrabber.getFeedFromFile(getApplicationContext(),FEEDAPI,"gu");
                Log.d("MYFILE", "In to Thread File 1! " );
                int size = test.size();
                Log.d("MYFILE", "List Item Count Set: "+size);

                String tmpTitle =MngData.getData(getApplicationContext(),"tmpString","title");
                String tmpDetails=MngData.getData(getApplicationContext(),"tmpString","details");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(tmpTitle.equals("1")){
                    ApiDataGrabber.getFeedFromFile(getApplicationContext(),FEEDAPI,"gu");
                    Log.d("FileControl", "INto Tmp Title!");
                }else{
                    String title = fc.getFileFromUrl(tmpTitle);
                    Log.d("FileControl", "onCreate: Temp Title Success: " + tmpTitle + "\n"+title);

                }



                if(tmpDetails.equals("1")){
                    ApiDataGrabber.getFeedFromFile(getApplicationContext(),FEEDAPI,"gu");
                    Log.d("FileControl", "INto Tmp Details!");
                }else{
                    String details = fc.getFileFromUrl(tmpDetails);
                    Log.d("FileControl", "onCreate: Temp Details Success: " + tmpDetails+"\n "+ details);


                }

                MngData.setData(getApplicationContext(),"tmpString","title","1");
                MngData.setData(getApplicationContext(),"tmpString","details","1");


/*                String data = fc.getFileFromUrl("http://durgaplacements.com/Data/2019/6/3/1/gu/details.txt");
                Log.d("FileControl", "onCreate: data from web: " + data);*/
            }
        }).start();


    }

    private void checkPostConfig() {
        String postCode = MngData.getData(getApplicationContext(), "postCode", "code");
        String lng = MngData.getData(getApplicationContext(), "language", "lng");
        if (lng.equals("1")) {
            MngData.setData(getApplicationContext(), "language", "lng", "en");
        }

        Log.d("HUMBINGO", "checkPostConfig: POSTCODE: " + postCode);
        Log.d("HUMBINGO", "checkPostConfig: LANGUAGE: " + lng);

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


    /**
     * Below is menu items code
     */
    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.languages, popup.getMenu());
        popup.show();
    }

    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.languages);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.guj:
                MngData.setData(getApplicationContext(), "language", "lng", "gu");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.eng:
                MngData.setData(getApplicationContext(), "language", "lng", "en");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.hin:
                MngData.setData(getApplicationContext(), "language", "lng", "hi");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

//Custom Actionbar Menu Ends


    private void loadOfflinePost() {
        SQLHelper sh = new SQLHelper(getApplicationContext());
        String lng = MngData.getDataDefault(getApplicationContext(), "language", "lng", "en");
        Log.d("HUMBINGO", "loadOfflinePost: OFFLINE POST LANGUAGE: " + lng);
        String postCode = MngData.getData(getApplicationContext(), "postCode", "code");
        if (sh.countRecord("OFFLINE_POST") <= 0) {
            loadOnlinePost();
        } else {
            postData = new ArrayList<>();
            postData = sh.getOffPostList(postCode, lng);
            MainFeedAdapter feedAdapter = new MainFeedAdapter(postData, getApplicationContext());
            mainFeed.setAdapter(feedAdapter);
            shimmerFrameLayout.setVisibility(View.GONE);
            mainFeed.setVisibility(View.VISIBLE);
        }
    }

    private void loadOfflineMenu() {
        menuData = new ArrayList<>();
        SQLHelper sh = new SQLHelper(getApplicationContext());
        String lng = MngData.getData(getApplicationContext(), "language", "lng");
        if (sh.countRecord("OFFLINE_MENU") <= 0) {
            loadOnlineMenu();
        } else {
            menuData = sh.getOffMenuList(lng);
            MenuItemAdapter menuItemAdapter = new MenuItemAdapter(menuData, getApplicationContext());
            menuItems.setAdapter(menuItemAdapter);
        }

    }

    private void loadOnlineMenu() {
        final List<GetMenu> menuList = new ArrayList<>();
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
                        menuList.add(item);
                    }

                    menuData = menuList;
                    MenuItemAdapter menuItemAdapter = new MenuItemAdapter(menuData, getApplicationContext());
                    menuItems.setAdapter(menuItemAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        WebServiceProvider.getInstace(getApplicationContext()).addToRequestQueue(stringRequest1);
    }

    private void loadOnlinePost() {
        final List<GetPostFromLocal> postList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, POST_API, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("mainfeed");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        final GetPostFromLocal item = new GetPostFromLocal(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("imgUrl"),
                                o.getString("details"),
                                o.getString("date"),
                                o.getString("views"),
                                o.getString("postcode"),
                                "en"
                        );
                        postList.add(item);
                    }
                    List<GetPostFromLocal> olPost = postList;
                    MainFeedAdapter feedAdapter = new MainFeedAdapter(olPost, getApplicationContext());
                    mainFeed.setAdapter(feedAdapter);
                    shimmerFrameLayout.setVisibility(View.GONE);
                    mainFeed.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        WebServiceProvider.getInstace(getApplicationContext()).addToRequestQueue(stringRequest);

    }


//Class End
}