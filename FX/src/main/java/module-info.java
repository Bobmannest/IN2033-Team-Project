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
    requires java.net.http;
    requires com.fasterxml.jackson.annotation;
    //requires javafx.graphics;
    //requires javafx.swt;

    opens com.example.fx to javafx.fxml;
    exports com.example.fx;
    exports com.example.email;
    opens com.example.email to spring.core, spring.beans, spring.context;
    exports com.example.basket;
    opens com.example.basket to javafx.fxml;
    exports com.example.catalogue;
    opens com.example.catalogue to javafx.fxml;
    exports com.example;
    opens com.example to spring.beans, spring.context, spring.core;
    exports com.example.payment;
    opens com.example.payment to spring.beans, spring.context, spring.core;
}