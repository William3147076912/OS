package com.scau.cfd.app;

import com.leewyatt.rxcontrols.controls.RXLineButton;
import io.vproxy.vfx.ui.button.FusionButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-17T14:16:54CST 14:16
 * @description:
 */
public class WilliamTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane load = new AnchorPane();
        load.setPrefHeight(500);
        load.setPrefWidth(500);
        FusionButton fusionButton = new FusionButton() {{
            setWidth(100);
            setHeight(100);
        }};
        load.getChildren().add(fusionButton);
        primaryStage.setScene(new Scene(load));
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.show();
    }
}
