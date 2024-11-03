package com.scau.cfd.william_test;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-03T18:47:50CST 18:47
 * @description:
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectComboBox extends Application {

    private final List<String> options = List.of("选项 1", "选项 2", "选项 3", "选项 4");
    private final List<String> selectedOptions = new ArrayList<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.setEditable(false);

        // 自定义下拉列表
        ComboBoxPopup comboBoxPopup = new ComboBoxPopup(comboBox, options, selectedOptions);
        comboBox.setOnMouseClicked(e -> comboBoxPopup.show());

        // 显示所选项
        Button button = new Button("确认选择");
        button.setOnAction(e -> {
            System.out.println("选择的项: " + selectedOptions);
        });

        VBox vbox = new VBox(comboBox, button);
        Scene scene = new Scene(vbox, 300, 250);

        primaryStage.setTitle("多选 ComboBox 示例");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 自定义下拉框
    private class ComboBoxPopup {
        private final ComboBox<String> comboBox;
        private final List<String> options;
        private final List<String> selectedOptions;

        public ComboBoxPopup(ComboBox<String> comboBox, List<String> options, List<String> selectedOptions) {
            this.comboBox = comboBox;
            this.options = options;
            this.selectedOptions = selectedOptions;
        }

        public void show() {
            Stage popupStage = new Stage();
            VBox vbox = new VBox();

            for (String option : options) {
                CheckBox checkBox = new CheckBox(option);
                checkBox.setSelected(selectedOptions.contains(option));
                checkBox.setOnAction(e -> {
                    if (checkBox.isSelected()) {
                        selectedOptions.add(option);
                    } else {
                        selectedOptions.remove(option);
                    }
                    updateComboBox();
                });
                vbox.getChildren().add(checkBox);
            }

            Scene scene = new Scene(vbox);
            popupStage.setScene(scene);
            popupStage.initOwner(comboBox.getScene().getWindow());
            popupStage.show();
        }

        private void updateComboBox() {
            comboBox.getItems().clear();
            comboBox.getItems().add(String.join(", ", selectedOptions));
        }
    }
}
