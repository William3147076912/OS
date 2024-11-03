package com.scau.cfd.controller;

import com.leewyatt.rxcontrols.controls.RXLineButton;
import com.scau.cfd.Catalog;
import com.scau.cfd.OurFile;
import com.scau.cfd.utils.ConstantSet;
import com.scau.cfd.utils.TooltipUtil;
import io.vproxy.vfx.ui.table.VTableView;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.util.Duration;
import org.kordamp.ikonli.ionicons.Ionicons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 主控制器类，用于管理主界面的逻辑。
 *
 * @author: William
 * @date: 2024-10-06T00:09:31CST 00:09
 * @description:
 */
public class MainController {
    @FXML
    private RXLineButton back;
    @FXML
    private Label path;             // 路径输入框
    @FXML
    private Label name;                   // 名称标签
    @FXML
    private Label position;               // 位置标签
    @FXML
    private Label positionField;          // 位置显示区域
    @FXML
    private Label size;                   // 大小标签
    @FXML
    private Label sizeField;              // 大小显示区域
    @FXML
    private Label stats;                  // 属性标签
    @FXML
    private Label statsField;             // 属性显示区域
    @FXML
    private Label type;                   // 类型标签
    @FXML
    private Label typeField;              // 类型显示区域


    private TableView<Object> tableView;  // 表格视图

