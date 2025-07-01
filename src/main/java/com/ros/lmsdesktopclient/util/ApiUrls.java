package com.ros.lmsdesktopclient.util;

public enum ApiUrls {
    LOGIN("api/login"),
    BOOKS("api/books"),
    GENRES("api/genres"),
    MEMBERS("api/members"),
    HEALTH_CHECK("actuator/health");

    private final String url;

    ApiUrls(String url) {
        //String BASE_URL = "http://ros-lms-api.us-east-1.elasticbeanstalk.com/";
        String BASE_URL = "http://localhost:8080/";
        this.url = BASE_URL + url;
    }

    public String getUrl() {
        return this.url;
    }
}
