package com.example.asthanewsbeta2.Modules;

public class GetMenu {
    private String id;
    private String title;
    private String cat;

    public GetMenu(String id, String title, String cat) {
        this.id = id;
        this.title = title;
        this.cat = cat;
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

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }
}
