package com.example.asthanewsbeta2.DataManager;

import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.example.asthanewsbeta2.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FileControl {

    public  String getFileFromUrl(String URL) {
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
        }
        return text.toString();
    }
}
