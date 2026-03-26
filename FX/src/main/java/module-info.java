module com.example.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.mail;
    requires spring.web;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;

    opens com.example.fx to javafx.fxml;
    exports com.example.fx;
    exports com.example.email;
    opens com.example.email to spring.core, spring.beans, spring.context;
}