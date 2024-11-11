package com.scau.cfd.william_test;

import com.scau.cfd.utils.ConstantSet;
import com.scau.cfd.utils.ImageUtils;
import com.scau.cfd.utils.MyFusionButton;
import io.vproxy.vfx.ui.button.FusionButton;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MultiSelectComboBox extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FusionButton fusionButton = new FusionButton();
        // 创建一个MenuButton
        MyFusionButton button = new MyFusionButton("选择一个选项");
        button.setIcon(new Image(Objects.requireNonNull(ImageUtils.class.getResource("/images/file.png")).toExternalForm(), ConstantSet.ROW_HEIGHT, ConstantSet.ROW_HEIGHT, true, true));
        // 创建MenuItem
        MenuItem item1 = new MenuItem("选项1");
        MenuItem item2 = new MenuItem("选项2");
        MenuItem item3 = new MenuItem("选项3");

        // 给每个MenuItem添加事件处理器
        item1.setOnAction(e -> System.out.println("选择了选项1"));
        item2.setOnAction(e -> System.out.println("选择了选项2"));
        item3.setOnAction(e -> System.out.println("选择了选项3"));
        // 创建ContextMenu
        ContextMenu contextMenu = new ContextMenu();
        // 将MenuItem添加到MenuButton
        contextMenu.getItems().addAll(item1, item2, item3);
        // 创建布局容器
        VBox vBox = new VBox(button);

        // 设置场景
        Scene scene = new Scene(vBox, 200, 150);
        // 将ContextMenu与Button关联
        button.setOnAction(e -> {
            // 显示ContextMenu
            contextMenu.show(button, button.getLayoutX(), button.getLayoutY() + button.getHeight());
        });

        // 设置舞台（窗口）属性
        primaryStage.setTitle("MenuButton 示例");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}