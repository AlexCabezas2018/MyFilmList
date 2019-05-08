package com.example.myfilmlist.business.film;

import java.io.Serializable;
import java.util.List;

public class TFilmFull implements Serializable {
    private String title;
    private String imageURL;
    private String genre;
    private String rate;
    private String year;
    private String duration;
    private String directors;
    private String actors;
    private String plot;
    private String type;
    private String imbdid;

    public TFilmFull() {};

    public TFilmFull(String title, String imageURL, String genre, String year,
                     String duration, String directors, String actors, String plot,
                     String rate, String type, String imbdid) {
        this.title = title;
        this.imageURL = imageURL;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.directors = directors;
        this.actors = actors;
        this.plot = plot;
        this.rate = rate;
        this.type = type;
        this.imbdid = imbdid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getActors() { return actors;  }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setType(String type) { this.type = type;}

    public String getType() { return type;}

    public String getImbdid() { return imbdid; }

    public void setImbdid(String imbdid) { this.imbdid = imbdid; }
}
