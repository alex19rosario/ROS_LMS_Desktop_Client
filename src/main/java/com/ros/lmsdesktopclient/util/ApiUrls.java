package com.ros.lmsdesktopclient.util;

public enum ApiUrls {
    LOGIN("login"),
    BOOKS("books");

    private String url;
    private final String BASE_URL = "http://localhost:8080/api/";

    ApiUrls(String url){
        this.url = BASE_URL + url;
    }

    public String getUrl(){
        return this.url;
    }
}
