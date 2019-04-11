package com.example.myfilmlist.business.film;

public class TFilmPreview {
    private String title;
    private String year;
    private String imdbID;
    private String type;
    private String imageURL;

    public TFilmPreview() {}

    public TFilmPreview(String title, String year, String imdbID, String type, String imageURL) {
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
