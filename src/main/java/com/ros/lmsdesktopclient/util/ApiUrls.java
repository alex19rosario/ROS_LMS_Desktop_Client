package com.ros.lmsdesktopclient.util;

public enum ApiUrls {
    LOGIN("login"),
    BOOKS("books"),
    GENRES("genres"),
    MEMBERS("members");

    private final String url;

    ApiUrls(String url){
        String BASE_URL = "http://localhost:8080/api/";
        this.url = BASE_URL + url;
    }

    public String getUrl(){
        return this.url;
    }
}
