package com.example.myfilmlist.business;

/**
 * Utility class used to implements method which modifies string... basically, algorithms without relation with
 * the logic of the application
 */


public class Utils {

    /**
     * Fills the spaces of a string with +. For example: "hello world" --> "hello+world"
     * @param nameToCompound
     * @return
     */
    public static String getCompoundName(String nameToCompound) {
        String[] dividedFilmName = nameToCompound.trim().split(" "); //We get every word of the compound name (if it is)
        StringBuilder resultFilmName = new StringBuilder();

        if(dividedFilmName.length > 1){ //If there is more than one word...
            for(int i = 0; i < dividedFilmName.length - 1; i++){
                resultFilmName.append(dividedFilmName[i]).append("+"); //We add the word plus "+" to the resulting string for every word of the tittle
            }
            resultFilmName.append(dividedFilmName[dividedFilmName.length - 1]); //Add the last letter (which not contains '+')
        }
        else resultFilmName = new StringBuilder(nameToCompound); //If there is only one word, then we are done

        return resultFilmName.toString();
    }
}
