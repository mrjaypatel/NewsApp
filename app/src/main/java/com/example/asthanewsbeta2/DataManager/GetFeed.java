package com.example.asthanewsbeta2.DataManager;

public class GetFeed {
    private String id;
    private String title_en;
    private String title_gu;
    private String title_hi;
    private String details_en;
    private String details_gu;
    private String details_hi;
    private String date_time;
    private String imgUrl;
    private String views;
    private String postcode;

    public GetFeed(String id, String title_en, String title_gu, String title_hi, String details_en, String details_gu, String details_hi, String date_time, String imgUrl, String views, String postcode) {
        this.id = id;
        this.title_en = title_en;
        this.title_gu = title_gu;
        this.title_hi = title_hi;
        this.details_en = details_en;
        this.details_gu = details_gu;
        this.details_hi = details_hi;
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

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_gu() {
        return title_gu;
    }

    public void setTitle_gu(String title_gu) {
        this.title_gu = title_gu;
    }

    public String getTitle_hi() {
        return title_hi;
    }

    public void setTitle_hi(String title_hi) {
        this.title_hi = title_hi;
    }

    public String getDetails_en() {
        return details_en;
    }

    public void setDetails_en(String details_en) {
        this.details_en = details_en;
    }

    public String getDetails_gu() {
        return details_gu;
    }

    public void setDetails_gu(String details_gu) {
        this.details_gu = details_gu;
    }

    public String getDetails_hi() {
        return details_hi;
    }

    public void setDetails_hi(String details_hi) {
        this.details_hi = details_hi;
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
