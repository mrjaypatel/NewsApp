package com.example.asthanewsbeta2.Modules;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.asthanewsbeta2.Services.GoogleTranslate;

import java.io.IOException;


import static android.content.Context.MODE_PRIVATE;

public class MngData {

    public static void setData(Context context, final String fileName, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(fileName, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String getData(Context context, final String fileName, String key) {
        String name = "1";
        SharedPreferences prefs = context.getSharedPreferences(fileName, MODE_PRIVATE);
        String restoredText = prefs.getString(key, null);
        if (restoredText != null) {
            name = prefs.getString(key, "No value defined");//"No name defined" is the default value.
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
        String newString;
        String[] splitArray = data.split("\\.");
        int count = splitArray.length - 1;
        String stringPart = "";
        for (int i = 0; i <= count; i++) {
            stringPart = data.split("[.]")[i];
            stringPart += ".";
            stringConverter(context,stringPart);
        }
        newString = getData(context, "strStore","str");
        MngData.setData(context, "strStore","str", "");
        outputStr = "";
        return newString;
    }


    public static String outputStr = "";
    public static void stringConverter(final Context context, final String data) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String lng = MngData.getData(context, "language", "lng");
                    String title;
                    String tmp = "";
                    title = GoogleTranslate.translate(lng, data);
                    if(!title.contains("."))
                    {
                        title += ".";
                    }
                    tmp += title;
                    outputStr += tmp;
                    Log.d("OUTPUT", "run: Output Str " + outputStr);
                    MngData.setData(context, "strStore","str", outputStr);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


}
