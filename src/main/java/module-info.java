module com.example.lmsdesktopclient {
    requires java.net.http;
    requires com.auth0.jwt;
    requires javafx.controls;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires dagger;
    requires javax.inject;
    requires java.desktop;
    requires java.prefs;

    opens com.ros.lmsdesktopclient.models to javafx.base;
    opens com.ros.lmsdesktopclient.util to com.fasterxml.jackson.databind;

    exports com.ros.lmsdesktopclient;
    exports com.ros.lmsdesktopclient.views;
    exports com.ros.lmsdesktopclient.view_models;
    exports com.ros.lmsdesktopclient.commands;
    exports com.ros.lmsdesktopclient.services.service;
    exports com.ros.lmsdesktopclient.models to com.fasterxml.jackson.databind;
    exports com.ros.lmsdesktopclient.util to com.auth0.jwt;
    exports com.ros.lmsdesktopclient.dtos to com.fasterxml.jackson.databind;
    exports com.ros.lmsdesktopclient.di.modules to com.fasterxml.jackson.databind;
    opens com.ros.lmsdesktopclient.di.modules to javafx.base;
    exports com.ros.lmsdesktopclient.di.factories;
    exports com.ros.lmsdesktopclient.util.enums to com.auth0.jwt;
    opens com.ros.lmsdesktopclient.util.enums to com.fasterxml.jackson.databind;
    exports com.ros.lmsdesktopclient.util.annotations to com.auth0.jwt;
    opens com.ros.lmsdesktopclient.util.annotations to com.fasterxml.jackson.databind;
}