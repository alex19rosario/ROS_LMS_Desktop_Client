module com.example.lmsdesktopclient {
    requires javafx.fxml;
    requires java.prefs;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.auth0.jwt;
    requires java.xml;
    requires javafx.controls;


    opens com.ros.lmsdesktopclient to javafx.fxml;
    opens com.ros.lmsdesktopclient.views to javafx.fxml;
    exports com.ros.lmsdesktopclient;
    exports com.ros.lmsdesktopclient.views to javafx.fxml;
    exports com.ros.lmsdesktopclient.models to com.fasterxml.jackson.databind;
    exports com.ros.lmsdesktopclient.util to com.auth0.jwt;
}