package com.example.fx;

import com.example.SpringApp;
import com.example.catalogue.CatalogueTester;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Login extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("App");
        stage.setMinWidth(870);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setOnCloseRequest(event -> System.exit(0));
        stage.show();
    }

    public static void main(String[] args) {
        Thread springThread = new Thread(() -> SpringApplication.run(SpringApp.class, args));
        springThread.setDaemon(true);
        springThread.start();

        boolean serverReady = waitForServer("localhost", 8080, 15_000);
        System.out.println("Spring server ready: " + serverReady);

        if (serverReady) {
            CatalogueTester.loadSampleCatalogue();
        } else {
            System.out.println("Spring server did not become ready in time.");
        }

        launch(args);
    }

    private static boolean waitForServer(String host, int port, long timeoutMillis) {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMillis) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), 1000);
                return true;
            } catch (IOException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }

        return false;
    }
}