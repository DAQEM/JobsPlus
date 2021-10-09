package me.daqem.jobsplus.utils;

public class Capitalizer {

    public static String capitalizeWord(String str){
        String[] words = str.split("\\s");
        StringBuilder capitalizeWord= new StringBuilder();
        for(String w : words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizeWord.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return capitalizeWord.toString().trim();
    }
}
