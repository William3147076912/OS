package com.scau.cfd.controller;

import com.leewyatt.rxcontrols.controls.RXAvatar;
import com.leewyatt.rxcontrols.controls.RXLineButton;
import com.leewyatt.rxcontrols.controls.RXTextField;
import com.leewyatt.rxcontrols.event.RXActionEvent;
import com.scau.cfd.manage.FileManage;
import com.scau.cfd.manage.OurFile;
import com.scau.cfd.utils.ConstantSet;
import com.scau.cfd.utils.StringUtils;
import io.vproxy.vfx.ui.alert.SimpleAlert;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

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
        // FileChooser fileChooser = new FileChooser();
        // fileChooser.setTitle("选择图片");
        // fileChooser.getExtensionFilters().addAll(
        //         new FileChooser.ExtensionFilter("图片文件", "*.png", "*.jpg", "*.gif")
        // );
        // Window window = image.getScene().getWindow();
        // File selectedFile = fileChooser.showOpenDialog(window);
        // if (selectedFile != null) {
        //     // 如果用户选择了图片文件，则加载并显示在图片视图中
        //     image.setImage(new Image(selectedFile.toURI().toString()));
        //     System.out.println(image.getImage().getUrl() + "替换了界面图片");
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
//         }
    }

    @FXML
    void save(MouseEvent event) {
        // 检验文件名和文件类型合法性
        if (!StringUtils.isValidName(nameField.getText()) || !StringUtils.isValidType(typeFIeld.getText())) {
            SimpleAlert.show(Alert.AlertType.ERROR, "文件名或文件类型不合法，请重新输入(´▽`ʃ♡ƪ)");
            return;
        }
        if (StringUtils.isNameExists(nameField.getText(), OurFile.class)) {
            SimpleAlert.show(Alert.AlertType.ERROR, "文件名已存在，请重新输入(´▽`ʃ♡ƪ)");
            return;
        }
        byte sum = 0;
        if (r0.isSelected()) sum += ConstantSet.FILE;
        if (r1.isSelected()) sum += ConstantSet.SYSTEM_FILE;
        if (r2.isSelected()) sum += ConstantSet.READ_ONLY_FILE;
        if (sum >= ConstantSet.FILE + ConstantSet.SYSTEM_FILE) {
            SimpleAlert.show(Alert.AlertType.ERROR, "文件类型不能同时为系统文件和只读文件，请重新选择(´▽`ʃ♡ƪ)");
            return;
        }
        if (newOrModify == 0) {
            if (MainController.getTableView().getItems().size() >= 8) {
                SimpleAlert.show(Alert.AlertType.ERROR, "最多只能创建8个目录项，请删除后再试！(≧∇≦)ﾉ");
                return;
            }
            try {
                FileManage.CreateFile(nameField.getText(), typeFIeld.getText(), sum);
                MainController.refreshTable();
                SimpleAlert.showAndWait(Alert.AlertType.INFORMATION, "创建成功～(∠・ω< )⌒☆");
                cancel(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // MainTestController.getTableView().getItems().add(new OurFile(nameField.getText(), typeFIeld.getText(), sum, 0, 0));
        } else if (newOrModify == 1) {
            var file = (OurFile) MainController.getTableView().getSelectedItem();
            // int index = MainController.getTableView().getItems().indexOf(file);
            // MainController.getTableView().getItems().remove(file);
            // MainController.getTableView().getItems().add(index, file);
            try {
                // FileManage.DeleteFile(file.getName());
                // FileManage.CreateFile(nameField.getText(), typeFIeld.getText(), sum);
                FileManage.ChangeAttribute(file.getName(), sum);
                FileManage.ChangeName(file.getName(), nameField.getText());
                MainController.refreshTable();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SimpleAlert.showAndWait(Alert.AlertType.INFORMATION, "修改成功～(∠・ω< )⌒☆");
            cancel(event);
        } else {
            SimpleAlert.show(Alert.AlertType.ERROR, "newOrModify的值有误,请联系作者w(ﾟДﾟ)w");
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
            nameField.setText(((OurFile) MainController.getTableView().getSelectedItem()).getName());
            typeFIeld.setText(((OurFile) MainController.getTableView().getSelectedItem()).getType());
            OurFile ourFile = (OurFile) MainController.getTableView().getSelectedItem();

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
            SimpleAlert.show(Alert.AlertType.ERROR, "newOrModify的值有误,请联系作者w(ﾟДﾟ)w");
        }

    }
}