    @FXML
    private void initialize() {

        // back.setGraphic(new FontIcon());
        Image image1 = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/images/左指向标1.png")), 50, 50, false, false);
        Image image2 = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/images/左指向标2.png")), 50, 50, false, false);
        ImageView imageView = new ImageView(image1);
        back.setGraphic(imageView);
        // 添加鼠标悬浮事件
        back.setOnMouseEntered(event -> fadeTransition(imageView, image2).play());

        back.setOnMouseExited(event -> fadeTransition(imageView, image1).play());
        TooltipUtil.insertTooltip(back, "返回");
        // 初始化方法，用于设置界面控件的初始状态和行为
        path.setText(/* CatalogManage.currentCatalog.getName() */"/");
        // 为各个标签添加提示信息
        TooltipUtil.insertTooltip(name, name.getText());
        TooltipUtil.insertTooltip(position, position.getText());
        TooltipUtil.insertTooltip(positionField, positionField.getText());
        TooltipUtil.insertTooltip(size, size.getText());
        TooltipUtil.insertTooltip(sizeField, sizeField.getText());
        TooltipUtil.insertTooltip(stats, stats.getText());
        TooltipUtil.insertTooltip(statsField, statsField.getText());
        TooltipUtil.insertTooltip(type, type.getText());
        TooltipUtil.insertTooltip(typeField, typeField.getText());

        // 设置表格列的数据源
        TableColumn<Object, String> nameColumn = (TableColumn<Object, String>) tableView.getColumns().get(0);
        nameColumn.setCellValueFactory(param -> {
            if (param.getValue() instanceof Catalog) {
                return new SimpleStringProperty(((Catalog) param.getValue()).getName());
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(((OurFile) param.getValue()).getName());
            }
            return null;
        });

        TableColumn<Object, String> attributeColumn = (TableColumn<Object, String>) tableView.getColumns().get(1);
        attributeColumn.setCellValueFactory(param -> {
            if (param.getValue() instanceof Catalog) {
                return new SimpleStringProperty(ConstantSet.dirMap.get(((Catalog) param.getValue()).getAttribute()));
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(ConstantSet.dirMap.get(((OurFile) param.getValue()).getAttribute()));
            }
            return null;
        });

        TableColumn<Object, String> typeColumn = (TableColumn<Object, String>) tableView.getColumns().get(2);
        typeColumn.setCellValueFactory(param -> {
            if (param.getValue() instanceof Catalog) {
                return new SimpleStringProperty("文件夹");
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(((OurFile) param.getValue()).getType());
            }
            return null;
        });

        TableColumn<Object, String> lengthColumn = (TableColumn<Object, String>) tableView.getColumns().get(3);
        lengthColumn.setCellValueFactory(param -> {
            if (param.getValue() instanceof Catalog) {
                return new SimpleStringProperty("");
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(((OurFile) param.getValue()).getLength());
            }
            return null;
        });

        // 创建右键菜单
        ContextMenu contextMenu = new ContextMenu();
        VTableView<Object> vTableView = new VTableView<>();
        vTableView.setItems(tableView.getItems());

        // 设置表格点击事件
        tableView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) { // 检查是否是右键点击
                if (tableView.getSelectionModel().isEmpty()) {
                    // 点击的是空白行
                    name.setText("");
                    typeField.setText("");
                    statsField.setText("");
                    positionField.setText("");
                    sizeField.setText("");

                    contextMenu.getItems().clear();
                    // 创建菜单项
                    MenuItem mdItem = new MenuItem("建立目录");
                    MenuItem cfItem = new MenuItem("建立文件");
                    mdItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
                    cfItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
                    // 添加菜单项到右键菜单
                    contextMenu.getItems().addAll(mdItem, cfItem);
                    // 处理菜单项的点击事件
                    mdItem.setOnAction(this::handleMdAction);
                    cfItem.setOnAction(this::handleCfAction);
                } else {
                    Object selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (selectedItem instanceof Catalog catalog) {
                        name.setText(catalog.getName());
                        typeField.setText("文件夹");
                        statsField.setText(ConstantSet.dirMap.get(catalog.getAttribute()));
                        positionField.setText(catalog.getLocation());
                        sizeField.setText(String.valueOf(0));

                        contextMenu.getItems().clear();
                        // 创建菜单项
                        MenuItem dirItem = new MenuItem("显示目录内容");
                        MenuItem deleteItem = new MenuItem("删除空目录");
                        deleteItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
                        dirItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
                        // 添加菜单项到右键菜单
                        contextMenu.getItems().addAll(dirItem, deleteItem);
                        // 处理菜单项的点击事件
                        dirItem.setOnAction(this::handleDirAction);
                        deleteItem.setOnAction(this::handleDeleteAction);
                    } else if (selectedItem instanceof OurFile file) {
                        name.setText(file.getName());
                        typeField.setText(file.getType());
                        statsField.setText(ConstantSet.dirMap.get(file.getAttribute()));
                        positionField.setText(file.getNumber());
                        sizeField.setText(file.getLength());

                        contextMenu.getItems().clear();
                        // 创建菜单项
                        MenuItem openItem = new MenuItem("打开文件");
                        MenuItem deleteItem = new MenuItem("删除文件");
                        openItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
                        deleteItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
                        // 添加菜单项到右键菜单
                        contextMenu.getItems().addAll(openItem, deleteItem);
                        // 处理菜单项的点击事件
                        openItem.setOnAction(this::handleOpenAction);
                        deleteItem.setOnAction(this::handleDeleteAction);
                    }
                    // 清空被选中的行
                    tableView.getSelectionModel().clearSelection();
                }
                contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
            }
        });

        // 设置右键菜单的触发条件
        tableView.setContextMenu(contextMenu);


        // 添加测试数据
        List<Object> data = Arrays.asList(
                new Catalog("001", 23, 0),
                new OurFile("william", "233", 33, 23, 444)
        );
        tableView.getItems().addAll(data);

        // 应用样式
        contextMenu.getStyleClass().add(Objects.requireNonNull(MainController.this.getClass().getResource("/css/context_menu.css")).toExternalForm());
    }


    private FadeTransition fadeTransition(ImageView imageView, Image toImage) {
        // 创建FadeTransition
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), imageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), imageView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // 当淡出结束后，切换图像
        fadeOut.setOnFinished(event -> imageView.setImage(toImage));

        // 连接两个过渡动画
        fadeOut.play();
        fadeIn.play();

        return fadeOut;
    }

    private void handleCfAction(ActionEvent actionEvent) {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tableView.getItems().remove(selectedItem);
        }
    }

    private void handleDeleteAction(ActionEvent actionEvent) {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tableView.getItems().remove(selectedItem);
        }
    }

    private void handleOpenAction(ActionEvent event) {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem instanceof Catalog catalog) {
            System.out.println("打开文件夹: " + catalog.getName());
        } else if (selectedItem instanceof OurFile file) {
            System.out.println("打开文件: " + file.getName());
        }
    }

    private void handleMdAction(ActionEvent event) {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            tableView.getItems().remove(selectedItem);
        }
    }

    private void handleDirAction(ActionEvent event) {
        Object selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem instanceof Catalog catalog) {
            System.out.println("重命名文件夹: " + catalog.getName());
        } else if (selectedItem instanceof OurFile file) {
            System.out.println("重命名文件: " + file.getName());
        }
    }


}
