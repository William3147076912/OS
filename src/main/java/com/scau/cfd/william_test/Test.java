package com.scau.cfd.william_test;

import com.scau.cfd.controller.MainTestController;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneHideMethod;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
        addMainScene(stage, root);
        addQuestionScene(stage);
        stage.setTitle("Test");
        stage.getStage().setWidth(1200);
        stage.getStage().setHeight(1000);
        stage.show();
    }

    void addMainScene(VStage stage, AnchorPane root) {
        // 由于tableview是自定义类，没继承control类，所以不能借助fxml初始化，只能自己手动初始化
        root.getChildren().add(MainTestController.getTableView().getNode());
        root.getChildren().add(MainTestController.getControlPane().getNode());
        stage.getSceneGroup().addScene(new VScene(VSceneRole.MAIN) {{
            getContentPane().getChildren().add(root);
        }});
        VScene scene = new VScene(VSceneRole.MAIN) {{
            getContentPane().getChildren().add(root);
        }};
        stage.getInitialScene().getContentPane().getChildren().add(scene.getNode());
        stage.getInitialScene().enableAutoContentWidthHeight();
        FXUtils.observeWidthHeightCenter(stage.getInitialScene().getContentPane(), scene.getNode());
    }

    void addQuestionScene(VStage stage) {
        var questionScene = new VScene(VSceneRole.DRAWER_VERTICAL);
        questionScene.getNode().setPrefWidth(450);
        questionScene.enableAutoContentWidth();
        questionScene.getNode().setBackground(new Background(new BackgroundFill(
                Theme.current().subSceneBackgroundColor(),
                CornerRadii.EMPTY,
                Insets.EMPTY
        )));
        stage.getRootSceneGroup().addScene(questionScene, VSceneHideMethod.TO_LEFT);
        var questionBox = new VBox() {{
            setPadding(new Insets(0, 0, 0, 24));
            getChildren().add(new VPadding(20));
        }};
        questionScene.getContentPane().getChildren().add(questionBox);
        var inTro = new ThemeLabel("用户操作细则");
        inTro.setFont(Font.font(20));
        var tableHeader = new ThemeLabel("表格操作");
        var tableContent = new Label("1. 右键表格中的行，可以弹出操作菜单。\n" + "2. 也可以点击表格中的行，然后在菜单栏操作。\n" + "3. 鼠标悬浮在名称列可以便捷修改目录项名称\n" + "4. 点击表格中的列名，可以对表格进行相应排序展示。\n");
        var fileHeader = new ThemeLabel("文件相关");
        var fileContent = new Label("1. 合法目录与文件名仅可以使用字母、数字和除“$”、“.”、“/”以外 的字符。\n" + "2. 由于存储空间关系，名称只能有两个字符。\n" + "3.  由于存储空间关系，一个目录只能有最多八个目录项。\n");
        questionBox.getChildren().addAll(inTro, new VPadding(40), tableHeader, tableContent, new VPadding(40), fileHeader, fileContent);
        FXUtils.observeWidthCenter(questionScene.getContentPane(), questionBox);

    }
}
