package com.scau.cfd.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.Random;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-03T21:08:36CST 21:08
 * @description:
 */
public class ImageUtils {
    private static final Image fileImage = new Image(Objects.requireNonNull(ImageUtils.class.getResource("/images/file.png")).toExternalForm(), ConstantSet.ROW_HEIGHT, ConstantSet.ROW_HEIGHT, true, true);
    private static final int DIR_IMAGE_SIZE = 5;
    private static final Image[] dirImage = new Image[DIR_IMAGE_SIZE];

    static {
        for (int i = 0; i < 5; i++) {
            dirImage[i] = new Image(Objects.requireNonNull(ImageUtils.class.getResource("/images/category" + i + ".png")).toExternalForm(), ConstantSet.ROW_HEIGHT, ConstantSet.ROW_HEIGHT, true, true);
        }
    }

    public static ImageView getFileImage() {
        return new ImageView(fileImage);
    }

    public static ImageView getDirImage() {
        return new ImageView(dirImage[new Random().nextInt(DIR_IMAGE_SIZE)]);
    }

    public static ImageView getDirImage(int index) {
        return new ImageView(dirImage[index]);
    }
}
