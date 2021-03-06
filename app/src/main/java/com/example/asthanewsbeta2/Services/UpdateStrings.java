package com.example.asthanewsbeta2.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import com.example.asthanewsbeta2.Modules.MngData;
import com.example.asthanewsbeta2.DataManager.ApiDataGrabber;

import java.util.Date;

public class UpdateStrings extends Service {
    public static String POST_API = "http://durgaplacements.com/Api/mainFeedApi.php?key=madHash456@@&postCode=1";
    public static String MENU_API = "http://durgaplacements.com/Api/MenuItems.php?key=madHash456@@";
    public static String ALLPOST_API = "http://durgaplacements.com/Api/allPost.php?key=madHash456@@";
    public static String FEED_API = "http://durgaplacements.com/Api/feed.php?key=madHash456@@";

    String TAG = "inService";

    public UpdateStrings() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //Store Post/Menu in starting of service

                setOfflinePost();
                setOfflineMenu();

                while (true) {
                    try {
                        Thread.sleep(30000);
                        //Offline Post Data Store
                        String postCount = ApiDataGrabber.checkDataChange(getApplicationContext(), "feed");
                        String getExsist = MngData.getData(getApplicationContext(), "checkNewPost", "post");
                        if (postCount != null) {
                            if (!getExsist.equals(postCount)) {
                                Log.d(TAG, "Old Log Offline Post Count: Exsist Post: " + getExsist + " Online Post: " + postCount);
                                setOfflinePost();
                                MngData.setData(getApplicationContext(), "checkNewPost", "post", postCount);
                            } else {
                                String newUpdate = MngData.getData(getApplicationContext(), "checkNewPost", "post");
                                Log.d(TAG, "All Post Updated! Post Count: Exsist Post: " + newUpdate + " Online post: " + postCount);
                            }
                        }


                        //Offline Menu Data Store
                        String menuCount = ApiDataGrabber.checkDataChange(getApplicationContext(), "menuitems");
                        String getExsistMenu = MngData.getData(getApplicationContext(), "checkNewMenu", "menu");
                        if (menuCount != null) {
                            if (!getExsistMenu.equals(menuCount)) {
                                Log.d(TAG, "Old Log Offline Menu Count: Exsist Menu Item Count: " + getExsistMenu + " Online Menu Items: " + menuCount);
                                setOfflineMenu();
                                MngData.setData(getApplicationContext(), "checkNewMenu", "menu", menuCount);

                            } else {
                                String newUpdate = MngData.getData(getApplicationContext(), "checkNewMenu", "menu");
                                Log.d(TAG, "All Menu Updated! Menu Count: Exsist Menu: " + newUpdate + " Online menu: " + menuCount);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroy!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    private void setOfflinePost() {
        ApiDataGrabber.storeFeedOfline(getApplicationContext(), FEED_API, "gu");
    }

    private void setOfflineMenu() {
        ApiDataGrabber.storeMenuOffline(getApplicationContext(), MENU_API);
    }




}

