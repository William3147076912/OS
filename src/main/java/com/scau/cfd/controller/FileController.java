package com.scau.cfd.controller;

import com.leewyatt.rxcontrols.controls.RXAvatar;
import com.leewyatt.rxcontrols.controls.RXLineButton;
import com.leewyatt.rxcontrols.controls.RXTextField;
import com.leewyatt.rxcontrols.event.RXActionEvent;
import com.scau.cfd.OurFile;
import com.scau.cfd.utils.ConstantSet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public class FileController {

    @Setter
    @Getter
    // 0表示新建文件，1表示修改文件
    private static int newOrModify = 0;
    private final String[] fileAttributes = {"普通文件", "系统文件", "只读文件"};
    @FXML
    private RXLineButton cancel;
    @FXML
    private RadioButton r0;
    @FXML
    private RadioButton r1;
    @FXML
    private RadioButton r2;
    @FXML
    private RXAvatar image;
    @FXML
    private Label l0;
    @FXML
    private Label l1;
    @FXML
    private Label l2;
    @FXML
    private RXTextField nameField;
    @FXML
    private AnchorPane pane;
    @FXML
    private RXLineButton save;
    @FXML
    private RXTextField typeFIeld;

    @FXML
    void deleteText(RXActionEvent event) {// 文本框删除功能
        RXTextField tf = (RXTextField) event.getSource();
        tf.clear();
    }

    @FXML
    void setImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.gif")
        );
        Window window = image.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            // 如果用户选择了图片文件，则加载并显示在图片视图中
            image.setImage(new Image(selectedFile.toURI().toString()));
            System.out.println(image.getImage().getUrl() + "替换了界面图片");
//            //将image存到本地
//            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image.getImage(), null);
//            String suffix = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.') + 1);
//            File outputFile = new File("src/main/resources/images/" + nameField.getText() + "." + suffix);
//            try {
//                // Write the BufferedImage to the file
//                ImageIO.write(bufferedImage, suffix, outputFile);
//                System.out.println("Image saved successfully.");
//            } catch (IOException e) {
//                System.out.println("Error: " + e.getMessage());
//            }
        }
    }

    @FXML
    void save(MouseEvent event) {
        if (newOrModify == 0) {
            int sum = 0;
            if (r0.isSelected()) sum += ConstantSet.FILE;
            if (r1.isSelected()) sum += ConstantSet.SYSTEM_FILE;
            if (r2.isSelected()) sum += ConstantSet.READ_ONLY_FILE;
            new OurFile(nameField.getText(), typeFIeld.getText(), sum, 0, 0);
        } else if (newOrModify == 1) {

        } else {
            throw new RuntimeException("newOrModify的值有误");
        }
    }

    @FXML
    void cancel(MouseEvent event) {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
    }

    public void initialize() {

        // 初始化按钮和Label，给每个Label添加图标
        l0.setGraphic(new FontIcon());
        l1.setGraphic(new FontIcon());
        l2.setGraphic(new FontIcon());
        save.setGraphic(new FontIcon());
        cancel.setGraphic(new FontIcon());

        // 根据newOrModify的值来决定显示新建还是修改
        r0.setSelected(false);
        r1.setSelected(false);
        r2.setSelected(false);
        if (newOrModify == 0) {
            nameField.setText("");
            typeFIeld.setText("");
            r0.setSelected(true);

        } else if (newOrModify == 1) {
            nameField.setText(((OurFile) MainTestController.getTableView().getSelectedItem()).getName());
            typeFIeld.setText(((OurFile) MainTestController.getTableView().getSelectedItem()).getType());
            OurFile ourFile = (OurFile) MainTestController.getTableView().getSelectedItem();

            switch (ourFile.getAttribute()) {
                case ConstantSet.FILE -> r0.setSelected(true);
                case ConstantSet.SYSTEM_FILE -> r1.setSelected(true);
                case ConstantSet.READ_ONLY_FILE -> r2.setSelected(true);
                case ConstantSet.FILE + ConstantSet.READ_ONLY_FILE -> {
                    r0.setSelected(true);
                    r2.setSelected(true);
                }
                case ConstantSet.SYSTEM_FILE + ConstantSet.READ_ONLY_FILE -> {
                    r1.setSelected(true);
                    r2.setSelected(true);
                }
            }
        } else {
            throw new RuntimeException("newOrModify的值有误");
        }

    }
}

