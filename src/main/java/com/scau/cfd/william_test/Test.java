package com.scau.cfd.william_test;

import com.scau.cfd.controller.MainTestController;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.util.FXUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
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
    public void start(Stage primaryStage) throws Exception {
        var stage = new VStage(primaryStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main_test.fxml"));
        AnchorPane root = fxmlLoader.load();
        // 由于tableview是自定义类，没继承control类，所以不能借助fxml初始化，只能自己手动初始化
        root.getChildren().add(MainTestController.getTableView().getNode());
        stage.getSceneGroup().addScene(new VScene(VSceneRole.MAIN) {{
            getContentPane().getChildren().add(root);
        }});
        VScene scene = new VScene(VSceneRole.MAIN) {{
            getContentPane().getChildren().add(root);
        }};
        stage.getInitialScene().getContentPane().getChildren().add(scene.getNode());
        stage.getInitialScene().enableAutoContentWidthHeight();
        FXUtils.observeWidthHeightCenter(stage.getInitialScene().getContentPane(), scene.getNode());
        stage.setTitle("Test");
        stage.getStage().setWidth(1200);
        stage.getStage().setHeight(1000);
        stage.show();
    }
}
