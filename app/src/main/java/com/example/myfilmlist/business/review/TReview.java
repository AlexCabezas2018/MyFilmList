package com.example.myfilmlist.business.review;

public class TReview {
    private int id;
    private String content;
    private String imdbId;

    public TReview() { }

    public TReview(int id, String content, String imdbId) {
        this.id = id;
        this.content = content;
        this.imdbId = imdbId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
}
