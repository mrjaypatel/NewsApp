package com.example.asthanewsbeta2.Modules;

public class GetPost {
    private String id;
    private String title;
    private String imgUrl;
    private String details;
    private String date;
    private String views;
    private String postCode;

    public GetPost(String id, String title, String imgUrl, String details, String date, String views, String postCode) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.details = details;
        this.date = date;
        this.views = views;
        this.postCode = postCode;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }
}
