package com.scau.cfd.controller;

import com.leewyatt.rxcontrols.controls.RXLineButton;
import com.scau.cfd.Catalog;
import com.scau.cfd.OurFile;
import com.scau.cfd.utils.ConstantSet;
import com.scau.cfd.utils.ImageUtils;
import com.scau.cfd.utils.PopupScene;
import com.scau.cfd.utils.StringUtils;
import com.scau.cfd.utils.TooltipUtil;
import com.scau.cfd.william_test.Test;
import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.manager.font.FontUsages;
import io.vproxy.vfx.ui.alert.SimpleAlert;
import io.vproxy.vfx.ui.button.FusionButton;
import io.vproxy.vfx.ui.layout.HPadding;
import io.vproxy.vfx.ui.pane.FusionPane;
import io.vproxy.vfx.ui.scene.VSceneShowMethod;
import io.vproxy.vfx.ui.table.VTableColumn;
import io.vproxy.vfx.ui.table.VTableView;
import io.vproxy.vfx.ui.wrapper.FusionW;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import javafx.animation.FadeTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;
import org.kordamp.ikonli.ionicons.Ionicons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 主控制器类，用于管理主界面的逻辑。
 *
 * @author: William
 * @date: 2024-10-06T00:09:31CST 00:09
 * @description:
 */
public class MainTestController {
    private static final double ROW_HEIGHT = ConstantSet.ROW_HEIGHT;
    private static final VTableView<Object> tableView = new VTableView<>() {{
        getNode().setPrefWidth(700);
        getNode().setPrefHeight(700);
        getNode().setLayoutY(100);
    }};  // 表格视图
    @Getter
    private static final FusionPane // 菜单栏
            controlPane = new FusionPane(false) {{
        getNode().setLayoutY(50);
        getNode().setPrefHeight(50);
    }};
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

    public static VTableView getTableView() {
        return tableView;
    }


