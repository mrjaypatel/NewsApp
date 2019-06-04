package com.example.asthanewsbeta2;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;

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

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        SQLHelper sqlHelper = new SQLHelper(getApplicationContext());

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        //Check Post code and language if not seted for st sqlHelper.emptyTable("OFFLINE_POST");
        //        sqlHelper.emptyTable("OFFLINE_MENU");art
        checkPostConfig();
        final Intent updateService = new Intent(getApplicationContext(), UpdateStrings.class);
        startService(updateService);

        //Facebook preload for application main feed!
        shimmerFrameLayout = findViewById(R.id.placeholderPost);


        //Menu Item Setup RecyclerView
        menuItems = findViewById(R.id.menuItems);
        menuItems.setHasFixedSize(true);
        menuItems.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        menuItemData = new ArrayList<>();
        //Loads Menu Items From The Web API


        //Mainfeed recycler View Handler
        mainFeed = findViewById(R.id.mainFeed);
        mainFeed.setHasFixedSize(true);
        mainFeed.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        loadOfflineMenu();







        //Actionbar language Menu btn
      /*  Button menuLng = findViewById(R.id.languageMenu);
        menuLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
        });*/

        //Requiest Gujarati file data and Store into db
        /*String FEEDAPI = "http://durgaplacements.com/Api/feed.php?key=madHash456@@&postCode=1";
        ApiDataGrabber.storeFeedOfline(getApplicationContext(), FEEDAPI, "gu");*/


    }

    private void setOfflineData() {
        ApiDataGrabber.storeFeedOfline(getApplicationContext(), ALLPOST_API, "gu");
        ApiDataGrabber.storeMenuOffline(getApplicationContext(), MENU_API);
    }


    private void checkPostConfig() {
        String postCode = MngData.getData(getApplicationContext(), "postCode", "code");
        String lng = MngData.getData(getApplicationContext(), "language", "lng");
        if (lng.equals("1")) {
            MngData.setData(getApplicationContext(), "language", "lng", "gu");
        } else {
            MngData.setData(getApplicationContext(), "language", "lng", "gu");
        }
        Log.d("HUMBINGO", "checkPostConfig: POSTCODE: " + postCode);
        Log.d("HUMBINGO", "checkPostConfig: LANGUAGE: " + lng);



        SQLHelper sqlHelper = new SQLHelper(getApplicationContext());
        for(String data: sqlHelper.getCellMenu(1)){
            Log.d("MenuInfo", "checkPostConfig: Menu Title: "+data);

        }
        for(String data: sqlHelper.getCellMenu(2)){
            Log.d("MenuInfo", "checkPostConfig: Menu Title: "+data);

        }




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
                MngData.setData(getApplicationContext(), "language", "lng", "gu");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.hin:
                MngData.setData(getApplicationContext(), "language", "lng", "gu");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

//Custom Actionbar Menu Ends


    private void loadOfflinePost() {
        SQLHelper sh = new SQLHelper(getApplicationContext());
        String lng = "gu";
        String postCode = MngData.getData(getApplicationContext(), "postCode", "code");


        if (sh.countRecord("OFFLINE_POST") > 0) {
            postData = new ArrayList<>();
            postData = sh.getOffPostList(postCode, lng);
            MainFeedAdapter feedAdapter = new MainFeedAdapter(postData, getApplicationContext());
            mainFeed.setAdapter(feedAdapter);
            shimmerFrameLayout.setVisibility(View.GONE);
            mainFeed.setVisibility(View.VISIBLE);
        }else{
            setOfflineData();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

    }

    private void loadOfflineMenu() {
        menuData = new ArrayList<>();
        SQLHelper sh = new SQLHelper(getApplicationContext());
        String lng = "gu";
        if (sh.countRecord("OFFLINE_MENU") > 0) {
            menuData = sh.getOffMenuList(lng);
            MenuItemAdapter menuItemAdapter = new MenuItemAdapter(menuData, getApplicationContext());
            menuItems.setAdapter(menuItemAdapter);
            //Loading Offline Post from SQLITE database
            loadOfflinePost();

        } else {
            setOfflineData();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }

    }

//Class End
}