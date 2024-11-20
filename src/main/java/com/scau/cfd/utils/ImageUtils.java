package com.scau.cfd.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

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
    @Getter
    private static final Image questionImage = new Image(Objects.requireNonNull(ImageUtils.class.getResource("/images/question.png")).toExternalForm());
    private static final int DIR_IMAGE_SIZE = 5;
    private static final Image[] dirImage = new Image[DIR_IMAGE_SIZE];
    @Getter
    private static final ImageView backImage = new ImageView(new Image(Objects.requireNonNull(ImageUtils.class.getResource("/images/back.png")).toExternalForm(), 40, 40, true, true));

    static {
        for (int i = 0; i < DIR_IMAGE_SIZE; i++) {
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
