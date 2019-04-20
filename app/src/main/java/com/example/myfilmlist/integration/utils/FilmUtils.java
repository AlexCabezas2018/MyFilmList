package com.example.myfilmlist.integration.utils;

public class FilmUtils {

    /**
     * Transform time in minutes into time in hours and minutes
     * @param inputTime
     * @return
     */
    public static String transformTime(String inputTime) {
        String[] splitedTime = inputTime.split(" ");
        if(inputTime.equals("N/A") || !splitedTime[1].equals("min")) return inputTime;
        int timeInMinutes = Integer.parseInt(splitedTime[0]);
        if(timeInMinutes < 60) return inputTime;

        int hours = timeInMinutes / 60;
        int minutes = timeInMinutes % 60;

        String formatedString = hours + " h " + ((minutes > 0) ? + minutes + " min": "");
        return formatedString;
    }

    /**
     * Gets the number of pages based of the number of total results.
     * @param totalResults
     * @return
     */
    public static String numberOfPages(int totalResults) {
        int pages = totalResults / 10;
        return (totalResults % 10 > 0) ? Integer.toString(pages + 1) : Integer.toString(pages);
    }
}
