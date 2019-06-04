package com.example.asthanewsbeta2.Modules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.example.asthanewsbeta2.Services.GoogleTranslate;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MngData {
    public static List<String> myStr = new ArrayList<>();


    public static void setData(Context context, final String fileName, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getData(Context context, final String fileName, String key) {
        String name = "1";
        SharedPreferences prefs = context.getSharedPreferences(fileName, MODE_PRIVATE);
        String restoredText = prefs.getString(key, name);
        if (restoredText != null) {
            name = prefs.getString(key, "1");//"No name defined" is the default value.
            //int idName = prefs.getInt("idName", 0); //0 is the default value.
        }
        return name;
    }

    public static String getDataDefault(Context context, final String fileName, String key, String Default) {
        String name = "";
        SharedPreferences prefs = context.getSharedPreferences(fileName, MODE_PRIVATE);
        String restoredText = prefs.getString(key, Default);
        if (restoredText != null) {
            name = prefs.getString(key, Default);//"No name defined" is the default value.
            //int idName = prefs.getInt("idName", 0); //0 is the default value.
        }
        return name;
    }


    public static void setIntentData(Context context, Class dest, String key, String value) {
        Intent myintent = new Intent(context, dest).putExtra(key, value);
        context.startActivity(myintent);
    }

    public static String getIntentData(Class me, String key) {
        String s = "";
        //s = getIntent().getStringExtra(key);
        return s;

    }


    public static String finalData;

    //http://durgaplacements.com/Api/RecentPost.php?key=madHash456@@&postCode=3 //Feature post Code Api
    public static String mngString(Context context, String data) {
        String newString = "";
        String[] splitArray = data.split("\\.");
        int count = splitArray.length - 1;
        String stringPart;
        List<String> strList = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            stringPart = data.split("[.]")[i];
            strList.add(stringPart);
        }

        //newString = MngData.getData(context, "myConvert", "tmpStr");
        if (stringConverter(context, strList) != null) {
            newString = stringConverter(context, strList);
        } else {
            newString = stringConverter(context, strList);
        }

        Log.d("translate", "mngString: data: " + newString);
        MngData.setData(context, "myConvert", "tmpStr", "");
        outputStr = "";
        return newString;
    }


    public static String outputStr = "";

    public static String stringConverter(final Context context, final List<String> strList) {
        final MyGlobal mg = new MyGlobal();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String lng = MngData.getData(context, "language", "lng");
                    String title;
                    List<String> tmpList = new ArrayList<>();
                    for (int i = 0; i <= strList.size() - 1; i++) {
                        String data = strList.get(i);
                        title = GoogleTranslate.translate(lng, data);
                        if (!title.contains(".")) {
                            title += ".";
                        }
                        tmpList.add(title);
                    }

                    for (String Data : tmpList) {
                        outputStr += Data;

                    }
                    mg.setStringList(tmpList);
                    mg.setTmpGujStr(outputStr);
                    MngData.setData(context, "myConvert", "tmpStr", outputStr);
                } catch (Exception e) {
                    e.printStackTrace();
                    mg.setTmpGujStr("Opps, no data found!");
                }
            }
        }).start();


        if (mg.getStringList() != null) {
            for (String Data : mg.getStringList()) {

                Log.d("mngData", "List Items Converted: " + Data);
            }
        }else{
            Log.d("mngData", "Null Response data" );
        }


        return mg.getTmpGujStr();
    }

    public static String getDeviceInfo(Context context, String type) {

        switch (type) {
            case "version":
                return System.getProperty("os.version"); // OS version
            case "device":
                return android.os.Build.DEVICE;           // Device
            case "api":
                return android.os.Build.VERSION.SDK;      //Api Level
            case "model":
                return android.os.Build.MODEL;            // Model
            case "product":
                return android.os.Build.PRODUCT;         // Product
            case "mac":
                WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                String address = info.getMacAddress();
                return address;
            default:
                return "";

        }

    }


}
