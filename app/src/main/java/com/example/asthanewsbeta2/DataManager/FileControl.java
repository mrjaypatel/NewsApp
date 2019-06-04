package com.example.asthanewsbeta2.DataManager;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class FileControl {

    public String getFileFromUrl(String URL) {
        StringBuilder text = new StringBuilder();
        try {            // Create a URL for the desired page
            URL url = new URL(URL);

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                // str is one line of text; readLine() strips the newline character(s)
                text.append(str);
                text.append('\n');
            }
            in.close();
        } catch (Exception e) {
            Log.e("MYFILE", "getFileFromUrl: ", e);
            e.printStackTrace();
        }
        return text.toString();
    }
}
