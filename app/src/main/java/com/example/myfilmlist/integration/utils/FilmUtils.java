package com.example.myfilmlist.integration.utils;

public class FilmUtils {
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
}
