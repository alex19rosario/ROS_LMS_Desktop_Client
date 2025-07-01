module com.example.lmsdesktopclient {
    requires java.net.http;
    requires com.auth0.jwt;
    requires javafx.controls;
    requires java.prefs;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;

    opens com.ros.lmsdesktopclient.models to javafx.base;

    exports com.ros.lmsdesktopclient;
    exports com.ros.lmsdesktopclient.models to com.fasterxml.jackson.databind;
    exports com.ros.lmsdesktopclient.util to com.auth0.jwt;
    exports com.ros.lmsdesktopclient.dtos to com.fasterxml.jackson.databind;
}