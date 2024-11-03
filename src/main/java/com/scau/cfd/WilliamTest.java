package com.scau.cfd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class WilliamTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // 创建按钮
        Button back = new Button();

        // 加载 CSS 文件
        back.getStylesheets().add(getClass().getResource("/css/my_theme.css").toExternalForm());
        back.getStyleClass().add("back");

        // 创建场景
        StackPane root = new StackPane();
        root.getChildren().add(back);

        Scene scene = new Scene(root, 200, 200);

        // 设置舞台
        primaryStage.setTitle("Button Hover Effect");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}