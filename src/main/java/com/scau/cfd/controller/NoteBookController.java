package com.scau.cfd.controller;

import com.scau.cfd.manage.FileManage;
import com.scau.cfd.manage.OurFile;
import io.vproxy.vfx.control.dialog.VDialog;
import io.vproxy.vfx.control.dialog.VDialogButton;
import io.vproxy.vfx.ui.alert.SimpleAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-18T22:08:12CST 22:08
 * @description:
 */

public class NoteBookController {

    @FXML
    @Getter
    private static TextArea ta;
    // 开始搜索的位置
    int startIndex = 0;
    // textarea中光标的位置
    int position = 0;
    @FXML
    private MenuItem FindMenu;
    @FXML
    private CheckMenuItem WrapMenu;
    @FXML
    private MenuItem ReplaceMenu;
    @FXML
    private MenuItem Redo;
    @FXML
    private MenuItem Undo;
    @Setter
    private String originalText;
    private File result;

    // 灰度控制
    public void initialize() {
        // 初始状态下不可使用查找与替换功能
        FindMenu.setDisable(true);
        ReplaceMenu.setDisable(true);
        // 初始状态不可使用撤销和重做功能
        Redo.setDisable(true);
        Undo.setDisable(true);


        // 对textarea的内容是否改变进行监听
        ta.textProperty().addListener((observable, oldValue, newValue) -> {
            // 如果textarea中内容部位空,则可以使用查找与替换
            if (ta.getLength() > 0) {
                FindMenu.setDisable(false);
                ReplaceMenu.setDisable(false);
            } // 否则禁用查找与替换
            else {
                FindMenu.setDisable(true);
                ReplaceMenu.setDisable(true);
            }
            Redo.setDisable(false);
            Undo.setDisable(false);
            // 光标位置
            position = ta.getCaretPosition();
        });
    }


    // 保存功能
    @FXML
    void onSaveMenu(ActionEvent event) {
        if (originalText.equals(ta.getText())) return;
        try {
            // 执行凯哥修改文件内容的方法
            OurFile selectedItem = (OurFile) MainController.getTableView().getSelectedItem();
            FileManage.WriteFileForGUI(selectedItem.getName(), ta.getText());
            SimpleAlert.showAndWait(Alert.AlertType.INFORMATION, "保存成功～(∠・ω< )⌒☆");
        } catch (IOException e) {
            SimpleAlert.showAndWait(Alert.AlertType.ERROR, "保存失败，请联系作者(´இ皿இ｀)");
        }
    }

