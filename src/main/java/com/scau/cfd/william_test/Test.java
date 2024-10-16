package com.scau.cfd.william_test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 这个类是：
 *
 * @author: William
 * @date: 2024-10-07T21:01:44CST 21:01
 * @description:
 */
public class Test extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        Pane root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
