module com.example.lmsdesktopclient {
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.auth0.jwt;
    requires javafx.controls;
    requires java.desktop;
    requires java.prefs;


    opens com.ros.lmsdesktopclient to javafx.fxml;
    opens com.ros.lmsdesktopclient.views to javafx.fxml;
    opens com.ros.lmsdesktopclient.models to javafx.base;
    exports com.ros.lmsdesktopclient;
    exports com.ros.lmsdesktopclient.views to javafx.fxml;
    exports com.ros.lmsdesktopclient.models to com.fasterxml.jackson.databind;
    exports com.ros.lmsdesktopclient.util to com.auth0.jwt;
}