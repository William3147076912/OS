package com.scau.cfd.controller;

import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.ui.pane.FusionPane;
import io.vproxy.vfx.ui.scene.VScene;
import io.vproxy.vfx.ui.scene.VSceneRole;
import io.vproxy.vfx.ui.wrapper.ThemeLabel;
import io.vproxy.vfx.util.FXUtils;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import lombok.Getter;

import java.util.Objects;

/**
 * 这个类是：文件内容界面控制类
 * @author: William
 * @date: 2024-11-18T19:37:25CST 19:37
 * @description:
 */
public class ContentController {
    @Getter
    private static final TextArea content = new TextArea() {{
        setPrefWidth(600);
        setPrefHeight(600);
    }};
    @Getter
    private static final VScene contentScene = initFileContentScene();

    private static VScene initFileContentScene() {
        VScene fileContentScene = new VScene(VSceneRole.POPUP) {{
            getNode().setPrefWidth(800);
            getNode().setPrefHeight(600);
        }};
        ThemeLabel titleLabel = new ThemeLabel("文件内容") {{
            setFont(Font.font(FontManager.FONT_NAME_JetBrainsMono, 50));
        }};
        titleLabel.setLayoutY(50);
        fileContentScene.getContentPane().getChildren().add(titleLabel);
        FXUtils.observeWidthCenter(fileContentScene.getContentPane(), titleLabel);
        FusionPane fusionPane = new FusionPane() {{
            getNode().setPrefWidth(650);
            getNode().setPrefHeight(650);
        }};
        fileContentScene.getContentPane().getChildren().add(fusionPane.getNode());
        FXUtils.observeWidthCenter(fileContentScene.getContentPane(), fusionPane.getNode());

        content.getStylesheets().add(Objects.requireNonNull(ContentController.class.getResource("/css/my_theme.css")).toExternalForm());
        fusionPane.getContentPane().getChildren().add(content);
        FXUtils.observeWidthHeightCenter(fusionPane.getContentPane(), content);
        return fileContentScene;
    }


}
