package com.scau.cfd.controller;

import com.leewyatt.rxcontrols.controls.RXTextField;
import com.scau.cfd.Catalog;
import com.scau.cfd.OurFile;
import com.scau.cfd.utils.TooltipUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;
import java.util.List;

/**
 * 主控制器类，用于管理主界面的逻辑。
 *
 * @author: William
 * @date: 2024-10-06T00:09:31CST 00:09
 * @description:
 */
public class MainController {
    @FXML
    private RXTextField search;           // 搜索框
    @FXML
    private RXTextField path;             // 路径输入框
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

    @FXML
    private TableView<Object> tableView;  // 表格视图

    @FXML
    private void initialize() {
        // 初始化方法，用于设置界面控件的初始状态和行为

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
                return new SimpleStringProperty(Arrays.toString(((Catalog) param.getValue()).getName()));
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(Arrays.toString(((OurFile) param.getValue()).getFilename()));
            }
            return null;
        });

        TableColumn<Object, String> attributeColumn = (TableColumn<Object, String>) tableView.getColumns().get(1);
        attributeColumn.setCellValueFactory(param -> {
            if (param.getValue() instanceof Catalog) {
                return new SimpleStringProperty(String.valueOf(((Catalog) param.getValue()).getAttribute()));
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(String.valueOf(((OurFile) param.getValue()).getAttribute()));
            }
            return null;
        });

        TableColumn<Object, String> typeColumn = (TableColumn<Object, String>) tableView.getColumns().get(2);
        typeColumn.setCellValueFactory(param -> {
            if (param.getValue() instanceof Catalog) {
                return new SimpleStringProperty("文件夹");
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(Arrays.toString(((OurFile) param.getValue()).getType()));
            }
            return null;
        });

        TableColumn<Object, String> lengthColumn = (TableColumn<Object, String>) tableView.getColumns().get(3);
        lengthColumn.setCellValueFactory(param -> {
            if (param.getValue() instanceof Catalog) {
                return new SimpleStringProperty("");
            } else if (param.getValue() instanceof OurFile) {
                return new SimpleStringProperty(String.valueOf(((OurFile) param.getValue()).getLength()));
            }
            return null;
        });

        // 设置表格点击事件
        tableView.setOnMouseClicked(event -> {
            Object selectedPerson = tableView.getSelectionModel().getSelectedItem();
            if (selectedPerson instanceof Catalog catalog) {
                typeField.setText("文件夹");
                statsField.setText(String.valueOf(catalog.getAttribute()));
                positionField.setText(String.valueOf(catalog.getLocation()));
                sizeField.setText(String.valueOf(0));
            } else if (selectedPerson instanceof OurFile file) {
                typeField.setText(Arrays.toString(file.getType()));
                statsField.setText(String.valueOf(file.getAttribute()));
                positionField.setText(String.valueOf(file.getNumber()));
                sizeField.setText(String.valueOf(file.getLength()));
            } else {
                throw new RuntimeException("列表出现意料之外的对象");
            }
        });

        // 添加测试数据
        List<Object> data = Arrays.asList(
                new Catalog("000".getBytes(), Byte.parseByte("123"), Byte.parseByte("2"), Byte.parseByte("3")),
                new Catalog("010".getBytes(), Byte.parseByte("123"), Byte.parseByte("2"), Byte.parseByte("3")),
                new OurFile("100".getBytes(), "123".getBytes(), Byte.parseByte("2"), 2, 100),
                new Catalog("100".getBytes(), Byte.parseByte("123"), Byte.parseByte("2"), Byte.parseByte("3"))
        );
        tableView.getItems().addAll(data);
    }
}
