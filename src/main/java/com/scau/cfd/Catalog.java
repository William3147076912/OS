package com.scau.cfd;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * Catalog 类表示一个目录结构，包含目录名称、属性、位置和父目录等属性。
 * 提供了多个构造函数来创建目录对象，支持不同参数的初始化。
 */
@Data
@AllArgsConstructor
public class Catalog {
    byte[] name = new byte[3]; // 目录名称，初始化为长度为3的字节数组
    byte attribute;             // 目录属性
    byte location;              // 目录位置
    byte parent;                // 父目录

    /**
     * 使用指定名称初始化目录对象的构造函数。
     *
     * @param name 目录名称，从字符串转换为字节数组，使用 US_ASCII 编码。
     *             最多保留3个字节，多余的字符将被截断。
     */
    public Catalog(String name) {
        this.name = new byte[3]; // 初始化 name 属性
        byte[] theName = name.getBytes(StandardCharsets.US_ASCII); // 将输入的名称转换为使用 US_ASCII 编码的字节数组
        for (int i = 0; i < 3 && i < theName.length; i++) {
            this.name[i] = theName[i]; // 将 theName 中的字节复制到 this.name，最多复制3个字节
        }
    }
}
