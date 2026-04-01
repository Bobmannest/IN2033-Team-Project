package com.example.catalogue;

import com.example.fx.Login;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import com.example.SpringApp;
import java.io.IOException;

public class Catalogue extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("Catalogue.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 633);
        stage.setTitle("Catalogue");
        stage.setMinWidth(820);
        stage.setMinHeight(633);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        /* Launches SpringApp so will need to move this to Login screen later
        because that will be the first thing that user will see */
        Thread springThread = new Thread(() -> {
            SpringApplication.run(SpringApp.class, args);
        });
        springThread.setDaemon(true);
        springThread.start();

        //Launch Catalogue GUI
        launch();
    }
}
