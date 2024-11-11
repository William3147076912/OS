package com.scau.cfd.utils;

import io.vproxy.vfx.ui.button.FusionButton;

import io.vproxy.vfx.manager.font.FontManager;
import io.vproxy.vfx.manager.font.FontUsages;
import io.vproxy.vfx.theme.Theme;
import io.vproxy.vfx.util.FXUtils;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Window;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-05T12:17:07CST 12:17
 * @description:
 */
public class MyFusionButton extends FusionButton {
    private final Label text = new Label() {{
        setTextFill(Theme.current().fusionButtonTextColor());
        FontManager.get().setFont(FontUsages.fusionButtonText, this);
    }};
    private final ImageView icon = new ImageView(); // 新增的图标
    private final Pane borderLightPane = new Pane();
    private EventHandler<?> actionHandler = null;
    private Window watchingWindow = null;
    private Animation timer = null;
    private boolean disableAnimation = !Theme.current().enableFusionButtonAnimation();
    private boolean internalDisableAnimation = false;
    private boolean alreadyClicked = false;
    private boolean onlyAnimateWhenNotClicked = false;
    private final ChangeListener<? super Boolean> windowFocusPlayAnimationListener = (ob, old, now) -> {
        if (now == null) {
            return;
        }
        setInternalDisableAnimation(!now);
    };

    public MyFusionButton() {
        this(null);
    }

    public MyFusionButton(String text) {
        this.text.textProperty().addListener((ob, old, now) -> updateTextPosition());
        widthProperty().addListener((ob, old, now) -> {
            if (now == null) return;
            updateTextPosition();
            var w = now.doubleValue();
            borderLightPane.setPrefWidth(w);
        });
        heightProperty().addListener((ob, old, now) -> {
            if (now == null) return;
            updateTextPosition();
            var h = now.doubleValue();
            borderLightPane.setPrefHeight(h);
        });
        if (text != null) {
            this.text.setText(text);
        }
        setCursor(Cursor.HAND);

        // 创建一个 HBox 来容纳图标和文本
        HBox contentBox = new HBox(icon, this.text);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setSpacing(10); // 图标和文本之间的间距

        getChildren().add(contentBox);
        sceneProperty().addListener((ob, old, now) -> {
            var oldWindow = watchingWindow;
            watchingWindow = null;
            if (oldWindow != null) {
                oldWindow.focusedProperty().removeListener(windowFocusPlayAnimationListener);
            }
            if (now != null) {
                var newWindow = now.getWindow();
                watchingWindow = newWindow;
                newWindow.focusedProperty().addListener(windowFocusPlayAnimationListener);
                setInternalDisableAnimation(!newWindow.isFocused());
            } else {
                setInternalDisableAnimation(true);
            }
        });
        setInternalDisableAnimation(true);
        disabledProperty().addListener((ob, old, now) -> {
            if (now == null) return;
            handleDisable(now);
        });

        borderLightPane.setBorder(new Border(new BorderStroke(
                Theme.current().fusionButtonAnimatingBorderLightColor(),
                BorderStrokeStyle.SOLID,
                getCornerRadii(),
                new BorderWidths(1.5)
        )));
        borderLightPane.setBackground(Background.EMPTY);
        borderLightPane.setOpacity(0);
        getChildren().add(borderLightPane);

        setPrefWidth(64);
        setPrefHeight(24);
    }

    private void handleDisable(boolean v) {
        if (v) {
            setCursor(Cursor.DEFAULT);
            setMouseTransparent(true);
            text.setTextFill(Theme.current().fusionButtonDisabledTextColor());
        } else {
            setCursor(Cursor.HAND);
            setMouseTransparent(false);
            text.setTextFill(Theme.current().fusionButtonTextColor());
            startAnimating();
        }
    }

    private void updateTextPosition() {
        var bounds = FXUtils.calculateTextBounds(text);
        text.setLayoutX((getWidth() - bounds.getWidth()) / 2);
        text.setLayoutY((getHeight() - bounds.getHeight()) / 2);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public EventHandler<?> getOnAction() {
        return actionHandler;
    }

    public void setOnAction(EventHandler<?> handler) {
        this.actionHandler = handler;
    }

    @Override
    protected void onMouseClicked() {
        alreadyClicked = true;
        var actionHandler = this.actionHandler;
        if (actionHandler == null) {
            return;
        }
        actionHandler.handle(null);
    }

    @Override
    protected CornerRadii getCornerRadii() {
        return new CornerRadii(4);
    }

    public Label getTextNode() {
        return text;
    }

    public void setIcon(Image image) {
        this.icon.setImage(image);
        this.icon.setFitWidth(16); // 设置图标的宽度
        this.icon.setFitHeight(16); // 设置图标的高度
    }

    // return true if it's animating after calling this method
    public boolean startAnimating() {
        var timer = this.timer;
        if (timer != null) {
            return true; // is already animating
        }
        if (isDisableAnimation0()) {
            return false;
        }
        timer = new Animation();
        timer.start();
        this.timer = timer;
        return true;
    }

    public void stopAnimating() {
        var timer = this.timer;
        this.timer = null;
        if (timer != null) {
            timer.stop();
        }
        borderLightPane.setOpacity(0);
    }

    public boolean isDisableAnimation() {
        return disableAnimation;
    }

    public void setDisableAnimation(boolean disableAnimation) {
        this.disableAnimation = disableAnimation;
        if (disableAnimation) {
            stopAnimating();
        } else {
            startAnimating();
        }
    }

    public boolean isDisableAnimation0() {
        return disableAnimation || internalDisableAnimation || isDisabled() || (alreadyClicked && onlyAnimateWhenNotClicked);
    }

    public boolean isOnlyAnimateWhenNotClicked() {
        return onlyAnimateWhenNotClicked;
    }

    public void setOnlyAnimateWhenNotClicked(boolean onlyAnimateWhenNotClicked) {
        this.onlyAnimateWhenNotClicked = onlyAnimateWhenNotClicked;
    }

    private void setInternalDisableAnimation(boolean internalDisableAnimation) {
        this.internalDisableAnimation = internalDisableAnimation;
        if (internalDisableAnimation) {
            stopAnimating();
        } else {
            startAnimating();
        }
    }

    private class Animation extends AnimationTimer {
        private long beginTs = 0;

        @Override
        public void handle(long now) {
            if (beginTs == 0) {
                beginTs = now;
                return;
            }
            var delta = (now - beginTs) / 1_000_000;
            final long noAnimate = 2_000;
            final long showTime = 3_500;
            final long glowTime = 4_000;
            final long hideTime = 5_500;
            final long fullPeriod = 10_000;
            if (delta > fullPeriod) {
                while (delta > fullPeriod) {
                    delta -= fullPeriod;
                }
                beginTs = now - delta * 1_000_000;
            }
            if (delta < noAnimate) {
                borderLightPane.setOpacity(0);
            }
            if (delta < showTime) {
                var p = (delta - noAnimate) / (double) (showTime - noAnimate);
                borderLightPane.setOpacity(p);
            } else if (delta < glowTime) {
                borderLightPane.setOpacity(1);
            } else if (delta < hideTime) {
                var p = (delta - glowTime) / (double) (hideTime - glowTime);
                borderLightPane.setOpacity(1 - p);
            } else {
                borderLightPane.setOpacity(0);
                if (isDisableAnimation0()) {
                    timer = null;
                    stop();
                }
            }
        }
    }
}
