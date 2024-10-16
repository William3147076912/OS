package com.scau.cfd;

import lombok.Data;

import java.util.ArrayList;

/**
 * 这个类用于管理已打开文件的信息。
 *
 * @author: William
 * @date: 2024-10-05T20:47:30CST 20:47
 * @description:
 */
public class OFT {

    private final int N = 5;     // 实验中系统允许打开文件的最大数量
    public ArrayList<OFTLE> file = new ArrayList<>();      // 已打开文件登记表

    /**
     * 判断已打开文件表是否已满。
     *
     * @return 如果已满返回 true，否则返回 false
     */
    public boolean isFull() {
        return file.size() >= N;
    }

    /**
     * 创建文件。
     *
     * @param filename  文件名
     * @param attribute 文件属性
     * @return 成功创建返回 true，否则返回 false
     */
    boolean createFile(String filename, char attribute) {
        return true;
    }

    /**
     * 打开文件。
     *
     * @param filename 文件名
     * @param flag     操作类型（0 表示读，1 表示写）
     * @return 成功打开返回 true，否则返回 false
     */
    boolean openFile(String filename, int flag) {
        return true;
    }

    /**
     * 读文件。
     *
     * @param filename 文件名
     * @param length   读取长度
     * @return 成功读取返回 true，否则返回 false
     */
    boolean readFile(String filename, int length) {
        return true;
    }

    // 已打开文件表中读、写指针的结构
    @Data
    private static class Pointer {
        int dnum;    // 磁盘盘块号
        int bnum;    // 磁盘盘块内第几个字节
    }

    // 已打开文件表项类型定义
    @Data
    private static class OFTLE {
        char[] name = new char[20];     // 文件名
        char attribute;     // 文件的属性，用1个字节表示，所以此用 char 类型
        int number;      // 文件起始盘块号
        int length;      // 文件长度
        int flag;        // 操作类型，用“0”表示以读操作方式打开文件，用“1”表示以写操作方式打开文件
        // 读文件的位置，文件打开时 dnum 为文件起始盘块号，bnum 为“0”
        Pointer read = new Pointer();
        // 写文件的位置，文件刚建立时 dnum 为文件起始盘块号，bnum 为“0”，打开文件时 dnum 和 bnum 为文件的末尾位置
        Pointer write = new Pointer();
    }
}
