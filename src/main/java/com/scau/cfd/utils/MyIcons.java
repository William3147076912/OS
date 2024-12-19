package com.scau.cfd.utils;

import lombok.AllArgsConstructor;
import org.kordamp.ikonli.Ikon;

@AllArgsConstructor
public enum MyIcons implements Ikon {
    Help(ConstantSet.FONT_PREFIX + "help", '\ue904'),
    FileType(ConstantSet.FONT_PREFIX + "file-type", '\ue902'),
    FileAttribute(ConstantSet.FONT_PREFIX + "file-attribute", '\ue906'),
    Cancel(ConstantSet.FONT_PREFIX + "cancel", '\ue900'),
    Info(ConstantSet.FONT_PREFIX + "info", '\ue901'),
    Save(ConstantSet.FONT_PREFIX + "save", '\ue903'),
    FileName(ConstantSet.FONT_PREFIX + "file-name", '\ue905');


    private final String description;
    private final int code;

    public static Ikon findIkonByDesc(String description) {
        for (MyIcons value : values()) {
            if (value.getDescription().equals(description)) {
                return value;
            }
        }
        throw new IllegalArgumentException("没有找到对应的图标：" + description);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getCode() {
        return code;
    }
}