    public static boolean isNameExists(String name, Class<?> clazz) {
        for (Object item : MainTestController.getTableView().getItems()) {
            if (clazz.isInstance(item)) {
                String itemName = clazz.cast(item) instanceof OurFile ? ((OurFile) item).getName() : ((Catalog) item).getName();
                if (itemName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }


    @FXML
    private void initialize() {

        // back.setGraphic(new FontIcon());
        // Image image1 = new Image(Objects.requireNonNull(
        //         getClass().getResourceAsStream("/images/左指向标1.png")), 50, 50, false, false);
        // Image image2 = new Image(Objects.requireNonNull(
        //         getClass().getResourceAsStream("/images/左指向标2.png")), 50, 50, false, false);
        // ImageView imageView = new ImageView(image1);
        // back.setGraphic(imageView);
        // 添加鼠标悬浮事件
        // back.setOnMouseEntered(event -> fadeTransition(imageView, image2).play());
        //
        // back.setOnMouseExited(event -> fadeTransition(imageView, image1).play());
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
        VTableColumn<Object, Object> iconColumn = new VTableColumn<>("图标", new ThemeLabel("图标") {{
            setPrefHeight(50);
        }}, data -> data);
        iconColumn.setPrefWidth(100);
        iconColumn.setNodeBuilder(data -> {
            Label textNode = new Label();
            if (data instanceof OurFile) {
                textNode.setGraphic(ImageUtils.getFileImage());
            } else {
                textNode.setGraphic(ImageUtils.getDirImage());
            }
            return textNode;
        });
        VTableColumn<Object, Object> nameColumn = new VTableColumn<>("名称", new ThemeLabel("名称") {{
            setPrefHeight(50);
        }}, data -> data);
        nameColumn.setPrefWidth(250);
        nameColumn.setComparator(Comparator.comparing(data -> data instanceof OurFile ? ((OurFile) data).getName() : ((Catalog) data).getName()));
        nameColumn.setNodeBuilder(data -> {
            var textField = new TextField() {{
                setPrefHeight(50);
            }};
            var text = new FusionW(textField) {{
                setPrefHeight(50);
                FontManager.get().setFont(FontUsages.tableCellText, getLabel());
            }};
            textField.setText(data instanceof OurFile ? ((OurFile) data).getName() : ((Catalog) data).getName());
            textField.setOnMouseExited(event -> {
                if (!textField.getText().equals(data instanceof OurFile ? ((OurFile) data).getName() : ((Catalog) data).getName())) {
                    // 如果有做过更改才去判断名称合法性
                    if (textField.getText().isEmpty()) {
                        SimpleAlert.show(Alert.AlertType.ERROR, "名称不能为空(￣△￣；)");
                        textField.setText(data instanceof OurFile ? ((OurFile) data).getName() : ((Catalog) data).getName());
                    } else if (isNameExists(textField.getText(), data.getClass())) {
                        SimpleAlert.show(Alert.AlertType.ERROR, "名称已存在(눈‸눈)");
                        textField.setText(data instanceof OurFile ? ((OurFile) data).getName() : ((Catalog) data).getName());
                    } else if (!StringUtils.isValidName(textField.getText())) {
                        SimpleAlert.show(Alert.AlertType.ERROR, "名称不合法(งᵒ̌皿ᵒ̌)ง⁼³₌₃");
                        textField.setText(data instanceof OurFile ? ((OurFile) data).getName() : ((Catalog) data).getName());
                    } else {
                        if (data instanceof OurFile) {
                            ((OurFile) data).setName(textField.getText());
                        } else {
                            ((Catalog) data).setName(textField.getText());
                        }
                    }
                }
                tableView.getNode().requestFocus();
            });
            // textField.textProperty().addListener((ob, old, now) -> {
            //     textField.setText(sanitizeName(textField.getText()));
            //     if (data instanceof OurFile) ((OurFile) data).setName(textField.getText());
            //     else ((Catalog) data).setName(textField.getText());
            // });
            return text;
        });
        VTableColumn<Object, String> attributeColumn = new VTableColumn<>("属性", new ThemeLabel("属性") {{
            setPrefHeight(50);
        }}, data -> {
            if (data instanceof OurFile) {
                return ConstantSet.dirMap.get(((OurFile) data).getAttribute());
            } else {
                return ConstantSet.dirMap.get(((Catalog) data).getAttribute());
            }
        });
        attributeColumn.setPrefWidth(150);
        attributeColumn.setComparator(String::compareToIgnoreCase);
        VTableColumn<Object, String> typeColumn = new VTableColumn<>("类型", new ThemeLabel("类型") {{
            setPrefHeight(50);
        }}, data -> {
            if (data instanceof OurFile) {
                return ((OurFile) data).getType();
            } else {
                return "文件夹";
            }
        });

        typeColumn.setPrefWidth(150);
        typeColumn.setComparator(String::compareToIgnoreCase);
        VTableColumn<Object, Integer> lengthColumn = new VTableColumn<>("大小", new ThemeLabel("大小") {{
            setPrefHeight(50);
        }}, data -> {
            if (data instanceof OurFile) {
                return Integer.parseInt(((OurFile) data).getLength());
            } else {
                return 0;
            }
        });
        lengthColumn.setPrefWidth(150);
        lengthColumn.setComparator(Integer::compareTo);
        tableView.getColumns().addAll(iconColumn, nameColumn, attributeColumn, typeColumn, lengthColumn);
        // 创建右键菜单
        ContextMenu fileContextMenu = new ContextMenu();
        ContextMenu categoryContextMenu = new ContextMenu();
        ContextMenu emptyContextMenu = new ContextMenu();
        // 创建菜单项
        MenuItem mdItem = new MenuItem("建立目录");
        MenuItem mfItem = new MenuItem("建立文件");
        MenuItem openItem = new MenuItem("打开文件");
        MenuItem delFileItem = new MenuItem("删除文件");
        MenuItem modifyFileItem = new MenuItem("修改文件");
        MenuItem dirItem = new MenuItem("显示目录内容");
        MenuItem delDirItem = new MenuItem("删除空目录");
        mdItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
        mfItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
        openItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
        delFileItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
        modifyFileItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
        dirItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
        delDirItem.setGraphic(new FontIcon(Ionicons.ION_SOCIAL_OCTOCAT));
        // 处理菜单项的点击事件
        mdItem.setOnAction(this::handleMdAction);
        mfItem.setOnAction(this::handleCfAction);
        openItem.setOnAction(this::handleOpenAction);
        delFileItem.setOnAction(this::handleDeleteAction);
        modifyFileItem.setOnAction(this::handleModifyAction);
        dirItem.setOnAction(this::handleDirAction);
        delDirItem.setOnAction(this::handleDeleteAction);
        // 添加菜单项到右键菜单
        emptyContextMenu.getItems().addAll(mdItem, mfItem);
        fileContextMenu.getItems().addAll(openItem, delFileItem, modifyFileItem);
        categoryContextMenu.getItems().addAll(dirItem, delDirItem);


        // 设置表格点击事件
        tableView.getNode().setOnMouseClicked(event -> {
            double y = event.getY();
            // System.out.println(y);
            if (y > ROW_HEIGHT * (tableView.getItems().size() + 1)) {
                // 点击的是空白行
                name.setText("");
                typeField.setText("");
                statsField.setText("");
                positionField.setText("");
                sizeField.setText("");
                fileContextMenu.hide();
                categoryContextMenu.hide();
                if (event.getButton() == MouseButton.SECONDARY) {
                    emptyContextMenu.show(tableView.getNode(), event.getScreenX(), event.getScreenY());
                } else {
                    emptyContextMenu.hide();
                }
            } else if (y > ConstantSet.TABLE_HEAD_HEIGHT) {
                Object selectedItem = tableView.getSelectedItem();
                if (selectedItem instanceof OurFile ourFile) {
                    name.setText(ourFile.getName());
                    typeField.setText(ourFile.getType());
                    statsField.setText(ConstantSet.dirMap.get(ourFile.getAttribute()));
                    positionField.setText(ourFile.getNumber());
                    sizeField.setText(ourFile.getLength());
                    fileContextMenu.hide();
                    categoryContextMenu.hide();
                    emptyContextMenu.hide();
                    if (event.getButton() == MouseButton.SECONDARY) {
                        fileContextMenu.show(tableView.getNode(), event.getScreenX(), event.getScreenY());
                    } else {
                        fileContextMenu.hide();
                    }
                } else if (selectedItem instanceof Catalog catalog) {
                    name.setText(catalog.getName());
                    typeField.setText("文件夹");
                    statsField.setText(ConstantSet.dirMap.get(catalog.getAttribute()));
                    positionField.setText(catalog.getLocation());
                    sizeField.setText(String.valueOf(0));
                    fileContextMenu.hide();
                    categoryContextMenu.hide();
                    emptyContextMenu.hide();
                    if (event.getButton() == MouseButton.SECONDARY) {
                        categoryContextMenu.show(tableView.getNode(), event.getScreenX(), event.getScreenY());
                    } else {
                        categoryContextMenu.hide();
                    }
                }
            }
        });

        // 添加测试数据
        List<Object> data = Arrays.asList(
                new Catalog("01", ConstantSet.CATEGORY, 0),
                new Catalog("02", ConstantSet.CATEGORY, 0),
                new Catalog("03", ConstantSet.CATEGORY, 0),
                new Catalog("04", ConstantSet.CATEGORY, 0),
                new Catalog("05", ConstantSet.CATEGORY, 0),
                new OurFile("wi", "233", ConstantSet.FILE + ConstantSet.READ_ONLY_FILE, 23, 444)
        );
        tableView.getItems().addAll(data);


        HBox controlBox = new HBox();
        controlBox.getChildren().addAll(
                new FusionButton("新建文件") {{
                    setOnAction(MainTestController.this::handleCfAction);
                    setPrefWidth(120);
                    setPrefHeight(30);
                }},
                new HPadding(30),
                new FusionButton("新建目录") {{
                    setOnAction(MainTestController.this::handleMdAction);
                    setPrefWidth(120);
                    setPrefHeight(30);
                }},
                new HPadding(30),
                new FusionButton("删除") {{
                    setOnAction(MainTestController.this::handleDeleteAction);
                    setPrefWidth(120);
                    setPrefHeight(30);
                }},
                new HPadding(30),
                new FusionButton("修改") {{
                    setOnAction(MainTestController.this::handleModifyAction);
                    setPrefWidth(120);
                    setPrefHeight(30);
                }}
        );
        controlPane.getContentPane().getChildren().add(controlBox);
        // 应用样式
        fileContextMenu.getStyleClass().add(Objects.requireNonNull(MainTestController.this.getClass().getResource("/css/context_menu.css")).toExternalForm());
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

    private void showFileSetting() {
        Scene scene;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainTestController.class.getResource("/fxml/file.fxml"));
            scene = new Scene(fxmlLoader.load());
            PopupScene.fadeTransition(scene);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        scene.setFill(Color.TRANSPARENT);// 舞台透明
        new Stage() {{
            setScene(scene);
            initStyle(StageStyle.TRANSPARENT);// 窗口透明
            initModality(Modality.APPLICATION_MODAL);
            show();
        }};
    }

    private void handleModifyAction(Event actionEvent) {
        // 设置弹出窗口模式为修改文件模式
        if (MainTestController.getTableView().getSelectedItem() instanceof Catalog) {
            SimpleAlert.show(Alert.AlertType.ERROR, "目录并无修改选项，如果想修改目录名，请直接在表格目录名处修改即可（￣︶￣）↗　");
        } else {
            FileController.setNewOrModify(1);
            showFileSetting();
        }
    }

    private void handleCfAction(Event actionEvent) {
        // 设置弹出窗口模式为新建文件模式
        FileController.setNewOrModify(0);
        showFileSetting();
    }

    private void handleDeleteAction(Event actionEvent) {
        Object selectedItem = tableView.getSelectedItem();
        if (selectedItem != null) {
            tableView.getItems().remove(selectedItem);
        }
        // 还要删除对应后端数据
    }

    private void handleOpenAction(Event event) {
        Object selectedItem = tableView.getSelectedItem();
        if (selectedItem instanceof Catalog catalog) {
            System.out.println("打开文件夹: " + catalog.getName());
        } else if (selectedItem instanceof OurFile file) {
            System.out.println("打开文件: " + file.getName());
            Test.stage.getRootSceneGroup().show(ContentController.getContentScene(), VSceneShowMethod.FROM_BOTTOM);
        }
    }

    private void handleMdAction(Event event) {
        if (tableView.getItems().size() >= 8) {
            SimpleAlert.show(Alert.AlertType.ERROR, "最多只能创建8个目录项，请删除后再试！(≧∇≦)ﾉ");
            return;
        }
        tableView.getItems().add(new Catalog(StringUtils.randomString(2), ConstantSet.CATEGORY, 0));
    }

    private void handleDirAction(Event event) {
        Object selectedItem = tableView.getSelectedItem();
        if (selectedItem instanceof Catalog catalog) {
            System.out.println("重命名文件夹: " + catalog.getName());
        } else if (selectedItem instanceof OurFile file) {
            System.out.println("重命名文件: " + file.getName());
        }
    }

}