package com.scau.cfd.app;

import com.scau.cfd.controller.MainController;
import com.scau.cfd.manage.Catalog;
import com.scau.cfd.manage.CatalogManage;
import com.scau.cfd.manage.Disk;
import com.scau.cfd.utils.ImageUtils;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.ui.button.FusionImageButton;
import io.vproxy.vfx.ui.layout.VPadding;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneHideMethod;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.scene.VSceneShowMethod;
import io.vproxy.vfx.ui.stage.VStage;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

/**
 * 这个类是：
 *
 * @author: William
 * @date: 2024-10-07T21:01:44CST 21:01
 * @description:
 */
public class Main extends Application {
    public static VStage stage;
    public static Disk disk;
    byte[] buffer1;
    byte[] buffer2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 初始化磁盘
        disk = new Disk(new File("Disk"));
        // 打印磁盘容量
        // System.out.printf(String.valueOf(disk.getVolume()));
        // 格式化磁盘
        // disk.format();

        // 开机默认当前目录为主目录
        System.out.println("开机默认当前目录为主目录");
        Catalog man = new Catalog("/");
        man.setParent((byte) 2);
        man.setLocation((byte) 2);
        // man.attribute = 0x0B;
        CatalogManage.currentCatalog = man;
        //--------------------------------------

        stage = new VStage(primaryStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        AnchorPane root = fxmlLoader.load();
        addMainScene(root);

        addQuestionScene();
        stage.setTitle("Test");
        stage.getStage().setWidth(1200);
        stage.getStage().setHeight(1000);
        stage.show();
    }

    private void addMainScene(AnchorPane root) {
        // 由于tableview是自定义类，没继承control类，所以不能借助fxml初始化，只能自己手动初始化
        root.getChildren().add(MainController.getTableView().getNode());
        root.getChildren().add(MainController.getControlPane().getNode());
        stage.getSceneGroup().addScene(new VScene(VSceneRole.MAIN) {{
            getContentPane().getChildren().add(root);
        }});
        VScene scene = new VScene(VSceneRole.MAIN) {{
            getContentPane().getChildren().add(root);
        }};
        stage.getInitialScene().getContentPane().getChildren().add(scene.getNode());
        // stage.getRootSceneGroup().addScene(ContentController.getContentScene(), VSceneHideMethod.FADE_OUT);
        stage.getInitialScene().enableAutoContentWidthHeight();
        FXUtils.observeWidthHeightCenter(stage.getInitialScene().getContentPane(), scene.getNode());
    }

    private void addQuestionScene() {
        var questionScene = new VScene(VSceneRole.DRAWER_VERTICAL);
        questionScene.getNode().setPrefWidth(550);
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
        var inTro = new ThemeLabel("用户操作细则") {{
            setWrapText(true);
            setFont(Font.font(30));
        }};
        var tableHeader = new ThemeLabel("表格操作") {{
            setWrapText(true);
            setFont(Font.font(25));
        }};
        var tableContent = new ThemeLabel("1. 右键表格中的行，可以弹出操作菜单。\n" + "2. 也可以点击表格中的行，然后在菜单栏操作。\n" + "3. 鼠标悬浮在名称列可以便捷修改目录项名称\n" + "4. 点击表格中的列名，可以对表格进行相应排序展示。\n") {{
            setWrapText(true);
            setFont(Font.font(20));
        }};
        var fileHeader = new ThemeLabel("文件相关") {{
            setWrapText(true);
            setFont(Font.font(25));
        }};
        var fileContent = new ThemeLabel("1. 合法目录，文件名与文件类型名仅可以使用字母、数字和\n除“$”、“.”、“/”以外 的字符。\n" + "2. 由于存储空间关系，名称只能有两个字符。\n" + "3. 由于存储空间关系，一个目录只能有最多八个目录项。\n") {{
            setWrapText(true);
            setFont(Font.font(20));
        }};
        questionBox.getChildren().addAll(inTro, new VPadding(100), tableHeader, new VPadding(30), tableContent, new VPadding(100), fileHeader, new VPadding(20), fileContent);
        FXUtils.observeHeightCenter(questionScene.getContentPane(), questionBox);


        var questionBtn = new FusionImageButton(ImageUtils.getQuestionImage()) {{
            setPrefWidth(40);
            setPrefHeight(VStage.TITLE_BAR_HEIGHT + 1);
            getImageView().setFitHeight(15);
            setLayoutX(-2);
            setLayoutY(-1);
        }};
        questionBtn.setOnAction(e -> stage.getRootSceneGroup().show(questionScene, VSceneShowMethod.FROM_LEFT));
        stage.getRoot().getContentPane().getChildren().add(questionBtn);
    }


}
