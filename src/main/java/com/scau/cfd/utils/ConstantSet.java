package com.scau.cfd.utils;

import java.util.HashMap;

/**
 * 这个类是：
 * @author: William
 * @date: 2024-11-03T18:35:56CST 18:35
 * @description:
 */
public class ConstantSet {
    // 图标字体名字
    public static final String FONT_NAME = "MyIcons";
    // 图标字体前缀
    public static final String FONT_PREFIX = "MyIcons-";
    // 图标字体路径
    public static final String FONT_PATH = "/fonts/MyIcons.ttf";
    public static final int ROW_HEIGHT = 100;
    public static final int FILE_NAME_LEN = 2;
    public static final int CATALOG_NAME_LEN = 3;
    public static final int TABLE_HEAD_HEIGHT = 30;
    public static final int CATEGORY = 8;
    public static final int FILE = 4;
    public static final int SYSTEM_FILE = 2;
    public static final int READ_ONLY_FILE = 1;
    public static final HashMap<Integer, String> dirMap = new HashMap<>() {{
        put(8, "目录");
        put(4, "普通文件");
        put(5, "普通文件, 只读文件");
        put(3, "系统文件, 只读文件");
        put(2, "系统文件");
        put(1, "只读文件");
    }};
}
