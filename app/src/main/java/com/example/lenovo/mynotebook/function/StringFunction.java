package com.example.lenovo.mynotebook.function;

public class StringFunction {

    public static boolean isNullOrEmpty(String string) {
        if(string==null || string.length()==0){
            return true;
        }
        return false;
    }

    public static boolean isNotNullOrEmpty(String string) {
        if(string==null || string.length()==0){
            return false;
        }
        return true;
    }


    public static String clearAllSpace(String string) {
        if(isNullOrEmpty(string)){
            return "";
        }
        return string.replaceAll(" ", "");

    }
}

