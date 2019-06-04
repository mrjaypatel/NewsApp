package com.example.asthanewsbeta2.Modules;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyGlobal extends Application {

    //For setting total post count for ApiGrabber
    private String checkPostCount;
    public String getCheckPostCount() {
        return checkPostCount;
    }
    public void setCheckPostCount(String checkPostCount) {
        this.checkPostCount = checkPostCount;
    }


    private String checkFeedCount;

    public String getCheckFeedCount() {
        return checkFeedCount;
    }

    public void setCheckFeedCount(String checkFeedCount) {
        this.checkFeedCount = checkFeedCount;
    }

    //For setting total menu count for ApiGrabber
    private String checkMenuCount;
    public String getCheckMenuCount() {
        return checkMenuCount;
    }

    public void setCheckMenuCount(String checkMenuCount) {
        this.checkMenuCount = checkMenuCount;
    }

    //For setting tmp_Gujarati_string for google translate store
    private String tmpGujStr;

    public String getTmpGujStr() {
        return tmpGujStr;
    }

    public void setTmpGujStr(String tmpGujStr) {
        this.tmpGujStr = tmpGujStr;
    }

    //For setting tmp_Hindi_string for google translate store
    private String tmpHindiStr;

    public String getTmpHindiStr() {
        return tmpHindiStr;
    }

    public void setTmpHindiStr(String tmpHindiStr) {
        this.tmpHindiStr = tmpHindiStr;
    }


    //For Storing String lists
     private List<String> stringList = new ArrayList<>();

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }


    public String myTitle;

    public String getMyTitle() {
        return myTitle;
    }

    public void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    public String myDetails;

    public String getMyDetails() {
        return myDetails;
    }

    public void setMyDetails(String myDetails) {
        this.myDetails = myDetails;
    }



}
