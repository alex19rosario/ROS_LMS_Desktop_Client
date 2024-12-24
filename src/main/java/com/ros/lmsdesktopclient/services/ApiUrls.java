package com.ros.lmsdesktopclient.services;

public enum ApiUrls {
    LOGIN("login");

    private String url;
    private final String BASE_URL = "http://localhost:8080/api/";

    ApiUrls(String url){
        this.url = BASE_URL + url;
    }

    String getUrl(){
        return this.url;
    }
}
