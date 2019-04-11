package com.example.myfilmlist.business.film;

import java.util.List;

public class TFilmFull {
    private String title;
    private String imageURL;
    private String genre;
    private String rate;
    private String year;
    private String duration;
    private List<String> directors;
    private List<String> actors;
    private String plot;

    public TFilmFull() {};

    public TFilmFull(String title, String imageURL, String genre, String year,
                     String duration, List<String> directors, List<String> actors, String plot, String rate) {
        this.title = title;
        this.imageURL = imageURL;
        this.genre = genre;
        this.year = year;
        this.duration = duration;
        this.directors = directors;
        this.actors = actors;
        this.plot = plot;
        this.rate = rate;
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

    public List<String> getDirectors() {
        return directors;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
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
}