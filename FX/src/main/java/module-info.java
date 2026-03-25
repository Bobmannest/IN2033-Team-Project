module com.example.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.mail;


    opens com.example.fx to javafx.fxml;
    exports com.example.fx;
}