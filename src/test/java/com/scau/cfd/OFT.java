package com.scau.cfd;

import lombok.Data;

import java.util.ArrayList;

/**
 * 这个类是：
 *
 * @author: William
 * @date: 2024-10-05T20:47:30CST 20:47
 * @description:
 */
public class OFT {


    private final int N = 5;     //实验中系统允许打开文件的最大数量
    public ArrayList<OFTLE> file = new ArrayList<>();      //已打开文件登记表

    public boolean isFull() {
        return file.size() >= N;
    }

    /**
     * 创建文件
     *
     * @param filename  文件名
     * @param attribute 文件属性
     */
    boolean createFile(String filename, char attribute) {
        return true;
    }

    /**
     * 打开文件
     *
     * @param filename 文件名
     * @param flag     操作类型
     */
    //
    boolean openFile(String filename, int flag) {
        return true;
    }

    /**
     * 读文件
     *
     * @param filename 文件名
     * @param length   读取长度
     */
    boolean readFile(String filename, int length) {
        return true;
    }


    //已打开文件表中读、写指针的结构
    @Data
    private static class Pointer {
        int dnum;    //磁盘盘块号
        int bnum;    //磁盘盘块内第几个字节
    }

    //已打开文件表项类型定义
    @Data
    private static class OFTLE {
        char[] name = new char[20];     //已打开文件表中读、写指针的结构
        char attribute;     //文件的属性，用1个字节表示，所以此用char类型
        int number;      //文件起始盘块号
        int length;      //文件起始盘块号
        int flag;        //操作类型，用“0”表示以读操作方式打开文件，用“1”表示以写操作方式打开文件
        //读文件的位置，文件打开时dnum为文件起始盘块号，bnum为“0”
        Pointer read = new Pointer();
        //写文件的位置，文件刚建立时dnum为文件起始盘块号，bnum为“0 ，打开文件时dnum和bnum为文件的末尾位置
        Pointer write = new Pointer();
    }

}