    // 返回主界面
    @FXML
    void back(ActionEvent event) {
        if (originalText.equals(ta.getText())) {
            Stage stage = (Stage) ta.getScene().getWindow();
            stage.close();
        } else {
            var dialog = new VDialog<Integer>();
            dialog.setText("内容已被修改，是否要保存呢Owo?");
            dialog.setButtons(Arrays.asList(
                    new VDialogButton<>("好，朕已阅(ಡωಡ)", 1),
                    new VDialogButton<>("下次一定_(•̀ω•́ 」∠)_", 2)
            ));
            var result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.equals(Optional.of(1))) {
                    try {
                        // 执行凯哥修改文件内容的方法
                        OurFile selectedItem = (OurFile) MainController.getTableView().getSelectedItem();
                        FileManage.WriteFileForGUI(selectedItem.getName(), ta.getText());
                        SimpleAlert.showAndWait(Alert.AlertType.INFORMATION, "保存成功～(∠・ω< )⌒☆");
                    } catch (IOException e) {
                        SimpleAlert.showAndWait(Alert.AlertType.ERROR, "保存失败，请联系作者(´இ皿இ｀)");
                    }
                }
                Stage stage = (Stage) ta.getScene().getWindow();
                stage.close();
            }
        }
    }

    // 撤销
    @FXML
    void onUndoMenu(ActionEvent event) {
        ta.undo();
        Undo.setDisable(true);
    }

    // 重做
    @FXML
    void onRedoMenu(ActionEvent event) {
        ta.redo();
        Redo.setDisable(true);
    }

    // 查找功能
    @FXML
    void onFindMenu(ActionEvent event) throws IOException {
        HBox h1 = new HBox();
        h1.setPadding(new Insets(20, 5, 20, 5));
        h1.setSpacing(5);
        Label lable1 = new Label("查找内容(N):");
        TextField tf1 = new TextField();
        h1.getChildren().addAll(lable1, tf1);

        VBox v1 = new VBox();
        v1.setPadding(new Insets(20, 5, 20, 10));
        Button btn1 = new Button("查找下一个(F)");
        v1.getChildren().add(btn1);

        HBox findRootNode = new HBox();
        findRootNode.getChildren().addAll(h1, v1);

        Stage findStage = new Stage();
        Scene scene1 = new Scene(findRootNode, 450, 90);
        findStage.setTitle("查找");
        findStage.setScene(scene1);
        findStage.setResizable(false); // 固定窗口大小
        findStage.show();

        btn1.setOnAction((ActionEvent e) -> {
            String textString = ta.getText(); // 获取记事本文本域的字符串
            String tfString = tf1.getText(); // 获取要查找的字符串
            if (!tf1.getText().isEmpty()) {
                if (textString.contains(tfString)) {
                    // 查找方法
                    if (startIndex == -1) {// not found
                        Alert alert1 = new Alert(AlertType.WARNING);
                        alert1.titleProperty().set("提示");
                        alert1.headerTextProperty().set("已经找不到相关内容了！");
                        alert1.show();
                    }
                    startIndex = ta.getText().indexOf(tf1.getText(), startIndex);
                    if (startIndex >= 0 && startIndex < ta.getText().length()) {
                        ta.selectRange(startIndex, startIndex + tf1.getText().length());
                        startIndex += tf1.getText().length();
                    }
                }
                if (!textString.contains(tfString)) {
                    Alert alert1 = new Alert(AlertType.WARNING);
                    alert1.titleProperty().set("提示");
                    alert1.headerTextProperty().set("找不到相关内容！");
                    alert1.show();
                }
            } else if (tf1.getText().isEmpty()) {
                Alert alert1 = new Alert(AlertType.WARNING);
                alert1.titleProperty().set("出错了");
                alert1.headerTextProperty().set("输入内容为空");
                alert1.show();
            }
        });
    }

    // 替换功能
    @FXML
    void onReplaceMenu(ActionEvent event) {
        HBox h1 = new HBox();
        h1.setPadding(new Insets(20, 5, 10, 8));
        h1.setSpacing(5);
        Label label1 = new Label("查找下一个(F)");
        TextField tf1 = new TextField();
        h1.getChildren().addAll(label1, tf1);

        HBox h2 = new HBox();
        h2.setPadding(new Insets(5, 5, 20, 8));
        h2.setSpacing(5);
        Label label2 = new Label("替换内容(N):");
        TextField tf2 = new TextField();
        h2.getChildren().addAll(label2, tf2);

        VBox v1 = new VBox();
        v1.getChildren().addAll(h1, h2);

        VBox v2 = new VBox();
        v2.setPadding(new Insets(21, 5, 20, 10));
        v2.setSpacing(13);
        Button btn1 = new Button("查找下一个");
        Button btn2 = new Button("替换为");
        v2.getChildren().addAll(btn1, btn2);

        HBox replaceRootNode = new HBox();
        replaceRootNode.getChildren().addAll(v1, v2);

        Stage replaceStage = new Stage();
        Scene scene = new Scene(replaceRootNode, 430, 120);
        replaceStage.setTitle("替换");
        replaceStage.setScene(scene);
        replaceStage.setResizable(false); // 固定窗口大小
        replaceStage.show();

        btn1.setOnAction((ActionEvent e) -> {
            String textString = ta.getText(); // 获取记事本文本域的字符串
            String tfString = tf1.getText(); // 获取查找内容的字符串
            if (!tf1.getText().isEmpty()) {
                if (textString.contains(tfString)) {
                    if (startIndex == -1) {// not found
                        Alert alert1 = new Alert(AlertType.WARNING);
                        alert1.titleProperty().set("提示");
                        alert1.headerTextProperty().set("已经找不到相关内容了！");
                        alert1.show();
                    }
                    startIndex = ta.getText().indexOf(tf1.getText(), startIndex);
                    if (startIndex >= 0 && startIndex < ta.getText().length()) {
                        ta.selectRange(startIndex, startIndex + tf1.getText().length());
                        startIndex += tf1.getText().length();
                    }
                    btn2.setOnAction((ActionEvent e2) -> ta.replaceSelection(tf2.getText()));
                }
                if (!textString.contains(tfString)) {
                    Alert alert1 = new Alert(AlertType.WARNING);
                    alert1.titleProperty().set("提示");
                    alert1.headerTextProperty().set("找不到相关内容！");
                    alert1.show();
                }

            } else if (tf1.getText().isEmpty()) {
                Alert alert1 = new Alert(AlertType.WARNING);
                alert1.titleProperty().set("出错了");
                alert1.headerTextProperty().set("输入内容为空");
                alert1.show();
            }
        });
    }

    // 自动换行
    @FXML
    void onWrapMenu(ActionEvent event) {
        ta.setWrapText(WrapMenu.isSelected());
    }
}

