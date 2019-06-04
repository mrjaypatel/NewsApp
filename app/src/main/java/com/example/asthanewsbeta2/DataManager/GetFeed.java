package com.example.asthanewsbeta2.DataManager;

public class GetFeed {
    private String id;
    private String title_gu;

    private String details_gu;
    private String date_time;
    private String imgUrl;
    private String views;
    private String postcode;

    public GetFeed(String id, String title_gu, String details_gu, String date_time, String imgUrl, String views, String postcode) {
        this.id = id;
        this.title_gu = title_gu;
        this.details_gu = details_gu;
        this.date_time = date_time;
        this.imgUrl = imgUrl;
        this.views = views;
        this.postcode = postcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTitle_gu() {
        return title_gu;
    }

    public void setTitle_gu(String title_gu) {
        this.title_gu = title_gu;
    }


    public String getDetails_gu() {
        return details_gu;
    }

    public void setDetails_gu(String details_gu) {
        this.details_gu = details_gu;
    }


    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
