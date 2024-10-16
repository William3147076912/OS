package com.scau.cfd;

/**
 * 表示已打开文件的信息。
 */
public class OpenedFile {
    String pathAndFilename;     // 文件的路径和名称
    byte attribute;             // 文件的属性，用1个字节表示
    int number;                 // 文件起始盘块号
    int length;                 // 文件长度
    int flag;                   // 操作类型，用“0”表示以读操作方式打开文件，用“1”表示以写操作方式打开文件
    // 读文件的位置，文件打开时 dnum 为文件起始盘块号，bnum 为“0”
    Pointer read;
    // 写文件的位置，文件刚建立时 dnum 为文件起始盘块号，bnum 为“0”，打开文件时 dnum 和 bnum 为文件的末尾位置
    Pointer write;

    /**
     * 构造函数，初始化已打开文件的基本信息。
     *
     * @param pathAndFilename 文件的路径和名称
     */
    public OpenedFile(String pathAndFilename) {
        this.pathAndFilename = pathAndFilename;
        this.read = new Pointer();  // 初始化读指针
        this.write = new Pointer(); // 初始化写指针
    }

    // 已打开文件表中读、写指针的结构
    public static class Pointer {
        int dnum;    // 磁盘盘块号
        int bnum;    // 磁盘盘块内第几个字节
    }
}
