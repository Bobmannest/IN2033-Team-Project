package com.example.promotion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// small, now unused test UI for testing the promotion screen in early development, before we had the full application connected

public class TestPromotionUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/fx/Promotion.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Promotion Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}