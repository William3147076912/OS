package com.scau.cfd.controller;

import java.io.File;
import java.io.IOException;

import com.scau.cfd.utils.FileTools;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-18T22:08:12CST 22:08
 * @description:
 */

public class NoteBookController {

    // 开始搜索的位置
    int startIndex = 0;
    // textarea中光标的位置
    int position = 0;
    @FXML
    private MenuItem SaveMenu;
    @FXML
    private MenuItem FindMenu;
    @FXML
    private CheckMenuItem WrapMenu;
    @FXML
    private AnchorPane layoutPane;
    @FXML
    private MenuItem ReplaceMenu;
    @FXML
    private CheckMenuItem StateMenu;
    @FXML
    private MenuItem OpenMenu;
    @FXML
    private MenuItem TypefaceMenu;
    @FXML
    private MenuItem NewMenu;
    @FXML
    private TextArea ta;
    @FXML
    private Label label;
    @FXML
    private MenuItem Redo;
    @FXML
    private MenuItem Undo;
    private File result;

    // 灰度控制
    public void initialize() {
        // 初始状态下不可使用查找与替换功能
        FindMenu.setDisable(true);
        ReplaceMenu.setDisable(true);
        // 初始状态不可使用撤销和重做功能
        Redo.setDisable(true);
        Undo.setDisable(true);

        // 状态栏不可视
        label.setVisible(false);

        ta.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                position = ta.getCaretPosition();
                label.setText("第" + position + "个字符");
            }
        });

        // 对textarea的内容是否改变进行监听
        ta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // 如果testarea中内容部位空,则可以使用查找与替换
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
                label.setText("第" + position + "个字符");
            }
        });
    }

    // 修改前保
    void saveadvance() {
        if (result != null && ta.getLength() > 0) {

            FileTools.writeFile(result, ta.getText());
        } else if (result == null && ta.getLength() > 0) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setTitle("保存当前内容");
            result = fileChooser.showSaveDialog(null);
            if (result != null) {
                FileTools.writeFile(result, ta.getText());
            }
        }
    }

    // 新建功能
    @FXML
    void onNewMenu(ActionEvent event) {

        // 新建前保存
        saveadvance();

        ta.clear();
        result = null;
    }

    // 打开功能
    @FXML
    void onOpenMenu(ActionEvent event) {

        // 打开前保存
        saveadvance();

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        result = fileChooser.showOpenDialog(null);
        if (result != null) {
            ta.setText(FileTools.readFile(result));
        }
    }

    // 保存功能
    @FXML
    void onSaveMenu(ActionEvent event) throws IOException {
        if (result != null)// 如果已经存在保存路径
        {
            FileTools.writeFile(result, ta.getText());
        } else// 如果不存在保存的路径
        {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            result = fileChooser.showSaveDialog(null);
            if (result != null) {
                FileTools.writeFile(result, ta.getText());
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
    void onReplaceMenu(ActionEvent event) throws IOException {
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
                    btn2.setOnAction((ActionEvent e2) -> {
                        ta.replaceSelection(tf2.getText());
                    });
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
        if (WrapMenu.isSelected())
            ta.setWrapText(true);
        else
            ta.setWrapText(false);
    }

    // 字体
    @FXML
    void onTypefaceMenu(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Typeface.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        // 等待窗口被关闭在做出下一步反应
        stage.showAndWait();
        if (scene.getUserData() != null)// 如果用户有设定字体的样式,则将记事本中的字体改为该样式
        {
            Font font = (Font) scene.getUserData();
            ta.setFont(font);
        }
    }

    // 状态栏
    @FXML
    void onStateMenu(ActionEvent event) {
        if (StateMenu.isSelected())
            label.setVisible(true);
        else
            label.setVisible(false);
    }

}

